package com.scotty.lib;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;

public class SyncTaskQueue extends TaskQueue<SyncTask> {
	private final Context context;

	public SyncTaskQueue(ObjectQueue<SyncTask> delegate, Context context,
			Bus bus) {
		super(delegate);
		this.context = context;
		bus.register(this);

		if (size() > 0)
			startService();
	}

	private void startService() {
		context.startService(new Intent(context, SyncTaskService.class));
	}

	public static SyncTaskQueue create(Context context, Gson gson, Bus bus) {
		return null;
	}
}
