package com.example.uddishverma22.mait_go.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Globals;
import com.example.uddishverma22.mait_go.Utils.Preferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.util.EnumMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//TODO image not setting from prefs when phone is restarted
public class UserProfile extends AppCompatActivity {

    public static final String TAG = "UserProfile";
    private static final int KITKAT_VALUE = 1789;


    TextView name, roll, branch, semester, className, branchHeading, classHeading, semesterHeading;
    public static int themeColor = 0;
    LinearLayout branchSelector, semesterSelector, classSelector;
    Typeface tf, tfLight;
    Button signOut;
    Toolbar toolbar;

    //SharedPrefs values
    String studentRollNo;
    String studentName;
    String studentSection;
    String studentBranch;
    String studentSemester;

    //Components to change image in the profile page
    CircleImageView profileImage;
    private static Uri profilePicUri;
    Drawable profilePicDrawable;
    ImageView blurredBackImage;
    Display display;
    Point size;
    String studentPicPath = null;

    //barcode ticket components
    ImageView leftCircle, rightCircle, barcodeImg;

    BottomSheetBehavior branchBottomSheetBehavior, classBottomSheetBehavior, semesterBottomSheetBehavior;
    NestedScrollView branchBottomSheet, classBottomSheet, semesterBottomSheet;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    TextView aboutUs, license, moreTitle, buildVersion, buildTitle;

    AlertDialog.Builder alert;
    TextView branchCs, branchIt, branchEce, branchEee, branchMae;            //tv for the branch bottomsheet
    TextView first, second, third, fourth, fifth, sixth, seventh, eighth;    //tv for the popup semester menu
    EditText classCharacter, classNo;                                       //et for the class bottomsheet
    ImageView classDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        attachViews();

        setToolbar();

        setStudentDetails();

        setProfilePic();

        //Bottom sheet action
        branchBottomSheet = (NestedScrollView) findViewById(R.id.branch_bottomsheet);
        classBottomSheet = (NestedScrollView) findViewById(R.id.class_bottomsheet);
        semesterBottomSheet = (NestedScrollView) findViewById(R.id.semester_bottomsheet);
        branchBottomSheetBehavior = BottomSheetBehavior.from(branchBottomSheet);
        classBottomSheetBehavior = BottomSheetBehavior.from(classBottomSheet);
        semesterBottomSheetBehavior = BottomSheetBehavior.from(semesterBottomSheet);
        branchBottomSheetBehavior.setPeekHeight(0);
        classBottomSheetBehavior.setPeekHeight(0);
        semesterBottomSheetBehavior.setPeekHeight(0);


//        //Barcode generation function
//        barcodeImg = (ImageView) findViewById(R.id.barcode_img);
//        leftCircle = (ImageView) findViewById(R.id.left_ticket_circle);
//        rightCircle = (ImageView) findViewById(R.id.right_ticket_circle);

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
//        barcodeImg.setImageBitmap(bitmap);

        //*********************************************************************************************************

        branchHeading = (TextView) findViewById(R.id.branch_heading);
        classHeading = (TextView) findViewById(R.id.class_heading);
        semesterHeading = (TextView) findViewById(R.id.semester_heading);
        branchHeading.setTypeface(tf);
        classHeading.setTypeface(tf);
        semesterHeading.setTypeface(tf);

        //Custom alert dialog to pick the branch
        alert = new AlertDialog.Builder(UserProfile.this);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Preferences.deletePrefs(getApplicationContext());
                Globals.IS_OPENED_FIRST_TIME = 1998;
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("*/*");
                        startActivityForResult(intent, KITKAT_VALUE);
                    } else {
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("*/*");
                        startActivityForResult(intent, KITKAT_VALUE);
                    }
                }

            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, AboutUs.class));
            }
        });

        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, OpenSourceLicense.class));
            }
        });

