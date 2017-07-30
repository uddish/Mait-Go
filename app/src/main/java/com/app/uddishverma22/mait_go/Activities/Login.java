package com.app.uddishverma22.mait_go.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.uddishverma22.mait_go.R;
import com.app.uddishverma22.mait_go.Utils.Globals;
import com.app.uddishverma22.mait_go.Utils.Preferences;
import com.app.uddishverma22.mait_go.Utils.VolleySingleton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;
    Button signInButton;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "Login";
    public static FirebaseUser currentUser = null;
    Typeface tf, tfBold;

    TextView heading, subHeading;
    EditText rollNo, section;
    TextInputLayout rollnoLayout, sectionLayout;
    AVLoadingIndicatorView avi;

    RequestQueue queue;

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/info/";

    private final Handler waitHandler = new Handler();
    private final Runnable waitCallback = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startActivity(new Intent(Login.this, SplashIntroActivity.class));

        signInButton = (Button) findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        attachViews();

        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Login.this, "Google play services error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForNullRollNo() && checkForNullSection()) {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    fetchBranch();
                    signIn();
                    startLoading();
                    InputMethodManager imm = (InputMethodManager) Login.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(signInButton.getWindowToken(), 0);

                }

            }
        });

        section.addTextChangedListener(new TextWatcher() {

            int flag = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.d(TAG, "beforeTextChanged: " + count);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + s);
                if (s.length() == 1 && flag == 0) {
                    s.append("-");
                    flag = 1;
                }
                if (s.length() == 1) {
                    flag = 0;
                }

            }
        });

    }

    private void attachViews() {
        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        tfBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Bold.ttf");
        heading = (TextView) findViewById(R.id.hi_text);
        subHeading = (TextView) findViewById(R.id.subhead);
        rollNo = (EditText) findViewById(R.id.input_rollno);
        section = (EditText) findViewById(R.id.input_class);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

        rollnoLayout = (TextInputLayout) findViewById(R.id.input_layout_rollno);
        sectionLayout = (TextInputLayout) findViewById(R.id.input_layout_class);

        heading.setTypeface(tfBold);
        subHeading.setTypeface(tf);
    }

    private boolean checkForNullRollNo() {
        if (TextUtils.isEmpty(rollNo.getText().toString())) {
            rollnoLayout.setError(getString(R.string.rollno_error));
            requestFocus(rollNo);
            return false;
        } else {
            url = url + rollNo.getText().toString();
            Globals.rollNo = rollNo.getText().toString();
            Preferences.setPrefs("rollNo", rollNo.getText().toString(), getApplicationContext());
            rollnoLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean checkForNullSection() {
        if (TextUtils.isEmpty(section.getText().toString()) || section.getText().toString().length() < 3) {
            sectionLayout.setError(getString(R.string.section_error));
            requestFocus(section);
            return false;
        } else {
            Globals.section = section.getText().toString();
            Preferences.setPrefs("studentSection", Globals.section, getApplicationContext());
            sectionLayout.setErrorEnabled(false);
        }
        Log.d(TAG, "checkForNullSection: " + section.getText().toString().length());
        return true;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                stopLoading();
                Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Preferences.setPrefs("studentName", acct.getDisplayName(), getApplicationContext());
                            Preferences.setPrefs("studentImage", String.valueOf(acct.getPhotoUrl()), getApplicationContext());
                            if (!Preferences.getPrefs("class and section", Login.this).equals("notfound")) {
                                stopLoading();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                waitHandler.postDelayed(waitCallback, 2000);
                            }

                        } else {
                            stopLoading();
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void fetchBranch() {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Globals.semester = String.valueOf(response.get("sem"));

                            if (Globals.semester != null && response.get("branch") != null) {
                                Preferences.setPrefs("studentSemester", Globals.semester, getApplicationContext());
                                Preferences.setPrefs("studentBranch", String.valueOf(response.get("branch")), getApplicationContext());
                                Preferences.setPrefs("class and section", Globals.semester + Globals.section.charAt(0) + Globals.section.charAt(2), Login.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("x-access-token", Globals.X_ACCESS_TOKEN);
                return headers;
            }

        };
        queue.add(objectRequest);
    }

    private void startLoading() {
        avi.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void stopLoading() {
        avi.hide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


}
