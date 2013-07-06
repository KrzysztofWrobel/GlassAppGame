package com.hackato.GlassAppGame;

import retrofit.http.GET;

/**
 * Created by alek on 7/6/13.
 */
public interface BackendAPI {
	@GET("/register")
    com.hackato.GlassAppGame.models.StdResponse register();

	@GET("/challenges")
    com.hackato.GlassAppGame.models.ChallengesResponse challenges();
}
