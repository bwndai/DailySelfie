package bdai.dailyselfie.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Selfies";
	private static final String CREATE_SELFIE_TABLE = " CREATE TABLE "
			+ DailySelfieContract.SELFIES_TABLE_NAME + " ("
			+ DailySelfieContract.SELFIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DailySelfieContract.SELFIE_NAME + " TEXT NOT NULL, "
			+ DailySelfieContract.SELFIE_PATH + " TEXT NOT NULL, "
			+ DailySelfieContract.SELFIE_THUMBNAIL + " TEXT NOT NULL) ";


	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_SELFIE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "
				+ DailySelfieContract.SELFIES_TABLE_NAME);
		onCreate(db);
	}

}