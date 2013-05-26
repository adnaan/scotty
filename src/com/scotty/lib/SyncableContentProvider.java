package com.scotty.lib;

import java.util.HashMap;

import android.content.ContentValues;
import android.net.Uri;
import edu.mit.mobile.android.content.SimpleContentProvider;

public class SyncableContentProvider extends SimpleContentProvider {

	public SyncableContentProvider(String authority, String dbName,
			int dbVersion, String baseApiPath) {
		super(authority, dbName, dbVersion);

	}

	public int update(HashMap<String, String> data) {

		return 0;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int changed = super.update(uri, values, selection, selectionArgs);
		// queue update task
		return changed;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri insertedUri = super.insert(uri, values);
		// queue insert task
		return insertedUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = super.delete(uri, selection, selectionArgs);
		// queue delete task
		return count;
	}
}
