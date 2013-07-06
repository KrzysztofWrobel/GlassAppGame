package com.hackato.GlassAppGame;

import com.hackato.GlassAppGame.models.ChallengesResponse;
import com.hackato.GlassAppGame.models.StdResponse;

import retrofit.http.GET;

/**
 * Created by alek on 7/6/13.
 */
public interface BackendAPI {
	@GET("/register")
	StdResponse register();

	@GET("/challenges")
	ChallengesResponse challenges();
}
