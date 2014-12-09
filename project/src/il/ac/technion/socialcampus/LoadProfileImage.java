package il.ac.technion.socialcampus;

import il.ac.technion.logic.Objects.BitmapDisplayer;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	Bitmap m_iconBitmap;
	boolean mResizeUserIcon;
	BitmapDisplayer mObject;
	
	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;

	public LoadProfileImage(BitmapDisplayer object, ImageView bmImage, Bitmap iconBitmap, boolean resizeUserIcon) {
		this.bmImage = bmImage;
		this.m_iconBitmap = iconBitmap;
		mResizeUserIcon = resizeUserIcon;
		mObject = object;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		if(mResizeUserIcon){
			// by default the profile url gives 50x50 px image only
			// we can replace the value with whatever dimension we want by
			urldisplay = urldisplay.substring(0,urldisplay.length() - 2) + PROFILE_PIC_SIZE;
		}
		Bitmap mIcon = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon;
	}

	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
		m_iconBitmap = result;
		mObject.setBitmap(result);
	}
}
