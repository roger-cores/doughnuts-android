package com.frostox.doughnuts.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.frostox.doughnuts.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameText, emailText, passwordText, rePasswordText;
    private Button registerButton;
    private ImageView appIcon;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    Animation rotation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setFillAfter(true);
        rotation.setFillAfter(true);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(RegisterActivity.class.getName(), "onAuthStateChanged:signed_in:" + user.getUid());

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userNameText.getText().toString())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(RegisterActivity.class.getName(), "UserName updated.");
                                    }
                                }
                            });

                    finish();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(RegisterActivity.class.getName(), "onAuthStateChanged:signed_out");
                }
            }
        };


        userNameText = (EditText) findViewById(R.id.activity_register_uname);
        emailText = (EditText) findViewById(R.id.activity_register_email);
        passwordText = (EditText) findViewById(R.id.activity_register_password);
        rePasswordText = (EditText) findViewById(R.id.activity_register_repassword);
        appIcon = (ImageView) findViewById(R.id.activity_register_app_icon);
        Picasso.with(this).load(R.drawable.doughnut).into(appIcon);
        registerButton = (Button) findViewById(R.id.activity_register_sign_up);

    }

    public void register(View view){
        String uname = userNameText.getText().toString();
        String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();
        String rePassword = rePasswordText.getText().toString();

        if(uname.equals("")){
            userNameText.setError("Are you gonna fill this or should I?");
            return;
        } else if(uname.contains(" ")){
            userNameText.setError("I don't like these spaces, please remove them!");
            return;
        } else if(email.equals("")){
            emailText.setError(getString(R.string.email_needed));
            return;
        } else if((!email.contains("@")) || (!email.contains(".")) || (email.indexOf('@')>email.lastIndexOf('.'))){
            emailText.setError(getString(R.string.invalid_email));
            return;
        } else if(password.equals("")){
            passwordText.setError("How are ya ever gonna login without a password?");
            return;
        } else if(password.length() < 6){
            passwordText.setError("That's nice, but how about a password with more than 5 characters?");
            return;
        } else if(!password.equalsIgnoreCase(rePassword)){
            rePasswordText.setError("The passwords don't match!");
            return;
        }


        appIcon.startAnimation(rotation);
        registerButton.setEnabled(false);
        registerButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(RegisterActivity.class.getName(), "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            appIcon.clearAnimation();
                            rotation.cancel();
                            registerButton.setEnabled(true);
                            registerButton.setTextColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.white));
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                passwordText.setError("Google didn't like this password, change it!");
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                emailText.setError(getString(R.string.invalid_email));
                            } catch(FirebaseAuthUserCollisionException e) {
                                emailText.setError("This email is taken");
                            } catch(Exception e) {
                                Toast.makeText(RegisterActivity.this, "Sign up failed because " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
