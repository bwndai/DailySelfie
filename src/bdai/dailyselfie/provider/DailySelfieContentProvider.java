package bdai.dailyselfie.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DailySelfieContentProvider extends ContentProvider {

	private DatabaseHelper mDbHelper;
	
	@Override
	public boolean onCreate() {
		mDbHelper = new DatabaseHelper(getContext());
		return (mDbHelper != null);
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		//no deletion needed
		return 0;

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = mDbHelper.getWritableDatabase().insert(
				DailySelfieContract.SELFIES_TABLE_NAME, "", values);
		if (rowID > 0) {
			Uri fullUri = ContentUris.withAppendedId(
					DailySelfieContract.SELFIES_URI, rowID);
			getContext().getContentResolver().notifyChange(fullUri, null);
			return fullUri;
		}
		throw new SQLException("Failed to add record into" + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(DailySelfieContract.SELFIES_TABLE_NAME);

		Cursor cursor = qb.query(mDbHelper.getWritableDatabase(), projection, selection,
				selectionArgs, null, null, sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;

	}

	@Override
	public String getType(Uri arg0) {
		// Not Implemented
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// unimplemented
		return 0;
	}

}