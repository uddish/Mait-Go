package com.example.uddishverma22.mait_go.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends AppCompatActivity {

    public static final String TAG = "UserProfile";


    TextView name, roll, branch, semester, className, branchHeading, classHeading, semesterHeading;
    ImageView orangeThemeButton;
    public static int themeColor = 0;
    RelativeLayout diagonalLayout;
    LinearLayout branchSelector, semesterSelector, classSelector, outlineBox;
    Typeface tf;

    //Components to change image in the profile page
    CircleImageView profileImage;
    public static final int IMAGE_CODE = 2990;
    public static final int KITKAT_VALUE = 1002;
    private static Uri profilePicUri;
    Drawable profilePicDrawable;
    ImageView dpImage, blurredBackImage;
    Display display;
    Point size;

    //barcode ticket components
    ImageView leftCircle, rightCircle, barcodeImg;
    ScrollView backgroundLayout;
    RelativeLayout barcodeBackground;


    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;


    AlertDialog.Builder alert;
    View alertLayout;
    TextView alertCs, alertIt, alertEce, alertEee, alertMae;            //tv for the popup branch menu
    TextView first, second, third, fourth, fifth, sixth, seventh, eighth;            //tv for the popup semester menu
    EditText classEditText;
    TextView classAlertHeading;
    ImageView classImg;                            //tv and et for the popup class menu


    /**
     * YELLOW -> 101
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Regular.ttf");

        name = (TextView) findViewById(R.id.name_tv);
        roll = (TextView) findViewById(R.id.enrollment_tv);

        profileImage = (CircleImageView) findViewById(R.id.circleImageView);
        blurredBackImage = (ImageView) findViewById(R.id.profile_blur_img);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();


        branch = (TextView) findViewById(R.id.branch_tv);
        semester = (TextView) findViewById(R.id.semester_tv);
        className = (TextView) findViewById(R.id.class_tv);
        branch.setTypeface(tf);
        semester.setTypeface(tf);
        className.setTypeface(tf);


        branchSelector = (LinearLayout) findViewById(R.id.branch_selector);
        semesterSelector = (LinearLayout) findViewById(R.id.semester_selector);
        classSelector = (LinearLayout) findViewById(R.id.class_selector);

        //TODO Store the profile pic in shared preference
        //Setting the profile pic
        if (profilePicUri != null) {
            Picasso.with(this).load(profilePicUri).into(profileImage);
            Picasso.with(this).load(profilePicUri).into(blurredBackImage);

            //Getting screen's width and height
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            Picasso.with(this).load(profilePicUri).resize(width, 1000).centerCrop().into(blurredBackImage);

        }

        //Theme selectors
        orangeThemeButton = (ImageView) findViewById(R.id.orange_theme);
        diagonalLayout = (RelativeLayout) findViewById(R.id.diagonal_view);
        backgroundLayout = (ScrollView) findViewById(R.id.background_layout);
        outlineBox = (LinearLayout) findViewById(R.id.outline_box);
        barcodeBackground = (RelativeLayout) findViewById(R.id.barcode_background);

        //Barcode generation function
        barcodeImg = (ImageView) findViewById(R.id.barcode_img);
        leftCircle = (ImageView) findViewById(R.id.left_ticket_circle);
        rightCircle = (ImageView) findViewById(R.id.right_ticket_circle);

        //************************ LOADING IMAGE VIA ASYNCTASK INTO TICKET ***************************************
        String barcodeData = "2CS131";
        Bitmap bitmap = null;

        bitmap = encodeAsBitmap(barcodeData, BarcodeFormat.CODE_39, 300, 100);

//        try {
//            Bitmap asyncBitmap  = new ImageLoader(barcodeData, 3, getApplicationContext()).execute().get();
//                barcodeImg.setImageBitmap(asyncBitmap);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        barcodeImg.setImageBitmap(bitmap);

        //*********************************************************************************************************

        branchHeading = (TextView) findViewById(R.id.branch_heading);
        classHeading = (TextView) findViewById(R.id.class_heading);
        semesterHeading = (TextView) findViewById(R.id.semester_heading);
        branchHeading.setTypeface(tf);
        classHeading.setTypeface(tf);
        semesterHeading.setTypeface(tf);

        //Custom alert dialog to pick the branch
        alert = new AlertDialog.Builder(UserProfile.this);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, KITKAT_VALUE);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    startActivityForResult(intent, KITKAT_VALUE);
                }

            }
        });

        branchSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertBranchDialog();
            }
        });
        semesterSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertSemesterDialog();
            }
        });
        classSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertClassDialog();
            }
        });


        //Checking for theme
        if (themeColor == 101) {
            diagonalLayout.setBackgroundResource(R.drawable.diagonal_background_yellow);
        }

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Regular.ttf");
        name.setTypeface(tf);

        orangeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor = 101;
                diagonalLayout.setBackgroundResource(R.drawable.diagonal_background_yellow);
                backgroundLayout.setBackgroundResource(R.color.darkYellow);
                outlineBox.setBackgroundResource(R.drawable.custom_box_yell);
//                leftCircle.setBackgroundResource(R.drawable.ticket_circle_yell);
//                rightCircle.setBackgroundResource(R.drawable.ticket_circle_yell);
//                barcodeBackground.setBackgroundResource(R.color.darkYellow);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KITKAT_VALUE) {
            if (resultCode == Activity.RESULT_OK) {
                profilePicUri = data.getData();
                Picasso.with(this).load(profilePicUri).into(profileImage);
                //Getting screen's width and height
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                Picasso.with(this).load(profilePicUri).resize(width, 1000).centerCrop().into(blurredBackImage);

//                Drawable img = getDrawableFromUri();
//                Resources res = getApplicationContext().getResources();
//                LayerDrawable mDrawable = (LayerDrawable) res.getDrawable(R.drawable.diagonal_background_blue);
//                mDrawable.setDrawableByLayerId(R.id.diagonal_profile_image, img);
//                diagonalLayout.setBackground(mDrawable);
                //Bitmap layer2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profilePicUri);
//                Drawable layer1 = getResources().getDrawable( R.drawable.layer_one);
//                Drawable layer2 = getResources().getDrawable( R.drawable.profile);
//                Drawable layer3 = getResources().getDrawable( R.drawable.diagonal_background);
//                Drawable[] layers = {layer1, layer2, layer3};
//                LayerDrawable splash_test = new LayerDrawable(layers);
//                diagonalLayout.setBackground(splash_test);

            }
        }
        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profilePicUri = data.getData();
            Picasso.with(this).load(profilePicUri).into(profileImage);
            //Getting screen's width and height
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            //Loading the blurred image
            Picasso.with(this).load(profilePicUri).resize(width, 1000).centerCrop().into(blurredBackImage);
        }
    }

    private Drawable getDrawableFromUri() {
        try {
            InputStream stream = getContentResolver().openInputStream(profilePicUri);
            profilePicDrawable = Drawable.createFromStream(stream, profilePicUri.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return profilePicDrawable;
    }

    private void alertBranchDialog() {

        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.branch_selector_layout, null);
        alert.setView(alertLayout);

        alertCs = (TextView) alertLayout.findViewById(R.id.alert_cs);
        alertIt = (TextView) alertLayout.findViewById(R.id.alert_it);
        alertEce = (TextView) alertLayout.findViewById(R.id.alert_ece);
        alertEee = (TextView) alertLayout.findViewById(R.id.alert_eee);
        alertMae = (TextView) alertLayout.findViewById(R.id.alert_mae);

        final AlertDialog dialog = alert.create();
        dialog.show();

        alertCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertCs.getText().toString());
                dialog.dismiss();
            }
        });
        alertIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertIt.getText().toString());
                dialog.dismiss();
            }
        });
        alertEce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertEce.getText().toString());
                dialog.dismiss();
            }
        });
        alertEee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertEee.getText().toString());
                dialog.dismiss();
            }
        });
        alertMae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertMae.getText().toString());
                dialog.dismiss();
            }
        });

    }

    private void alertSemesterDialog() {

        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.semester_selector_layout, null);
        alert.setView(alertLayout);

        first = (TextView) alertLayout.findViewById(R.id.alert_first);
        second = (TextView) alertLayout.findViewById(R.id.alert_second);
        third = (TextView) alertLayout.findViewById(R.id.alert_third);
        fourth = (TextView) alertLayout.findViewById(R.id.alert_fourth);
        fifth = (TextView) alertLayout.findViewById(R.id.alert_fifth);
        sixth = (TextView) alertLayout.findViewById(R.id.alert_sixth);
        seventh = (TextView) alertLayout.findViewById(R.id.alert_seventh);
        eighth = (TextView) alertLayout.findViewById(R.id.alert_eighth);

        final AlertDialog dialog = alert.create();
        dialog.show();

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(first.getText().toString());
                dialog.dismiss();
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(second.getText().toString());
                dialog.dismiss();
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(third.getText().toString());
                dialog.dismiss();
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(fourth.getText().toString());
                dialog.dismiss();
            }
        });
        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(fifth.getText().toString());
                dialog.dismiss();
            }
        });
        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(sixth.getText().toString());
                dialog.dismiss();
            }
        });
        seventh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(seventh.getText().toString());
                dialog.dismiss();
            }
        });
        eighth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(eighth.getText().toString());
                dialog.dismiss();
            }
        });

    }

    private void alertClassDialog() {
        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.class_selector_layout, null);
        alert.setView(alertLayout);

        classAlertHeading = (TextView) alertLayout.findViewById(R.id.alert_class_heading);
        classEditText = (EditText) alertLayout.findViewById(R.id.alert_class);
        classImg = (ImageView) alertLayout.findViewById(R.id.class_img);

        final AlertDialog dialog = alert.create();
        dialog.show();

        classImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                className.setText(classEditText.getText().toString());
            }
        });

    }

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }

        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            iae.printStackTrace();
            return null;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}

