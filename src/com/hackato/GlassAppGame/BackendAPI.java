package com.hackato.GlassAppGame;

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
