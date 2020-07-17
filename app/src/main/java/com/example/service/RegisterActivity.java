package com.example.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";

    // ui views
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    // add spinner later
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // binds ui elements
        etFirstName = findViewById(R.id.register_first_name);
        etLastName = findViewById(R.id.register_last_name);
        etEmail = findViewById(R.id.register_email);
        etPassword = findViewById(R.id.register_password);
        etPasswordConfirm = findViewById(R.id.register_password_confirm);
        btnRegister = findViewById(R.id.register_submit_btn);

        // register new user
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                if (validRegisterUser(firstName, lastName, email, password, passwordConfirm)) {
                    registerUser(firstName, lastName, email, password);
                }
            }
        });
    }

    // checks if fields are valid to register
    private boolean validRegisterUser(String firstName, String lastName, String email, String password, String passwordConfirm) {
        if (firstName.length() == 0 || lastName.length() == 0 || email.length() == 0 || password.length() == 0 || passwordConfirm.length() == 0) {
            makeMessage("Please make sure all fields are filled out.");
            return false;
        } else if (!isEmail(email)) {
            makeMessage("Please enter a valid email.");
        } else if (!password.equals(passwordConfirm)) {
            makeMessage("Please make sure your passwords match.");
            return false;
        } else if (password.length() < 6) {
            makeMessage("Your password must be at least 6 characters long.");
            return false;
        }
        return true;
    }

    // registers a new user
    private void registerUser(final String firstName, final String lastName, String email, String password) {
        ParseUser user = new ParseUser();
        user.setUsername("");
        user.setPassword(password);
        user.setEmail(email);

        user.put("firstName", firstName);
        user.put("lastName", lastName);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    goMainActivity();
                } else {
                    Log.e(TAG, String.valueOf(e));
                    makeMessage("There was a problem with your registration. Please try again.");
                }
            }
        });
    }

    // is an email
    private static boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    // goes to main activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // shows user a message
    private void makeMessage(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}