package com.scotty.lib;

import org.json.JSONObject;

public class Sync {
	public static String APP_ID_KEY = "X-APP-ID";
	public static String APP_SECRET_KEY = "X-APP-SECRET";
	public static String APP_ID = "";
	public static String APP_SECRET = "";

	public static class GetSuccessEvent {
		public final JSONObject jsonObject;
		public final String uuid;

		public GetSuccessEvent(JSONObject jsonObject, String uuid) {
			this.jsonObject = jsonObject;
			this.uuid = uuid;
		}
	}

	public static class GetFailureEvent {
		public final String uuid;

		public GetFailureEvent(String uuid) {
			this.uuid = uuid;

		}
	}

}
