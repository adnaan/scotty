package com.scotty.lib;

import javax.inject.Singleton;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

public class ScottyApplication extends Application {

	private ObjectGraph objectGraph;

	@Override
	public void onCreate() {
		super.onCreate();
		objectGraph = ObjectGraph.create(new SyncModule(this));
	}

	public void inject(Object object) {
		objectGraph.inject(object);
	}

	@Module(injects = { SyncableContentProvider.class,
			SyncableM2MManager.class, SyncTaskQueue.class,
			SyncTaskService.class, SyncableContentItem.class })
	public class SyncModule {
		private final Context appContext;

		SyncModule(Context appContext) {
			this.appContext = appContext;
		}

		@Provides
		@Singleton
		SyncTaskQueue provideTaskQueue(Gson gson, Bus bus) {
			return SyncTaskQueue.create(appContext, gson, bus);
		}

		@Provides
		@Singleton
		Bus provideBus() {
			return new Bus();
		}

		@Provides
		@Singleton
		Gson provideGson() {
			return new GsonBuilder().create();
		}

	}

}
