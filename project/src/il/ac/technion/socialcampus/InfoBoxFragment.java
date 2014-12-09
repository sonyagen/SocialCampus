package il.ac.technion.socialcampus;

import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.HotSpot;
import il.ac.technion.logic.ServerCommunication.ServerRequestManager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class InfoBoxFragment extends Fragment {
	
	public interface ButtonInteraction {
		public void joinBtnClick(Long id);
		public void leaveBtnClick(Long id);  
		public void pinBtnClick(Long id);
		public void unpinBtnClick(Long id);
		public void shareBtnClick(Long id);
		public void editBtnClick(Long id);
		public void discardBtnClick(Long id);
		public int getRequestCodeForEditHotSpot();
	}
	
	ButtonInteraction mListener;
	
	protected static final String HotSpotId = "id";
	Context mContext;
	protected Long mHotSpotDataId;
	
	ImageButton share ;
	ImageButton joinLeave;
	ImageButton pinUnpin;
	TextView headline;
	TextView timeStr;
	ImageView image;
	TextView desc;
	TextView place;
	ImageView attending1;
	ImageView attending2;
	ImageView attending3;

	//TODO don't use mView - get an inflater instead.
	View mView;
	
	public static InfoBoxFragment newInstance(Long mHotSpotId) {
		InfoBoxFragment fragment = new InfoBoxFragment();
		Bundle args = new Bundle();
		args.putLong(HotSpotId, mHotSpotId);
		fragment.setArguments(args);
		return fragment;
	}

	public InfoBoxFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity(); 
		if (getArguments() != null) {
			mHotSpotDataId = getArguments().getLong(HotSpotId);
		}
	}
	
	RelativeLayout goingStrip;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		 View v = inflater.inflate(R.layout.fragment_info_box, container, false);
		 mView = v;
		 
		 share = ((ImageButton)mView.findViewById(R.id.shareImgBtn));
		 joinLeave = ((ImageButton)mView.findViewById(R.id.joinImgBtn));
		 pinUnpin = ((ImageButton)mView.findViewById(R.id.pinImgBtn));
		 headline = ((TextView) mView.findViewById(R.id.name));
		 timeStr = ((TextView)mView.findViewById(R.id.timeStr));
		 //desc = ((TextView)mView.findViewById(R.id.description));
		 place = ((TextView)mView.findViewById(R.id.place));
		 image = (ImageView)mView.findViewById(R.id.image);
		 attending1 = (ImageView)mView.findViewById(R.id.usr1);
		 attending2 = (ImageView)mView.findViewById(R.id.usr2);
		 attending3 = (ImageView)mView.findViewById(R.id.usr3);
		 goingStrip = (RelativeLayout)mView.findViewById(R.id.goingStrip);
		 goingStrip.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(),GoingUsersActivity.class).putExtra("id",mHotSpotDataId));
				}
			});
		 setView();
		 return v;
	}

	public HotSpot getCurrHotSpot(){
		return LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId);
	}
	
	public Long getCurrHotSpotId(){
		return mHotSpotDataId;
	}

	protected boolean validateHotSpot(){
		if (mHotSpotDataId == null) return false;
		HotSpot mHotSpotData = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId);
		if (mHotSpotData == null) return false;
		
		return true;
	}
	
	public void resetInfoBox(){
		setView();
	}
	
	public void resetInfoBoxBtn(){
		if (!validateHotSpot()) return;
		
		//if owner
		if (LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId).getAdminId()
				.equals( UserManager.INSTANCE.getMyID()) ){
			
			//set edit btn instead of join-leave btn
			joinLeave.setImageResource(R.drawable.ic_action_edit);
			joinLeave.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onEditBtnClick();
				}
			});
			
			//set discard btn instead of pin-unpin
			pinUnpin.setImageResource(R.drawable.ic_action_discard);
			pinUnpin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onDiscardBtnClick();
				}
			});
			 
			return;
		}
		
		HotSpot mHotSpotData = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId);
		
		//handle share
		//share.setVisibility(View.VISIBLE);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onShareBtnClick();
			}
		});
    	
		//handle join-leave
		Long hid = mHotSpotData.getmId();
		String uid = UserManager.INSTANCE.getMyID();
		if(LocalDBManager.INSTANCE.UserHotSpot.isCombined(uid, hid)){
			joinLeave.setImageResource(R.drawable.leave);
			joinLeave.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onLeaveBtnClick();
				}
			});
    	}else{
    		joinLeave.setImageResource(R.drawable.join);
    		joinLeave.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onJoinBtnClick();
				}
			});
			
    	}
		
		//handle pin-unpin
		//TODO PIN
		if (UserManager.INSTANCE.isPinned(mHotSpotData.getmId())){
			pinUnpin.setImageResource(R.drawable.ic_pinned);
			pinUnpin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onUnpinBtnClick();
				}
			});
    	}else{
    		pinUnpin.setImageResource(R.drawable.ic_pin);
    		pinUnpin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onPinBtnClick();
				}
			});
			
    	}
	}
	
	public void resetInfoBox(Long newId){
		if(LocalDBManager.INSTANCE.HotSpotDB.getItemById(newId) == null) return;
		//mHotSpotData = MyDB.INSTANCE.HotSpotDB.getItemById(newId);
		mHotSpotDataId = newId;
		resetInfoBox();
	}
	
	protected void setView() {
		if (mHotSpotDataId == null) return;
		HotSpot mHotSpotData = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId);
		if (mHotSpotData == null) return;
		
		//TODO: get the inflater instead using mView
		//View v = View.inflate(getActivity(), R.layout.fragment_info_box, container);
		
		String name = mHotSpotData.getmName();
    	headline.setText(name);
    	   	
    	Long time1 = mHotSpotData.getmTime();
    	Calendar c1 = Calendar.getInstance();
    	c1.setTimeInMillis(time1);
    	
    	Long time2 = mHotSpotData.getEndTime();
    	Calendar c2 = Calendar.getInstance();
    	c2.setTimeInMillis(time2);
   
    	if( c1.get(Calendar.DATE) == c2.get(Calendar.DATE) ){
    		String timeStart = new SimpleDateFormat("HH:mm").format(new Date(time1));
    		String timeEnd = new SimpleDateFormat("HH:mm").format(new Date(time2));
    		String Date = new SimpleDateFormat("dd/MM").format(new Date(time2));
    		timeStr.setText(timeStart + "-" + timeEnd + ", " + Date);
    	}else{
    		String timeStart = new SimpleDateFormat("HH:mm").format(new Date(time1));
    		String timeEnd = new SimpleDateFormat("HH:mm").format(new Date(time2));
    		String DateStart = new SimpleDateFormat("dd/MM").format(new Date(time1));
    		String DateEnd = new SimpleDateFormat("dd/MM").format(new Date(time2));
    		timeStr.setText(timeStart+ " " + DateStart + " - " + timeEnd+ " " + DateEnd );
    	}
    	
    	String location = mHotSpotData.getmLocation();
    	place.setText(location);
    	
    	HotSpot hs = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId);
    	Bitmap im = hs.getImage();
		if (im!=null) image.setImageBitmap(im);
		else new LoadProfileImage(hs, image, im, false).execute(hs.getImageURL());
		//image.setImageResource(R.drawable.wave);
    	
    	if (desc!=null)desc.setText(mHotSpotData.getmDesc());
    	
    	ArrayList<String> users = new ArrayList<String>();
    	users.addAll(LocalDBManager.INSTANCE.UserHotSpot.getUsersFromHotSpot(mHotSpotDataId));
    	attending3.setVisibility(View.GONE);
    	attending2.setVisibility(View.GONE);
    	attending1.setVisibility(View.GONE);
    	switch (users.size()) {
		
		default:
			attending3.setVisibility(View.VISIBLE);
			LocalDBManager.INSTANCE.UserDB.getItemById(users.get(2)).setUserPhoto(attending3);
		case 2:
			attending2.setVisibility(View.VISIBLE);
			LocalDBManager.INSTANCE.UserDB.getItemById(users.get(1)).setUserPhoto(attending2);
		case 1:
			attending1.setVisibility(View.VISIBLE);
			LocalDBManager.INSTANCE.UserDB.getItemById(users.get(0)).setUserPhoto(attending1);
		case 0:
			break;

		}
    	
    	resetInfoBoxBtn();
	}
	
	public void onJoinBtnClick() {
		
		HotSpot mCurrSpot = getCurrHotSpot();
		ServerRequestManager.INSTANCE.JoinUserHotSpot(
			UserManager.INSTANCE.getMyID(), mCurrSpot.getId(),new UiOnDone() {
				@Override
				public void execute() {
					resetInfoBoxBtn();
					if (mListener!=null) 
						mListener.joinBtnClick(mHotSpotDataId);
				}
			}, new UiOnError(mContext));
	}
	
	public void onLeaveBtnClick() {
		HotSpot mCurrSpot = getCurrHotSpot();
		ServerRequestManager.INSTANCE.BreakUserHotSpot( 
				UserManager.INSTANCE.getMyID(), mCurrSpot.getId(), new UiOnDone() {
					@Override
					public void execute() {
						resetInfoBoxBtn();
						if (mListener!=null) 
							mListener.leaveBtnClick(mHotSpotDataId);
					}
				}, new UiOnError(mContext));
	}

	public void onShareBtnClick() {
		//TODO share
		Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
		if (mListener!=null) 
			mListener.shareBtnClick(mHotSpotDataId);
	}
	
	public void onPinBtnClick() {
		
		UserManager.INSTANCE.pinHotSpot(mHotSpotDataId);
		resetInfoBoxBtn();
		mListener.pinBtnClick(mHotSpotDataId);
		
//		MyDB.INSTANCE.HotSpotDB.PinUserHotSpotToUser(getCurrHotSpotId(), UserManager.INSTANCE.getMyID());
//		resetInfoBoxBtn();
//		if (mListener!=null) 
//			mListener.pinBtnClick(mHotSpotDataId);
	}
	
	public void onUnpinBtnClick() {
		
		UserManager.INSTANCE.unPinHotSpot(mHotSpotDataId);
		resetInfoBoxBtn();
		mListener.unpinBtnClick(mHotSpotDataId);
		
//		MyDB.INSTANCE.HotSpotDB.UnpinUserHotSpotFromUser(getCurrHotSpotId(), UserManager.INSTANCE.getMyID());
//		resetInfoBoxBtn();
//		if (mListener!=null) 
//			mListener.unpinBtnClick(mHotSpotDataId);
	}

	public void onEditBtnClick() {
		if (mListener!=null){ 
			mListener.editBtnClick(mHotSpotDataId);

			startActivityForResult((new Intent(getActivity(), CreateNewHotSpotActivity.class)
				.putExtra(CreateNewHotSpotActivity.HotSpotId, mHotSpotDataId)), mListener.getRequestCodeForEditHotSpot());
		}
	}
	
	public void onDiscardBtnClick() {
		ServerRequestManager.INSTANCE.removeHotSpots(
				LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId), new UiOnDone() {
			
			@Override
			public void execute() {
				mListener.discardBtnClick(mHotSpotDataId);
				
			}
		}, null); 
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (ButtonInteraction) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

}
