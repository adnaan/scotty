package com.scotty.lib;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTask extends SyncTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1286146215845467910L;
	String url;
	HttpResponse httpResponse;
	private final String uuid;

	public GetTask(String url, String uuid) {
		super(uuid);
		this.uuid = uuid;
		this.url = url;
	}

	@Override
	public void execute(SyncCallback callback) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			// auth
			httpGet.addHeader(Sync.APP_ID_KEY, Sync.APP_ID);
			httpGet.addHeader(Sync.APP_SECRET_KEY, Sync.APP_SECRET);

			httpResponse = httpclient.execute(httpGet);

			if (isSuccess())
				callback.onGetSuccess(getJsonObject(), uuid);
			else
				callback.onGetFailure(uuid);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	boolean isSuccess() {
		int statusCode = httpResponse.getStatusLine().getStatusCode();

		if ((httpResponse.getEntity() == null)
				|| ((statusCode < 200) || (statusCode >= 300))
				|| (getJsonObject() == null))
			return false;

		return true;
	}

	public JSONObject getJsonObject() {
		try {
			return new JSONObject(
					EntityUtils.toString(httpResponse.getEntity()));
		} catch (org.apache.http.ParseException e) {
			return null;
		} catch (JSONException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}
