package com.scotty.lib;

import javax.inject.Inject;

import org.json.JSONObject;

import android.database.Cursor;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import edu.mit.mobile.android.content.ContentItem;
import edu.mit.mobile.android.content.column.BooleanColumn;
import edu.mit.mobile.android.content.column.DBColumn;
import edu.mit.mobile.android.content.column.DatetimeColumn;
import edu.mit.mobile.android.content.column.TextColumn;

public abstract class SyncableContentItem implements ContentItem {
	@Inject
	Bus bus;
	private final String uuid;

	public SyncableContentItem() {
		uuid = generateUUID();
	}

	String generateUUID() {
		return "";
	}

	public String getUUID() {
		return uuid;
	}

	/**
	 * ObjectId : A UUID for the item, created when the item is first saved
	 * (either on the client or on the server). This will help prevent
	 * accidentally creating duplicates even if there's an error in network
	 * communication. It is also used to update an item locally when the item is
	 * changed on the server.
	 */
	@DBColumn(type = TextColumn.class, unique = true, notnull = true)
	public static final String OBJECTID = "object_id";

	/**
	 * The time that the item was last modified, in local time. This should be
	 * updated each time the item is modified locally.
	 */
	@DBColumn(type = DatetimeColumn.class, notnull = true, defaultValue = DatetimeColumn.NOW_IN_MILLISECONDS)
	public static final String LOCAL_UPDATED_DATE = "local_updated_at";

	/**
	 * The date that the server last returned as the item's modified date. This
	 * is used to determine if the item has been modified at all on the server
	 * side. This value is relative to the server's clock and may need to be
	 * adjusted for the local clock skew.
	 */
	@DBColumn(type = DatetimeColumn.class)
	public static final String UPDATED_DATE = "updated_at";

	/**
	 * The local date that the item has been created. This is auto-set to the
	 * time of insertion.
	 */
	@DBColumn(type = DatetimeColumn.class, notnull = true, defaultValue = DatetimeColumn.NOW_IN_MILLISECONDS)
	public static final String LOCAL_CREATED_DATE = "local_created_at";

	/**
	 * The server date that the item has been created. This is set after
	 * response from server of item insertion.
	 */
	@DBColumn(type = DatetimeColumn.class, notnull = true, defaultValue = DatetimeColumn.NOW_IN_MILLISECONDS)
	public static final String CREATED_DATE = "created_at";

	/**
	 * <p>
	 * Set this to true to prevent the {@link SyncEngine} from propagating
	 * changes to the server. Items marked draft will be ignored until this
	 * column is false.
	 * </p>
	 * 
	 * <p>
	 * 1 is true; 0 or {@code null} is false
	 * </p>
	 * 
	 * @see #isDraft()
	 * @see #isDraft(Cursor)
	 */
	@DBColumn(type = BooleanColumn.class)
	public static final String DRAFT = "draft";

	/**
	 * <p>
	 * Set this to true to mark that the item has been deleted locally by the
	 * user. Once the {@link SyncEngine} processes the item, it will take care
	 * of actually deleting the content both remotely and locally.
	 * </p>
	 * <p>
	 * 1 is true; 0 or {@code null} is false
	 * </p>
	 * 
	 * @see #isDeleted()
	 * @see #isDeleted(Cursor)
	 */
	@DBColumn(type = BooleanColumn.class)
	public static final String DELETED = "deleted";

	/**
	 * This should be set to true when an item is locally modified.
	 * {@link SyncableSimpleContentProvider} will do this automatically.
	 */
	@DBColumn(type = BooleanColumn.class)
	public static final String DIRTY = "dirty";

	// fromJson
	// toJson

	public abstract void onGetSuccess(JSONObject jsonObject);

	public abstract void onGetFailure();

	@SuppressWarnings("UnusedDeclaration")
	// Used by event bus.
	@Subscribe
	public void onGetSuccessEvent(Sync.GetSuccessEvent event) {
		if (event.uuid.equalsIgnoreCase(uuid))
			onGetSuccess(event.jsonObject);

	}

	@SuppressWarnings("UnusedDeclaration")
	// Used by event bus.
	@Subscribe
	public void onGetFailureEvent(Sync.GetFailureEvent event) {
		if (event.uuid.equalsIgnoreCase(uuid))
			onGetFailure();
	}

}
