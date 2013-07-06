package com.github.krzysztofwrobel.glassappgame;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.krzysztofwrobel.glassappgame.models.ChallengesResponse;
import com.github.krzysztofwrobel.glassappgame.models.RewardResponse;
import com.github.krzysztofwrobel.glassappgame.models.StdResponse;

/**
 * Created by alek on 7/6/13.
 */
public class NetworkService extends IntentService {
	public static final String ACTION_REGISTER = "ACTION_REGISTER";
	public static final String ACTION_CHALLENGES = "ACTION_CHALLENGES";
	public static final String ACTION_CHALLENGE_COMPLETE = "ACTION_CHALLENGE_COMPLETE";

	private static final String TAG = "NetworkService";
	private static final int SO_TIMEOUT = 4000;
	private static final int CONNECT_TIMEOUT = 4000;
	private final String API_URL = "http://glass.pelotaspl.us:8080/";
	private final LocalBroadcastManager mLocalBroadcastManager;
	private BackendAPI service;

	// TODO get from settings
	private String userLogin;

	public void setUserLogin() {
		AccountManager am = AccountManager.get(this);
		if (am == null) {
			Log.e(TAG, "am is null");
			userLogin = "pelotasplus@gmail.com";
			return;
		}

		Account[] accounts = am.getAccounts();

	    for (Account account : accounts) 	    {
	        if(account.name.endsWith("gmail.com")) {
	        	userLogin = account.name;
				break;
	        }
	    }

	    if(userLogin == null)
	    	userLogin = Long.toString(System.currentTimeMillis());
	}

	public NetworkService() {
		super("NetworkService");

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer(API_URL)
				.setClient(new ApacheClient(client))
				.setDebug(true)
				.build();
		service = restAdapter.create(BackendAPI.class);

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		setUserLogin();

		String action = intent.getAction();
		Bundle params = intent.getBundleExtra("params");
		if (params == null)
			params = new Bundle();

		try {
			if (ACTION_REGISTER.equals(action)) {
				StdResponse resp = service.register();

				Intent in = new Intent();
				in.setAction(action);
				in.putExtra("error", resp.isError());
				mLocalBroadcastManager.sendBroadcast(in);
			} else if (ACTION_CHALLENGES.equals(action)) {
				ChallengesResponse challenges = service.challenges(userLogin);

				Intent in = new Intent();
				in.setAction(action);
				in.putExtra("challenges", challenges.getChallenges());
				in.putExtra("error", challenges.isError());
				mLocalBroadcastManager.sendBroadcast(in);
			} else if (ACTION_CHALLENGE_COMPLETE.equals(action)) {
				String challenge_id = params.getString("id", "default-random-id");

				RewardResponse rewardResponse = service.challenges_complete(userLogin, challenge_id);
				Log.d(TAG, "rewardResponse=" + rewardResponse);

				Intent in = new Intent();
				in.setAction(action);
				in.putExtra("reward", rewardResponse.getReward());
				in.putExtra("error", rewardResponse.isError());
				mLocalBroadcastManager.sendBroadcast(in);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			Intent in = new Intent();
			in.setAction(action);
			in.putExtra("error", true);
			mLocalBroadcastManager.sendBroadcast(in);
		}
	}

	public static void run(Context ctx, String action, Bundle params) {
		Log.d(TAG, "run=" + action);
		Intent in = new Intent(ctx, NetworkService.class);
		in.setAction(action);

		if (params == null) {
			params = new Bundle();
		}
		in.putExtra("params", params);
		ctx.startService(in);
	}
}
