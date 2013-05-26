package com.scotty.lib;

import org.json.JSONObject;

import com.squareup.tape.Task;

public abstract class SyncTask implements Task<SyncTask.SyncCallback> {

	/**
	 * 
	 */
	public static final long serialVersionUID = -6809187955497236944L;

	public final String APP_ID_KEY = "APP-ID";
	public final String API_KEY_KEY = "APP_KEY";
	private final String uuid;

	SyncTask(final String uuid) {
		this.uuid = uuid;
	}

	public interface SyncCallback {
		void onGetSuccess(JSONObject jsonObject, String uuid);

		void onGetFailure(String uuid);

	}

	public String getUUID() {
		return uuid;
	}

}
