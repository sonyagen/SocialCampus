package il.ac.technion.socialcampus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;

public class LocationServices {

	 Context mContext;
	 LocationListener mLocationListner;
	 
	public LocationServices(Context context,LocationListener locationListner ){
		
		mContext = context;
		mLocationListner = locationListner;
	}
//	GoogleMap getMap(){}
	
	Location getCurrentLocation(Context context, LocationServiceListener mCallback){ 
		mContext = context;
		final LocationManager locationManager  = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		Criteria c = new Criteria();
		c.setBearingAccuracy(Criteria.ACCURACY_HIGH);
		String provider = locationManager.getBestProvider(c, false);
		//TODO:Criteria best practice
		
		if (!locationManager.isProviderEnabled(provider)){
			//requestProviderEnabling(mContext);
			mCallback.onProviderDisabled();
			return null;
		}
		
		locationManager.requestLocationUpdates(provider, 3000, 100, mLocationListner);
		return locationManager.getLastKnownLocation(provider);
	}
	
	void requestProviderEnabling(Context context,final LocationServiceListener mCallback){
		//new AlertDialog(mContext).show();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Location disabled")
		.setMessage("Please enable your location")
		.setCancelable(false);
		
		builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				  mContext.startActivity(intent);
			}
		  })
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
				mCallback.onAlertDialogCancle();
			}
		});
		
		builder.create().show();
	}
	
	public interface LocationServiceListener{
		public void onAlertDialogCancle();
		public void onProviderDisabled();
	}



}
