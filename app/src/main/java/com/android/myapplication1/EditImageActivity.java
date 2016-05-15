package com.android.myapplication1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class EditImageActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int SELECT_IMAGE_FROM_GALLERY = 1;


    private static ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_image_second);
        imageView = (ImageView) findViewById(R.id.edit_image3);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.sss);
        LinearLayout linearLayout1=(LinearLayout)findViewById(R.id.sss1);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEmailAddress();

            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFirstName();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }


    private void editFirstName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditImageActivity.this);
        builder.setTitle("Edit Name");
        final EditText editText = new EditText(EditImageActivity.this);
        final TextView textView = (TextView) findViewById(R.id.txt);
        editText.setText(textView.getText().toString());
        builder.setView(editText);
        editText.setHint("First Name");
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           textView.setText(editText.getText().toString());
            return;
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();

    }

    private void editEmailAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditImageActivity.this);
        builder.setTitle("Edit Email Address");
        final EditText editText = new EditText(EditImageActivity.this);
        final TextView textView = (TextView) findViewById(R.id.txt1);
        editText.setText(textView.getText().toString());
        builder.setView(editText);
        editText.setHint("Email Address");
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(editText.getText().toString());
                return;
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();

    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditImageActivity.this);
        builder.setTitle("Select source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                } else if (items[item].equals("Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Image"),
                            SELECT_IMAGE_FROM_GALLERY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_FROM_GALLERY)
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }



}