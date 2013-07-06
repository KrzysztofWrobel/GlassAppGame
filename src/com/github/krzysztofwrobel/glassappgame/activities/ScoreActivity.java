package com.github.krzysztofwrobel.glassappgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.models.Reward;
import com.squareup.picasso.Picasso;

/**
 * Created by Krzysztof on 06.07.13.
 */
public class ScoreActivity extends BaseActivity {
    private TextView scoreTitleTextView;
    private TextView rewardDescriptionTextView;
    private ImageView rewardImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_activity);

        scoreTitleTextView = (TextView) findViewById(R.id.tv_score_title);
        rewardDescriptionTextView = (TextView) findViewById(R.id.tv_reward_description);
        rewardImageView = (ImageView) findViewById(R.id.iv_reward_icon);

        Reward receivedReward = getIntent().getParcelableExtra("reward");
        if(receivedReward!=null){
            scoreTitleTextView.setText("You've got a Reward!");
            rewardDescriptionTextView.setText(receivedReward.getDescription());
            Picasso.with(this).load(receivedReward.getImage_link()).resize(640, 320).into(rewardImageView);
        }

    }
}
