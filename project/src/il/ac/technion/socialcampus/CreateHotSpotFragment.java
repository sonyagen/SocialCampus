package il.ac.technion.socialcampus;

import il.ac.technion.logic.LocationFactury;
import il.ac.technion.logic.TimeDateStringFactory;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.HotSpot;
import il.ac.technion.logic.ServerCommunication.ServerRequestManager;

import java.io.FileNotFoundException;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link CreateHotSpotFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link CreateHotSpotFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class CreateHotSpotFragment extends Fragment {
	
	final private String START_TIME_PICKER_HEDLINE = "Choose Start Time";
	final private String END_TIME_PICKER_HEDLINE = "Choose End Time";
	final private String START_DATE_PICKER_HEDLINE = "Choose Start Date";
	final private String END_DATE_PICKER_HEDLINE = "Choose End Date";
	
	private boolean imageChangedByUser = false;
	private boolean anImageIsSet = false;
	private Long mCurrHotSpotId;
	private HotSpot mCurrHotSpotData;
	private GoogleMap mMap;
	private EditText headline;
	private ImageView imageView;
	private TextView timeInput;
	private TextView dateInput;
	private TextView endTimeInput;
	private TextView endDateInput;
	private EditText place;
	private EditText description;
	private ImageView userImage1;
	private ImageView userImage2;
	private ImageView userImage3;
	private boolean isEdit;
	public static Long defHotSpotID = -1L;
	private Bitmap bitmap;
	private Uri imageFromGalaryUri;
	private CameraPosition chosenPos;
	
  	public static CreateHotSpotFragment newInstance(Long hsid) {
  		if(hsid==null){
  			hsid=defHotSpotID;
  		}
		CreateHotSpotFragment fragment = new CreateHotSpotFragment();
		Bundle args = new Bundle();
		args.putLong("id", hsid);
		fragment.setArguments(args);
		return fragment;
	}

	public CreateHotSpotFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			
			mCurrHotSpotId = getArguments().getLong("id");
			mCurrHotSpotData = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mCurrHotSpotId);
			if(mCurrHotSpotData == null || mCurrHotSpotId == defHotSpotID ) {
				isEdit = false;
			}
			else{ 
				isEdit = true;
			}
			setHasOptionsMenu(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_create_hot_spot, container, false);
		
		//text views
		headline = (EditText)v.findViewById(R.id.name);
		imageView = (ImageView)v.findViewById(R.id.image);
		timeInput = (TextView)v.findViewById(R.id.timeStr);
		dateInput = (TextView)v.findViewById(R.id.dateeStr);
		endTimeInput = (TextView)v.findViewById(R.id.endtimeStr);
		endDateInput = (TextView)v.findViewById(R.id.enddateeStr);
		place = (EditText)v.findViewById(R.id.place);
		description = (EditText)v.findViewById(R.id.desc);
		
		//going user pics
		userImage1 = (ImageView)v.findViewById(R.id.usr1);
		userImage2 = (ImageView)v.findViewById(R.id.usr2);
		userImage3 = (ImageView)v.findViewById(R.id.usr3);
		
		//init view
		initTextFilds();
		initImage();
		initMap();
		initGoing();
		setListeners();
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.create_new_hot_spot, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
	    	case R.id.ok:
	    		
	    		if (!validateInput()) return true;
	    		
	    		final HotSpot fromView = createHotSpotFromView();
	    		
	    		//if URL WAS CHANED
	    		if(imageChangedByUser){
	    			ServerRequestManager.INSTANCE.uploadImageToServer(fromView,imageFromGalaryUri,new UiOnDone() {
	    				
	    				@Override public void execute() {
	    					addOrEditTheEvent(fromView);
	    				}
	    			},new UiOnError(getActivity()));

	    		}else{
	    			addOrEditTheEvent(fromView);
	    		}
				return true;
		      
	    	case R.id.cancel:
		    	  setReturnAndFinish(-1L);
		    	  return true;
		      default:
		    	  return super.onOptionsItemSelected(item);
	
	   }
	
	}
	
	private void addOrEditTheEvent(final HotSpot fromView){
		if (isEdit){
			ServerRequestManager.INSTANCE.updateHotSpots(fromView, new UiOnDone() {
				@Override
				public void execute() {
					setReturnAndFinish(fromView.getId());
				}
			}, new UiOnError(getActivity()));
		}else{
			//create new: 
			ServerRequestManager.INSTANCE.addHotSpots(fromView, new UiOnDone() {
				@Override
				public void execute() {
					setReturnAndFinish(fromView.getId());
				}
			}, new UiOnError(getActivity()));
		}
	}

	private boolean validateInput() {
		boolean res = true;
		if(headline.getText().toString().equals("")){
			headline.setError("add headline");
			res = false;
		}
		if(description.getText().toString().equals("  ")){
			description.setError("add description");
			res = false;
		}
		if(place.getText().toString().equals("")){
			place.setError("add place description");
			res = false;
		}
		
		if(!res) return res;
		
		if(!LocationSet){
			res = false;
			Toast.makeText(getActivity(), "tap on map to choose a location", Toast.LENGTH_SHORT).show();
		}
//		else if(startTime.getTimeInMillis()<Calendar.getInstance().getTimeInMillis()){
//			res = false;
//			Toast.makeText(getActivity(), "Please edit time to future time", Toast.LENGTH_SHORT).show();
//		}else if (startTime.getTimeInMillis()>= endTime.getTimeInMillis()){
//			res = false;
//			Toast.makeText(getActivity(), "Please edit event's end time", Toast.LENGTH_SHORT).show();
//		}
		else if(!anImageIsSet){
			res = false;
			Toast.makeText(getActivity(), "tap on the image to change it", Toast.LENGTH_SHORT).show();
		}
		
		return res;
	}

	private void setReturnAndFinish(Long hotSpotId){
		Intent returnIntent = new Intent();
		returnIntent.putExtra("hotSpoId",hotSpotId);
		getActivity().setResult(Activity.RESULT_OK,returnIntent);
		getActivity().finish();
	}
	
	//init fields foe new or edit
	private void initTextFilds(){
		
		if(isEdit){
			startTime.setTimeInMillis(mCurrHotSpotData.getmTime());
			endTime.setTimeInMillis(mCurrHotSpotData.getEndTime());
			
			headline.setText(mCurrHotSpotData.getmName());
			description.setText(mCurrHotSpotData.getmDesc());
			place.setText(mCurrHotSpotData.getmLocation());
		}
							
		timeInput.setText(TimeDateStringFactory.getTimeStr(startTime.getTimeInMillis()));
		dateInput.setText(TimeDateStringFactory.getDateStr(startTime.getTimeInMillis()));
		endTimeInput.setText(TimeDateStringFactory.getTimeStr(endTime.getTimeInMillis()));
		endDateInput.setText(TimeDateStringFactory.getDateStr(endTime.getTimeInMillis()));
		
	}
	
	private void initImage(){
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoGalery();
			}
		});
		
		if(isEdit){
			bitmap = mCurrHotSpotData.getImage();
			if (bitmap!=null) {
				imageView.setImageBitmap(bitmap);
			}else {
				//bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
				HotSpot hs = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mCurrHotSpotId);
				new LoadProfileImage(hs, imageView, bitmap, false).execute(mCurrHotSpotData.getImageURL());
			}
			anImageIsSet = true;
		}
	}

	boolean LocationSet = false;
	private void initMap() {
		SupportMapFragment f = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.My_map));
		mMap = f.getMap();
		
		if(isEdit){
			LatLng ll = new LatLng( mCurrHotSpotData.getLangt(),mCurrHotSpotData.getLongt());
			chosenPos = LocationFactury.buildCameraPosition(ll);
			if (mMap != null) {
	        	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(chosenPos));
	        	mMap.addMarker(new MarkerOptions().position(ll));
	        	LocationSet = true;
	        }
		
		}else{
			chosenPos = LocationFactury.currentPositionOrCornellTeach();;
			if (mMap != null) 
	        	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(chosenPos));
		}
	}
	
	private void initGoing(){
//		if(!isEdit){
//			//set imge me: userImage1.set...
//			UserManager.INSTANCE.getMyData().setUserPhoto(userImage1);
			userImage1.setVisibility(View.GONE);
			userImage2.setVisibility(View.GONE);
			userImage3.setVisibility(View.GONE);
//		}
//		else{
//			//set 3 imges of users
//		}
		
	}
	
	private void setListeners(){
		timeInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {	
				TimePickerDialog tm = new TimePickerDialog(
						(Context)getActivity(), new innerFirstTime(), 
						startTime.get(Calendar.HOUR_OF_DAY),
						startTime.get(Calendar.MINUTE), true);
				tm.setTitle(START_TIME_PICKER_HEDLINE);
				tm.show();
			}
		});
		dateInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {	
				
				DatePickerDialog tm = new DatePickerDialog(
						(Context)getActivity(),  new innerFirstDate(), 
						startTime.get(Calendar.YEAR),
						startTime.get(Calendar.MONTH), 
						startTime.get(Calendar.DAY_OF_MONTH));
				tm.setTitle(START_DATE_PICKER_HEDLINE);
				tm.show();
			}
		});
		endTimeInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {	
				
				TimePickerDialog tm = new TimePickerDialog(
						(Context)getActivity(), new secondInnerTime(), 
						endTime.get(Calendar.HOUR_OF_DAY),
						endTime.get(Calendar.MINUTE), true);
				tm.setTitle(END_TIME_PICKER_HEDLINE);
				tm.show();
			}
		});
		endDateInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {	
				
				DatePickerDialog tm = new DatePickerDialog(
						(Context)getActivity(),  new secondInnerDate(), 
						endTime.get(Calendar.YEAR),
						endTime.get(Calendar.MONTH), 
						endTime.get(Calendar.DAY_OF_MONTH));
				tm.setTitle(END_DATE_PICKER_HEDLINE);
				tm.show();
			}
		});
		mMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				double lat = chosenPos.target.latitude;
				double lng = chosenPos.target.longitude;
				startActivityForResult(new Intent(getActivity(),MapPickerActivity.class).
						putExtra("lat", lat).putExtra("lng", lng),requestCodeMap);
			}
		});
	}

	int requestCodeMap = 100;
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity();
		if (requestCode == requestCodeMap && 
				resultCode == Activity.RESULT_OK) {
			 
			 //reset Camera on mMap 
			 Double lat = data.getExtras().getDouble("lat");
			 Double lng = data.getExtras().getDouble("lng");
			 chosenPos = LocationFactury.buildCameraPosition(lat,lng);
					
			 mMap.clear();
			 mMap.moveCamera(CameraUpdateFactory.newCameraPosition(chosenPos));
			 mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
			 LocationSet = true;
			 
		}
		
		if(requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK){
			//back from galary
			
			imageChangedByUser = true;
			anImageIsSet = true;
			imageFromGalaryUri = data.getData();
//			HotSpot hs = new HotSpot();
//			test(hs, selectedImage);
			try {
				bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageFromGalaryUri));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
            imageView.setImageBitmap(bitmap);
		}
		 
	 }

	//generate obj from view
	//NOTE: event created with previous imgUrl even if it was changed.
	private HotSpot createHotSpotFromView(){
		
		//get data available in View
		Long mTime = startTime.getTimeInMillis();
		Long mEndTime = endTime.getTimeInMillis();
		String mLocation = place.getText().toString();
		String mName = headline.getText().toString();
		double lat = chosenPos.target.latitude;
		double lon = chosenPos.target.longitude;
		String mdescription = description.getText().toString();

		//get data not from View
		Long mId = isEdit ? mCurrHotSpotData.getmId(): 0L;
		String mAdminId = UserManager.INSTANCE.getMyID();
		String mImageURL = isEdit ? mCurrHotSpotData.getImageURL(): "";
		
		return new HotSpot(mId, mTime, mEndTime, mName, lat, lon, 
				mLocation, mdescription, mAdminId, mImageURL, bitmap, null, null);

	}
	
	//open galary
	int SELECT_PHOTO = 200;
	private void gotoGalery(){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);    
	}

	// time date objects
	Calendar startTime = Calendar.getInstance();
	Calendar endTime = Calendar.getInstance();
	
	class innerFirstDate implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	startTime.set(Calendar.YEAR, year);
    		startTime.set(Calendar.MONTH, monthOfYear);
    		startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    		
			dateInput.setText(TimeDateStringFactory.getDateStr(startTime.getTimeInMillis()));

        }
	}
	class secondInnerDate implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	endTime.set(Calendar.YEAR, year);
        	endTime.set(Calendar.MONTH, monthOfYear);
        	endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        	
        	endDateInput.setText(TimeDateStringFactory.getDateStr(endTime.getTimeInMillis()));
        }
	}
	class innerFirstTime implements OnTimeSetListener{
		@Override
		public void onTimeSet(TimePicker arg0, int hour, int minets) {
			startTime.set(Calendar.HOUR_OF_DAY, hour);
			startTime.set(Calendar.MINUTE, minets);
			
			timeInput.setText(TimeDateStringFactory.getTimeStr(startTime.getTimeInMillis()));
		}
        
	}
	class secondInnerTime implements OnTimeSetListener{
			@Override
			public void onTimeSet(TimePicker arg0, int hour, int minets) {
				endTime.set(Calendar.HOUR_OF_DAY, hour);
				endTime.set(Calendar.MINUTE, minets);
				
				endTimeInput.setText(TimeDateStringFactory.getTimeStr(endTime.getTimeInMillis()));
			}
		}

}
