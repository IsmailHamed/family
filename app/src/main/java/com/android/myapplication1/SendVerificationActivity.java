package com.android.myapplication1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.sinch.verification.PhoneNumberFormattingTextWatcher;
import com.sinch.verification.PhoneNumberUtils;

import java.util.Locale;

public class SendVerificationActivity extends Activity {

    public static final String SMS = "sms";
    public static final String FLASHCALL = "flashcall";
    public static final String INTENT_PHONENUMBER = "phonenumber";
    public static final String INTENT_METHOD = "method";

    public static final String INTENT_COUNTRYISO = "CountryIso";


    private EditText mPhoneNumber;
    private Button mSmsButton;
    private Button mFlashCallButton;
    private String mCountryIso;
    private TextWatcher mNumberTextWatcher;
    private Intent serviceIntent;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentUser();

        setContentView(R.layout.activity_send_verification);


        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mSmsButton = (Button) findViewById(R.id.smsVerificationButton);


        mCountryIso = PhoneNumberUtils.getDefaultCountryIso(this);
        final String defaultCountryName = new Locale("", mCountryIso).getDisplayName();
        final CountrySpinner spinner = (CountrySpinner) findViewById(R.id.spinner);
        spinner.init(defaultCountryName);
        spinner.addCountryIsoSelectedListener(new CountrySpinner.CountryIsoSelectedListener() {
            @Override
            public void onCountryIsoSelected(String selectedIso) {
                if (selectedIso != null) {
                    mCountryIso = selectedIso;
                    resetNumberTextWatcher(mCountryIso);
                    // force update:
                    mNumberTextWatcher.afterTextChanged(mPhoneNumber.getText());
                }
            }
        });
        resetNumberTextWatcher(mCountryIso);

        tryAndPrefillPhoneNumber();
    }

    private void tryAndPrefillPhoneNumber() {
        if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber.setText(manager.getLine1Number());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tryAndPrefillPhoneNumber();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "This application needs permission to read your phone number to automatically "
                        + "pre-fill it", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openActivity(String phoneNumber, String method, String countryIso) {
        Intent verification = new Intent(this, VerificationActivity.class);
        verification.putExtra(INTENT_PHONENUMBER, phoneNumber);
        verification.putExtra(INTENT_METHOD, method);

        verification.putExtra(INTENT_COUNTRYISO, countryIso);

        startActivity(verification);

    }

    private void setButtonsEnabled(boolean enabled) {
        mSmsButton.setEnabled(enabled);
    }

    public void onButtonClicked(View view) {
        if (view == mSmsButton) {
            openActivity(getE164Number(), SMS, mCountryIso);
        } else if (view == mFlashCallButton) {
            openActivity(getE164Number(), FLASHCALL, mCountryIso);
        }
    }

    private void resetNumberTextWatcher(String countryIso) {

        if (mNumberTextWatcher != null) {
            mPhoneNumber.removeTextChangedListener(mNumberTextWatcher);
        }

        mNumberTextWatcher = new PhoneNumberFormattingTextWatcher(countryIso) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                super.beforeTextChanged(s, start, count, after);
            }

            @Override
            public synchronized void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (isPossiblePhoneNumber()) {
                    setButtonsEnabled(true);
                    mPhoneNumber.setTextColor(Color.BLACK);
                } else {
                    setButtonsEnabled(false);
                    mPhoneNumber.setTextColor(Color.RED);
                }
            }
        };

        mPhoneNumber.addTextChangedListener(mNumberTextWatcher);
    }

    private boolean isPossiblePhoneNumber() {
        return PhoneNumberUtils.isPossibleNumber(mPhoneNumber.getText().toString(), mCountryIso);
    }

    private String getE164Number() {
        return PhoneNumberUtils.formatNumberToE164(mPhoneNumber.getText().toString(), mCountryIso);
    }

    public void CurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
             intent = new Intent(SendVerificationActivity.this, ListUsersActivity.class);
            startActivity(intent);
            serviceIntent = new Intent(getApplicationContext(), MessageService.class);
            startService(serviceIntent);
            // do stuff with the user
        } else {
            // show the signup or login screen
//            Intent intent = new Intent(SendVerificationActivity.this, VerificationActivity.class);
//            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }
}
