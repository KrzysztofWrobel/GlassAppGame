package com.github.krzysztofwrobel.glassappgame.models;

import com.github.krzysztofwrobel.glassappgame.models.Reward;
import com.github.krzysztofwrobel.glassappgame.models.StdResponse;

import java.util.ArrayList;

/**
 * Created by alek on 7/6/13.
 */
public class RewardResponse extends StdResponse {
	private Reward reward;

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	@Override
	public String toString() {
		return "RewardResponse{" +
				"reward=" + reward +
				'}';
	}
}
