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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    EditText emailText, passwordText;
    ImageView appIcon;
    Button signInButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Animation rotation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setFillAfter(true);
        rotation.setFillAfter(true);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(LoginActivity.class.getName(), "onAuthStateChanged:signed_in:" + user.getUid());
                    finish();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(LoginActivity.class.getName(), "onAuthStateChanged:signed_out");
                }
            }
        };


        emailText = (EditText) findViewById(R.id.activity_login_email);
        passwordText = (EditText) findViewById(R.id.activity_login_password);
        appIcon = (ImageView) findViewById(R.id.activity_login_app_icon);
        Picasso.with(this).load(R.drawable.doughnut).into(appIcon);
        signInButton = (Button) findViewById(R.id.activity_login_sign_in);
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

    public void signIn(View view){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(email.equals("")){
            emailText.setError(getString(R.string.email_needed));
            return;
        } else if((!email.contains("@")) || (!email.contains(".")) || (email.indexOf('@')>email.lastIndexOf('.'))){
            emailText.setError(getString(R.string.invalid_email));
            return;
        } else if(password.equals("")){
            passwordText.setError(getString(R.string.password_needed));
            return;
        }

        appIcon.startAnimation(rotation);
        signInButton.setEnabled(false);
        signInButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LoginActivity.class.getName(), "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            appIcon.clearAnimation();
                            rotation.cancel();
                            signInButton.setEnabled(true);
                            signInButton.setTextColor(ContextCompat.getColor(LoginActivity.this, android.R.color.white));
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e){
                                emailText.setError("I can't find this email address in my ledger? Who are you?");
                            } catch(Exception e) {
                                Toast.makeText(LoginActivity.this, "Sign up failed because " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        // ...
                    }
                });
    }

    public void register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
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
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
