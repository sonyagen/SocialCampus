package il.ac.technion.socialcampus;

import il.ac.technion.logic.HotSpot;
import il.ac.technion.logic.HotSpotManager;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class InfoBoxFragment extends Fragment {
	protected static final String HotSpotId = "id";
	Context mContext;

	//protected HotSpot mHotSpotData;
	protected Long mHotSpotDataId;
//	protected OnFragmentInteractionListener mListener;
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

	//	TODO------
	ImageButton share ;
	ImageButton joinLeave;
	ImageButton pinUnpin;
	TextView headline;
	TextView timeStr;
	
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
		 
		 setView();
		 return v;
	}

	public HotSpot getCurrHotSpot(){
		return HotSpotManager.INSTANCE.getItemById(mHotSpotDataId);
	}
	
	public Long getCurrHotSpotId(){
		return mHotSpotDataId;
	}
	
	public void resetInfoBox(){
		setView();
	}
	
	protected boolean validateHotSpot(){
		if (mHotSpotDataId == null) return false;
		HotSpot mHotSpotData = HotSpotManager.INSTANCE.getItemById(mHotSpotDataId);
		if (mHotSpotData == null) return false;
		
		return true;
	}
	
	public void resetInfoBoxBtn(){
		if (!validateHotSpot()) return;
		
		HotSpot mHotSpotData = HotSpotManager.INSTANCE.getItemById(mHotSpotDataId);
		
		//handle share
		
		share.setVisibility(View.VISIBLE);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onShareBtnClick();
			}
		});
    	
		//handle join-leave
		if (UserManager.INSTANCE.getMyData().isJoined(mHotSpotData.getmId())){
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
		if (UserManager.INSTANCE.getMyData().isPinned(mHotSpotData.getmId())){
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
		if(HotSpotManager.INSTANCE.getItemById(newId) == null) return;
		//mHotSpotData = HotSpotManager.INSTANCE.getItemById(newId);
		mHotSpotDataId = newId;
		resetInfoBox();
	}
	
	protected void setView() {
		if (mHotSpotDataId == null) return;
		HotSpot mHotSpotData = HotSpotManager.INSTANCE.getItemById(mHotSpotDataId);
		if (mHotSpotData == null) return;
		
		//TODO: get the inflater instead using mView
		//View v = View.inflate(getActivity(), R.layout.fragment_info_box, container);
		
		String name = mHotSpotData.getmName();
    	headline.setText(name);
    	
    	Long time = mHotSpotData.getmTime();
    	timeStr.setText(new SimpleDateFormat("HH:mm dd/MM").format(new Date(time)));
    	
    	resetInfoBoxBtn();
	}
	

	public void onJoinBtnClick() {
		
		HotSpot mCurrSpot = getCurrHotSpot();
		HotSpotManager.INSTANCE.joinUserHotSpot(mCurrSpot, 
			UserManager.INSTANCE.getMyID(), new UiOnDone() {
				@Override
				public void execute() {
					resetInfoBoxBtn();
				}
			}, new UiOnError(mContext));
	}
	
	public void onLeaveBtnClick() {
		HotSpot mCurrSpot = getCurrHotSpot();
		HotSpotManager.INSTANCE.breakUserHotSpot(mCurrSpot, 
				UserManager.INSTANCE.getMyID(), new UiOnDone() {
					@Override
					public void execute() {
						resetInfoBoxBtn();
					}
				}, new UiOnError(mContext));
	}

	public void onShareBtnClick() {
		//TODO share
		Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
	}
	public void onPinBtnClick() {
		HotSpotManager.INSTANCE.PinUserHotSpotToUser(getCurrHotSpotId(), UserManager.INSTANCE.getMyID());
		resetInfoBoxBtn();
	}
	public void onUnpinBtnClick() {
		HotSpotManager.INSTANCE.UnpinUserHotSpotFromUser(getCurrHotSpotId(), UserManager.INSTANCE.getMyID());
		resetInfoBoxBtn();
	}


//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
//	}

//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mListener = null;
//	}

	
	
//	public interface OnFragmentInteractionListener {
//		public void onJoinBtnClick();
//		public void onLeaveBtnClick();
//		public void onShareBtnClick();
//		public void onUnpinBtnClick();
//		public void onPinBtnClick();
//	}

}
