package com.github.krzysztofwrobel.glassappgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.R;

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

        Intent intent = getIntent();
        //TODO obiekt reward ustawić wartości na polach ekranu;

    }
}
