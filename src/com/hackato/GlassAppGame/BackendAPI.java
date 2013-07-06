package com.hackato.GlassAppGame;

import com.hackato.GlassAppGame.models.ChallengesResponse;
import com.hackato.GlassAppGame.models.RewardResponse;
import com.hackato.GlassAppGame.models.StdResponse;

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
