package com.hackato.GlassAppGame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hackato.GlassAppGame.R;
import com.hackato.GlassAppGame.activities.HomeActivity;
import com.hackato.GlassAppGame.models.Challenge;

/**
 * Created by Krzysztof on 06.07.13.
 */
public class DescriptionActivity extends HomeActivity {
    private Challenge challenge;
    private TextView descriptionTitleTextView;
    private TextView descriptionFullTextView;
    private TextView friendsAchivmentsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);
        descriptionTitleTextView = (TextView) findViewById(R.id.tv_decription_title);
        descriptionFullTextView = (TextView) findViewById(R.id.tv_decription_full);
        friendsAchivmentsTextView = (TextView) findViewById(R.id.tv_friend_achivments);

        Intent intent = getIntent();
        challenge = intent.getParcelableExtra("challenge");
        if(challenge != null){
            descriptionTitleTextView.setText(challenge.getTitle());
            descriptionFullTextView.setText(challenge.getDescription());

        }
    }
}
