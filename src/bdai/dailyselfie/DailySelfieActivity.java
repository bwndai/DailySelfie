package bdai.dailyselfie;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.widget.ListView;
import java.util.Date;
import bdai.dailyselfie.provider.DailySelfieContract;

public class DailySelfieActivity extends ListActivity implements LoaderCallbacks<Cursor>{
	
	private SelfiesAdapter mAdapter;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private AlarmManager mAlarmManager;
	private Intent mReceiverIntent;
	private PendingIntent mReceiverPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 60 * 1000L;
	private String selfiePath;
	private String selfieName;
	//protected static final long JITTER = 60L;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new SelfiesAdapter(this);
		setListAdapter(mAdapter);
		//set up alarm
		mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		mReceiverIntent = new Intent(this, AlarmReceiver.class);
		mReceiverPendingIntent = PendingIntent.getBroadcast(DailySelfieActivity.this, 
				0, mReceiverIntent, 0);
		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
				SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
				2 * 60 * 1000, mReceiverPendingIntent);
		// 60 * 1000 should be change to a larger number. 
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_daily_selfie, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// take a selfie, I also found part of code online about this part.
		int id = item.getItemId();
		if(id == R.id.action_take_selfie){
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
		    	File photoFile = null;
		        try {
		        	photoFile = createImageFile();
		        	selfiePath = photoFile.getAbsolutePath();
		        	selfieName = photoFile.getName();
		        } catch (IOException ex) {}
		        if (null != photoFile) {
		            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		                    Uri.fromFile(photoFile));
		            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		        }	
		    }
		    return true;
		}
		return super.onOptionsItemSelected(item);
	}
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//Show the clicked selfie
		super.onListItemClick(l, v, position, id);
		Selfie selfie = (Selfie) mAdapter.getItem(position);
		Intent intent = new Intent(this,DisplaySelfieActivity.class);
		intent.putExtra("path",selfie.getPath());
		startActivity(intent);
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) { 
        	
            String thumPath = selfiePath.replace(".jpg", "_thumbnail.jpg");
            //Bundle extras = data.getExtras();
        	//Bitmap selfiePic = (Bitmap)extras.get(MediaStore.EXTRA_OUTPUT);
            Bitmap selfiePic = BitmapFactory.decodeFile(selfiePath);
            //args, bitmap, width, height, filter
            Bitmap selfieThumnail = Bitmap.createScaledBitmap(selfiePic, 150, 
            		(int)150 * selfiePic.getHeight() / selfiePic.getWidth() , false);

            storeBitmapToFile(selfieThumnail,thumPath);
            Selfie selfie = new Selfie(selfieName, selfiePath, thumPath);
            
            Log.i("Main activity thum path", thumPath);
            Log.i("Main activity pic path", selfiePath);
            
            mAdapter.add(selfie);
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED){
        	// delete the pic.
        	// pic is already generated and stored in the hardware
        	File file = new File (selfiePath);
        	boolean deleted = file.delete();
        	if (deleted)
        		Log.i("Main activity delete Selfie", "Sucess");
        	else Log.i("Main activity delete Selfie", "Fail");
        }
    }
	
	
	
	private File createImageFile() throws IOException {
	    // Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp;
	    File image = new File(SelfiesAdapter.mBitmapStoragePath + "/" + imageFileName + ".jpg");
	    return image;
	
	}
	
	private boolean storeBitmapToFile(Bitmap bitmap, String filePath) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			try {

				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(filePath));
				bitmap.compress(CompressFormat.PNG, 100, bos);
				bos.flush();
				bos.close();
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
			return true;
		}
		return false;
	}

	// Called when a new Loader should be created
	// Returns a new CursorLoader

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		return new CursorLoader(this, DailySelfieContract.SELFIES_URI, null,
				null, null, null);
	}

	// Called when the Loader has finished loading its data
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		// Swap the new cursor into the List adapter
		mAdapter.swapCursor(data);

	}

	// Called when the last Cursor provided to onLoadFinished()
	// is about to be closed

	public void onLoaderReset(Loader<Cursor> loader) {

		// set List adapter's cursor to null
		mAdapter.swapCursor(null);
	}
}
