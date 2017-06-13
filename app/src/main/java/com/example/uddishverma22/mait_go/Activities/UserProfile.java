package com.example.uddishverma22.mait_go.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uddishverma22.mait_go.MainActivity;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.HideKeyboard;
import com.example.uddishverma22.mait_go.Utils.Preferences;
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
    private static final int KITKAT_VALUE = 1789;


    TextView name, roll, branch, semester, className, branchHeading, classHeading, semesterHeading;
    public static int themeColor = 0;
    LinearLayout branchSelector, semesterSelector, classSelector;
    Typeface tf;

    //SharedPrefs values
    String studentRollNo;
    String studentName;
    String studentSection;

    //Components to change image in the profile page
    CircleImageView profileImage;
    public static final int IMAGE_CODE = 2990;
    private static Uri profilePicUri;
    Drawable profilePicDrawable;
    ImageView blurredBackImage;
    Display display;
    Point size;
    String studentPicPath = null;

    //barcode ticket components
    ImageView leftCircle, rightCircle, barcodeImg;

    BottomSheetBehavior branchBottomSheetBehavior, classBottomSheetBehavior;
    NestedScrollView branchBottomSheet, classBottomSheet;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;


    AlertDialog.Builder alert;
    View alertLayout;
    TextView branchCs, branchIt, branchEce, branchEee, branchMae;            //tv for the branch bottomsheet
    TextView first, second, third, fourth, fifth, sixth, seventh, eighth;    //tv for the popup semester menu
    EditText classCharacter, classNo;                                       //et for the class bottomsheet
    ImageView classDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Back Button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.name_tv);
        roll = (TextView) findViewById(R.id.enrollment_tv);

        profileImage = (CircleImageView) findViewById(R.id.circleImageView);
        blurredBackImage = (ImageView) findViewById(R.id.profile_blur_img);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Regular.ttf");
        branch = (TextView) findViewById(R.id.branch_tv);
        semester = (TextView) findViewById(R.id.semester_tv);
        className = (TextView) findViewById(R.id.class_tv);
        branch.setTypeface(tf);
        semester.setTypeface(tf);
        className.setTypeface(tf);


        branchSelector = (LinearLayout) findViewById(R.id.branch_selector);
        semesterSelector = (LinearLayout) findViewById(R.id.semester_selector);
        classSelector = (LinearLayout) findViewById(R.id.class_selector);

        setStudentDetails();

        deriveSemesterFromRoll();

        setProfilePic();

        //Bottom sheet action
        branchBottomSheet = (NestedScrollView) findViewById(R.id.branch_bottomsheet);
        classBottomSheet = (NestedScrollView) findViewById(R.id.class_bottomsheet);
        branchBottomSheetBehavior = BottomSheetBehavior.from(branchBottomSheet);
        classBottomSheetBehavior = BottomSheetBehavior.from(classBottomSheet);
        branchBottomSheetBehavior.setPeekHeight(0);
        classBottomSheetBehavior.setPeekHeight(0);


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
                branchSelector();
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
                classSelector();
            }
        });

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Regular.ttf");
        name.setTypeface(tf);

    }

    private void setProfilePic() {

        studentPicPath = Preferences.getPrefs("studentImage", getApplicationContext());
        if (!studentPicPath.equals("notfound")) {
            Log.d(TAG, "onCreate: Profile Pic inside LOOP " + studentPicPath);
            Picasso.with(this).load(studentPicPath).into(profileImage);

            //Getting screen's width and height
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            Picasso.with(this).load(studentPicPath).resize(width, 1000).centerCrop().into(blurredBackImage);

        }
    }

    private void setStudentDetails() {
        studentName = Preferences.getPrefs("studentName", getApplicationContext());
        studentRollNo = Preferences.getPrefs("studentRollNo", getApplicationContext());
        studentSection = Preferences.getPrefs("studentSection", getApplicationContext());
        if (!studentName.equals("notfound") && !studentRollNo.equals("notfound") && !studentSection.equals("notfound")) {
            name.setText(studentName);
            roll.setText(studentRollNo);
            className.setText(studentSection);
        }
    }

    public void deriveSemesterFromRoll() {
        studentRollNo = Preferences.getPrefs("studentRollNo", getApplicationContext());
        Log.d(TAG, "deriveSemesterFromRoll: " + studentRollNo.substring(studentRollNo.length() - 2, studentRollNo.length()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KITKAT_VALUE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: Request code is inside KITKAT");
                profilePicUri = data.getData();
                Picasso.with(this).load(profilePicUri).into(profileImage);
                Preferences.setPrefs("studentImage", String.valueOf(profilePicUri), getApplicationContext());

                //Updating the image in nav header in navigation drawer
                Picasso.with(this).load(profilePicUri).into(MainActivity.navHeaderImage);

                //Getting screen's width and height
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                Picasso.with(this).load(profilePicUri).resize(width, 1000).centerCrop().into(blurredBackImage);

            }
        } else {
            Log.d(TAG, "onActivityResult: Request code is NOT INSIDE KITKAT");
            profilePicUri = data.getData();
            //setting profile pic in shared prefs
            Preferences.setPrefs("studentImage", String.valueOf(profilePicUri), getApplicationContext());
            Picasso.with(this).load(profilePicUri).into(profileImage);

            //Updating the image in nav header in navigation drawer
            Picasso.with(this).load(profilePicUri).into(MainActivity.navHeaderImage);

            //Getting screen's width and height
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            //Loading the blurred image
            Picasso.with(this).load(profilePicUri).resize(width, 1000).centerCrop().into(blurredBackImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    private void branchSelector() {

        branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        branchCs = (TextView) findViewById(R.id.branch_cs);
        branchIt = (TextView) findViewById(R.id.branch_it);
        branchEce = (TextView) findViewById(R.id.branch_ece);
        branchEee = (TextView) findViewById(R.id.branch_eee);
        branchMae = (TextView) findViewById(R.id.branch_mae);

        branchCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(branchCs.getText().toString());
                branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        branchIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(branchIt.getText().toString());
                branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        branchEce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(branchEce.getText().toString());
                branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        branchEee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(branchEee.getText().toString());
                branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        branchMae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(branchMae.getText().toString());
                branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    private void classSelector() {

        classBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        classBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        className.setText(classCharacter.getText().toString() + "-" + classNo.getText().toString());
                        //hiding the keyboard
                        InputMethodManager imm = (InputMethodManager) MainActivity.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(classDone.getWindowToken(), 0);
                        Preferences.setPrefs("studentSection", classCharacter.getText().toString() + "-" + classNo.getText().toString(),
                                getApplicationContext());
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        classCharacter = (EditText) findViewById(R.id.class_char);
        classNo = (EditText) findViewById(R.id.class_no);
        classDone = (ImageView) findViewById(R.id.class_done);

        //TODO optimise keyboard hiding when bottomsheet is collapsed
        classDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(classCharacter.getText().toString())) {
                    classBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    Toast.makeText(UserProfile.this, "Please enter your class", Toast.LENGTH_SHORT).show();
                }
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

