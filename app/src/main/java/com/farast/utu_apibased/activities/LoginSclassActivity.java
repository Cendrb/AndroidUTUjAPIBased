package com.farast.utu_apibased.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.utu_spinner.ToStringConverter;
import com.farast.utu_apibased.custom_views.utu_spinner.UtuAdapter;
import com.farast.utu_apibased.exceptions.WTFIsHappeningException;
import com.farast.utu_apibased.tasks.UtuPredataDownloader;
import com.farast.utu_apibased.tasks.UtuSubmitter;
import com.farast.utuapi.data.Sclass;
import com.farast.utuapi.exceptions.APIRequestException;
import com.farast.utuapi.exceptions.ClientPredataNotLoadedException;

import java.util.List;

import static com.farast.utu_apibased.Bullshit.dataLoader;

/**
 * A login screen that offers login via email/password.
 */
public class LoginSclassActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextInputEditText mEmailView;
    private TextInputEditText mPasswordView;
    private ProgressBar mProgressBarView;
    private LinearLayout mProgressView;
    private TextView mProgressTextView;
    private View mLoginFormView;
    private Spinner mSclassSelector;
    private Button mLoadWithoutLoginButton;
    private View mLoadWithoutLoginForm;

    private Activity mActivity;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_login_sclass);

        mActivity = this;
        mPreferences = getPreferences(MODE_PRIVATE);

        // Set up the login form.
        mEmailView = (TextInputEditText) findViewById(R.id.email);

        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSclassSelector = (Spinner) findViewById(R.id.sclassSelector);

        mLoadWithoutLoginForm = (LinearLayout) findViewById(R.id.loadWithoutLoginForm);

        mLoadWithoutLoginButton = (Button) findViewById(R.id.loadWithoutLoginButton);
        mLoadWithoutLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Object selected = mSclassSelector.getSelectedItem();
                if (selected != null && selected instanceof Sclass) {
                    Sclass sclass = (Sclass) selected;
                    startMain(sclass.getId());
                } else
                    throw new WTFIsHappeningException("Selected item should be Sclass");
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressBarView = (ProgressBar) findViewById(R.id.login_progress_bar);
        mProgressView = (LinearLayout) findViewById(R.id.login_progress);
        mProgressTextView = (TextView) findViewById(R.id.login_progress_text);

        mEmailView.setText(mPreferences.getString("email", ""));
        mPasswordView.setText(mPreferences.getString("password", ""));

        new LoginSclassUtuPredataDownloader(this).execute();
    }

    private void startMain(int sclassId) {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra("sclass_id", sclassId);
        mActivity.startActivity(intent);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for empty
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    public class UserLoginTask extends UtuSubmitter {

        private final String mEmail;
        private final String mPassword;

        private boolean mLoginSuccessful = false;

        UserLoginTask(String email, String password) {
            super(LoginSclassActivity.this);
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void executeInBackground() throws APIRequestException {
            mLoginSuccessful = Bullshit.dataLoader.login(mEmail, mPassword);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            mProgressTextView.setText(R.string.operation_logging_in);
            mLoginSuccessful = false;
        }

        @Override
        protected void onFinished(final boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                if (mLoginSuccessful) {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("email", mEmail);
                    editor.putString("password", mPassword);
                    editor.apply();
                    startMain(dataLoader.getCurrentUser().getSclassId());
                } else {
                    mPasswordView.setError(getString(R.string.error_login_incorrect));
                    mPasswordView.requestFocus();
                }
            } else {
                if (!mActivity.isFinishing())
                    new AlertDialog.Builder(mActivity)
                            .setTitle(R.string.error_failed_to_connect)
                            .setMessage(R.string.try_again)
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // just close the dialog
                                }
                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new UserLoginTask(mEmail, mPassword).execute();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private class LoginSclassUtuPredataDownloader extends UtuPredataDownloader {
        public LoginSclassUtuPredataDownloader(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            mProgressTextView.setText(R.string.operation_downloading_predata);
        }

        @Override
        protected void onFinished(boolean success) {
            try {
                showProgress(false);
                if (success) {
                    List<Sclass> sclasses = dataLoader.getSclasses();
                    final UtuAdapter<Sclass> androidSucksAdapter = new UtuAdapter<>(mActivity, sclasses, new ToStringConverter<Sclass>() {
                        @Override
                        public String stringify(Sclass object) {
                            return object.getName();
                        }
                    });
                    mSclassSelector.setAdapter(androidSucksAdapter);
                } else if (!mActivity.isFinishing())
                    new AlertDialog.Builder(mActivity)
                            .setTitle(R.string.error_failed_to_connect)
                            .setMessage(R.string.try_again)
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new LoginSclassUtuPredataDownloader(mContext).execute();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
            } catch (ClientPredataNotLoadedException e) {
                // this should never happen
                new LoginSclassUtuPredataDownloader(mContext).execute();
            }
        }
    }
}