//        branchSelector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                branchSelector();
//            }
//        });
//        semesterSelector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                semesterSelector();
//            }
//        });
//        classSelector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                classSelector();
//            }
//        });

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Regular.ttf");
        name.setTypeface(tf);

    }

    private void attachViews() {

        name = (TextView) findViewById(R.id.name_tv);
        roll = (TextView) findViewById(R.id.enrollment_tv);

        signOut = (Button) findViewById(R.id.btn_signout);

        profileImage = (CircleImageView) findViewById(R.id.circleImageView);
        blurredBackImage = (ImageView) findViewById(R.id.profile_blur_img);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Regular.ttf");
        tfLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Light.ttf");
        branch = (TextView) findViewById(R.id.branch_tv);
        semester = (TextView) findViewById(R.id.semester_tv);
        className = (TextView) findViewById(R.id.class_tv);
        branch.setTypeface(tf);
        semester.setTypeface(tf);
        className.setTypeface(tf);

        branchSelector = (LinearLayout) findViewById(R.id.branch_selector);
        semesterSelector = (LinearLayout) findViewById(R.id.semester_selector);
        classSelector = (LinearLayout) findViewById(R.id.class_selector);

        aboutUs = (TextView) findViewById(R.id.about_us);
        license = (TextView) findViewById(R.id.license);
        moreTitle = (TextView) findViewById(R.id.more_title);
        buildVersion = (TextView) findViewById(R.id.build_version);
        buildTitle = (TextView) findViewById(R.id.build_title);

        aboutUs.setTypeface(tfLight);
        license.setTypeface(tfLight);
        moreTitle.setTypeface(tf);
        buildVersion.setTypeface(tf);
        buildTitle.setTypeface(tfLight);

        buildVersion.setText(Globals.BUILD_VERSION);

    }

    private void setProfilePic() {

        studentPicPath = Preferences.getPrefs("studentImage", getApplicationContext());
        if (!studentPicPath.equals("notfound")) {
            Picasso.with(this).load(studentPicPath).into(profileImage);
            //Getting screen's width and height
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            Picasso.with(this).load(studentPicPath).resize(width, 1000).centerCrop().into(blurredBackImage);
        } else {
            Picasso.with(this).load(R.drawable.ic_account_circle_white_48dp).into(profileImage);
        }
    }

    private void setStudentDetails() {
        studentName = Preferences.getPrefs("studentName", getApplicationContext());
        studentRollNo = Preferences.getPrefs("rollNo", getApplicationContext());
        studentSection = Preferences.getPrefs("studentSection", getApplicationContext());
        studentBranch = Preferences.getPrefs("studentBranch", getApplicationContext());
        studentSemester = Preferences.getPrefs("studentSemester", getApplicationContext());
        Log.d(TAG, "setStudentDetails:  + " + studentBranch + " " + studentName + " " + studentRollNo);
        if (!studentName.equals("notfound") && !studentRollNo.equals("notfound")
                && !studentSection.equals("notfound") && !studentSemester.equals("notfound")) {
            name.setText(studentName);
            roll.setText(studentRollNo);
            className.setText(studentSection);
            branch.setText(studentBranch);
            semester.setText(studentSemester);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KITKAT_VALUE) {

            if (resultCode == Activity.RESULT_OK && data != null) {
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
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    private void branchSelector() {

        classBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        branchBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (!TextUtils.isEmpty("N/A")) {
                            Preferences.setPrefs("studentBranch", branch.getText().toString(),
                                    getApplicationContext());
                        }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

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

    private void semesterSelector() {

        classBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        semesterBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (!TextUtils.isEmpty("N/A")) {
                            Preferences.setPrefs("studentSemester", semester.getText().toString(),
                                    getApplicationContext());
                        }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        first = (TextView) findViewById(R.id.semester_one);
        second = (TextView) findViewById(R.id.semester_two);
        third = (TextView) findViewById(R.id.semester_three);
        fourth = (TextView) findViewById(R.id.semester_four);
        fifth = (TextView) findViewById(R.id.semester_five);
        sixth = (TextView) findViewById(R.id.semester_six);
        seventh = (TextView) findViewById(R.id.semester_seven);
        eighth = (TextView) findViewById(R.id.semester_eight);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(first.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(second.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(third.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(fourth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(fifth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(sixth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        seventh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(seventh.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        eighth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(eighth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }

    private void classSelector() {

        branchBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Read Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Read Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Read Permission is granted");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Write Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Write Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Write Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("*/*");
                        startActivityForResult(intent, KITKAT_VALUE);
                    } else {
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("*/*");
                        startActivityForResult(intent, KITKAT_VALUE);
                    }
                }
                break;
        }
    }

}

