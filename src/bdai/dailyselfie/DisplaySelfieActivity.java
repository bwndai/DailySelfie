package bdai.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class DisplaySelfieActivity extends Activity{
	private ImageView mImageView;
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfie_view);				
		mImageView = (ImageView)findViewById(R.id.selfie_pic);
		
		String imagePath = getIntent().getStringExtra("path");
		Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
		mImageView.setImageBitmap(myBitmap);
	}
}
