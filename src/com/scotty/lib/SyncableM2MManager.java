package com.scotty.lib;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import edu.mit.mobile.android.content.ContentItem;
import edu.mit.mobile.android.content.m2m.M2MManager;

public class SyncableM2MManager extends M2MManager {

	public SyncableM2MManager(Class<? extends ContentItem> to) {
		super(to);

	}

	@Override
	public Uri insert(ContentResolver cr, Uri parent, ContentValues cv) {
		Uri itemUri = super.insert(cr, parent, cv);
		// queue to insert task
		return itemUri;
	}

}
