package bdai.dailyselfie.provider;

import android.net.Uri;

public class DailySelfieContract {

	public static final String AUTHORITY = "bdai.dailyselfie.provider";
	public static final Uri BASE_URI = Uri
			.parse("content://" + AUTHORITY + "/");

	public static final String SELFIES_TABLE_NAME = "selfies";

	// The URI for this table.
	public static final Uri SELFIES_URI = Uri.withAppendedPath(BASE_URI,
			SELFIES_TABLE_NAME);

	public static final String SELFIE_ID = "_id";
	public static final String SELFIE_NAME = "name";
	public static final String SELFIE_PATH = "path";
	public static final String SELFIE_THUMBNAIL = "thumbnail";
	
	
}
