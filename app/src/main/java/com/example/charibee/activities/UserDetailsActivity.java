package com.example.charibee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.charibee.data.Data;
import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String TAG = "UserDetailsActivity";

    User user;

    // ui views
    private ImageView ivAvatar;
    private ImageView ivAdmin;
    private TextView tvFullName;
    private TextView tvUsername;
    private TextView tvBio;
    private ListView lvInterests;
    private Button btnMsg;

    // adapter for interests
    ArrayAdapter<String> interestsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // unwrap
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));

        // bind views
        ivAvatar = findViewById(R.id.user_details_avatar_iv);
        ivAdmin = findViewById(R.id.user_details_admin_iv);
        tvFullName = findViewById(R.id.user_details_fullname);
        tvUsername = findViewById(R.id.user_details_username);
        tvBio = findViewById(R.id.user_details_bio);
        lvInterests = findViewById(R.id.user_details_interests);
        btnMsg = findViewById(R.id.user_details_msg_btn);

        // load values into views
        setValues();

        // admin
        if (ParseUser.getCurrentUser().get("role").equals("Organizer")) {
            btnMsg.setBackgroundColor(getResources().getColor(R.color.dark_red));
        }

        if (user.getId().equals(ParseUser.getCurrentUser().getObjectId())) {
            btnMsg.setVisibility(View.GONE);
        }
        btnMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.setToUser(user);
                goChatActivity();
            }
        });
    }

    // go to chat activity
    private void goChatActivity() {
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }

    // loads values into views
    private void setValues() {
        String fullname = (String) user.getName();
        String username = user.getUsername();
        String bio = user.getBio();

        // set admin
        if (user.getRole().equals("Organizer")) {
            TextView tvInterests = findViewById(R.id.user_details_interets_tv);
            ivAdmin.setImageResource(R.drawable.ic_admin);
            tvInterests.setVisibility(View.GONE);
            lvInterests.setVisibility(View.GONE);
        } else {
            ivAdmin.setVisibility(View.GONE);
        }

        // set profile pic
        ParseFile profilePic = (ParseFile) user.getProfilePic();
        if (profilePic != null) {
            Glide.with(this)
                    .load(httpToHttps(profilePic.getUrl()))
                    .circleCrop()
                    .into(ivAvatar);
        } else {
            Log.e(TAG, "couldn't load profile pic");
        }

        // set text
        tvFullName.setText(fullname);
        tvUsername.setText("@" + username);
        tvBio.setText(bio);

        // set interests
        interestsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, user.getStringInterests());
        lvInterests.setAdapter(interestsAdapter);
    }

    // converts http link to https
    private String httpToHttps(String url) {
        if (url == null) {
            return "";
        }

        if (url.contains("https")) {
            return url;
        }

        String httpsUrl = "https" + url.substring(4);
        return httpsUrl;
    }
}