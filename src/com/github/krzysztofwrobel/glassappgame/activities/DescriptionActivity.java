package com.github.krzysztofwrobel.glassappgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.models.Challenge;
import com.squareup.picasso.Picasso;

/**
 * Created by Krzysztof on 06.07.13.
 */
public class DescriptionActivity extends BaseActivity {
    private Challenge challenge;
    private TextView descriptionTitleTextView;
    private TextView descriptionFullTextView;
    private TextView pointsTextView;
    private ImageView challangeImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);
        descriptionTitleTextView = (TextView) findViewById(R.id.tv_decription_title);
        descriptionFullTextView = (TextView) findViewById(R.id.tv_decription_full);
        pointsTextView = (TextView) findViewById(R.id.tv_points);
        challangeImageView = (ImageView) findViewById(R.id.iv_description_image);
        
        Intent intent = getIntent();
        challenge = intent.getParcelableExtra("challenge");
        if(challenge != null){
            descriptionTitleTextView.setText(challenge.getTitle());
            descriptionFullTextView.setText(challenge.getDescription());
            Picasso.with(this).load(challenge.getImage_link()).resize(640, 320).into(challangeImageView);

            pointsTextView.setText(getString(R.string.points_for_challenge, challenge.getPoints()));

        }
    }
}
