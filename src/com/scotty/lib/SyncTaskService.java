package com.scotty.lib;

import javax.inject.Inject;

import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.scotty.lib.SyncTask.SyncCallback;
import com.squareup.otto.Bus;

public class SyncTaskService extends Service implements SyncCallback {
	private static final String TAG = "SyncTaskService";

	@Inject
	SyncTaskQueue queue;
	@Inject
	Bus bus;

	private boolean running;

	@Override
	public void onCreate() {
		super.onCreate();
		((ScottyApplication) getApplication()).inject(this);
		// Log.i(TAG, "Service starting!");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		executeNext();
		return START_STICKY;
	}

	private void executeNext() {
		if (running)
			return; // Only one task at a time.

		SyncTask task = queue.peek();
		if (task != null) {
			running = true;
			task.execute(this);
		} else {
			Log.i(TAG, "Service stopping!");
			stopSelf(); // No more tasks are present. Stop.
		}
	}

	@Override
	public void onGetSuccess(JSONObject jsonObject, String uuid) {
		running = false;
		queue.remove();
		bus.post(new Sync.GetSuccessEvent(jsonObject, uuid));
		executeNext();
	}

	@Override
	public void onGetFailure(String uuid) {
		bus.post(new Sync.GetFailureEvent(uuid));

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
