package com.github.krzysztofwrobel.glassappgame;

import com.github.krzysztofwrobel.glassappgame.models.ChallengesResponse;
import com.github.krzysztofwrobel.glassappgame.models.RewardResponse;
import com.github.krzysztofwrobel.glassappgame.models.StdResponse;

import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by alek on 7/6/13.
 */
public interface BackendAPI {
	@GET("/register")
	StdResponse register();

	@GET("/challenges")
	ChallengesResponse challenges();

	@PUT("/challenges/complete/{login}/{challenge_id}")
	RewardResponse challenges_complete(@Query("login") String login, @Query("challenge_id") String id);
}