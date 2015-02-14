package bdai.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bdai.dailyselfie.provider.DailySelfieContract;

public class SelfiesAdapter extends CursorAdapter{
	private static final String APP_DIR = "DailySelfie/Selfies";
	private ArrayList<Selfie> mSelfies = new ArrayList<Selfie>();
	private Context mContext;
	static public String mBitmapStoragePath;
	
	private static LayoutInflater sLayoutInflater = null;

	public SelfiesAdapter(Context context) {
		super(context,null,0);
		mContext = context;
		sLayoutInflater = LayoutInflater.from(mContext);

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				String root = mContext.getExternalFilesDir(null)
						.getCanonicalPath();
				if (null != root) {
					File bitmapStorageDir = new File(root, APP_DIR);
					bitmapStorageDir.mkdirs();
					mBitmapStoragePath = bitmapStorageDir.getCanonicalPath();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public Cursor swapCursor(Cursor newCursor) {

		super.swapCursor(newCursor);
		if (null != newCursor){
			mSelfies.clear();
			if (newCursor.moveToFirst()){
				do{
					mSelfies.add(getSelfieFromCursor(newCursor));
					
				}while(newCursor.moveToNext());
			}
		}
		
		return newCursor;
	}
	private Selfie getSelfieFromCursor(Cursor cursor) {

		String selfieName = cursor.getString(cursor
				.getColumnIndex(DailySelfieContract.SELFIE_NAME));
		String selfiePath = cursor.getString(cursor
				.getColumnIndex(DailySelfieContract.SELFIE_PATH));
		String selfieThumbPath = cursor.getString(cursor
				.getColumnIndex(DailySelfieContract.SELFIE_THUMBNAIL));
		return new Selfie(selfieName, selfiePath, selfieThumbPath);

	}

	public Object getItem(int position) {
		return mSelfies.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageView selfie;
		TextView name;
	}

	public void add(Selfie selfie) {

		mSelfies.add(selfie);
		ContentValues values = new ContentValues();
		
		values.put(DailySelfieContract.SELFIE_NAME, selfie.getName());
		values.put(DailySelfieContract.SELFIE_PATH, selfie.getPath());
		values.put(DailySelfieContract.SELFIE_THUMBNAIL, selfie.getThumbPath());
		mContext.getContentResolver().insert(DailySelfieContract.SELFIES_URI, values);
		mContext.getContentResolver().notifyChange(DailySelfieContract.SELFIES_URI, null);        
	}

	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ViewHolder holder = (ViewHolder) view.getTag();
		String thumpath = cursor.getString(cursor.getColumnIndex(DailySelfieContract.SELFIE_THUMBNAIL));
		Bitmap pic = getBitmapFromFile(thumpath);
		holder.selfie.setImageBitmap(pic);
		String name = cursor.getString(cursor.getColumnIndex(DailySelfieContract.SELFIE_NAME));
		holder.name.setText(name.substring(5, name.length() - 4));
		Log.i("Selfies Adapter Thumb Path",thumpath);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		//inflate the view and return it
		View newView;
		ViewHolder holder = new ViewHolder();

		newView = sLayoutInflater.inflate(R.layout.selfie_item_view, parent,
				false);
		holder.selfie = (ImageView) newView.findViewById(R.id.selfie_pic);
		holder.name = (TextView) newView.findViewById(R.id.selfie_name);

		newView.setTag(holder);

		return newView;
	}
	
	private Bitmap getBitmapFromFile(String filePath) {
		return BitmapFactory.decodeFile(filePath);
	}
	

}
