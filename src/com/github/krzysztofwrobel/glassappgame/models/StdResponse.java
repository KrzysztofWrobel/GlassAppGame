package com.github.krzysztofwrobel.glassappgame.models;

/**
 * Created by alek on 7/6/13.
 */
public class StdResponse {
	private String status;
	private String error;

	public boolean isOk() {
		return "ok".equals(status);
	}

	public boolean isError() {
		return "error".equals(status);
	}

	public String getErrorMsg() {
		return error;
	}
}
