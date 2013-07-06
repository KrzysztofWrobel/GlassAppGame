package com.github.krzysztofwrobel.glassappgame;

import com.github.krzysztofwrobel.glassappgame.models.ChallengesResponse;
import com.github.krzysztofwrobel.glassappgame.models.RewardResponse;
import com.github.krzysztofwrobel.glassappgame.models.StdResponse;

import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by alek on 7/6/13.
 */
public interface BackendAPI {
	@GET("/register")
	StdResponse register();

	@GET("/challenges/{login}")
	ChallengesResponse challenges(@Path("login") String login);

	@PUT("/challenges/complete/{login}/{challenge_id}")
	RewardResponse challenges_complete(@Path("login") String login, @Path("challenge_id") String id);
}
