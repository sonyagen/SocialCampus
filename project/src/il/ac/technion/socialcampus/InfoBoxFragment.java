package il.ac.technion.socialcampus;

import il.ac.technion.logic.HotSpot;
import il.ac.technion.logic.HotSpotManager;
import il.ac.technion.logic.UserManager;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class InfoBoxFragment extends Fragment{
	protected static final String HotSpotId = "id";

	//protected HotSpot mHotSpotData;
	protected Long mHotSpotDataId;
	protected OnFragmentInteractionListener mListener;
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
		if (getArguments() != null) {
			mHotSpotDataId = getArguments().getLong(HotSpotId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		 View v = inflater.inflate(R.layout.fragment_info_box, container, false);
		 mView = v;
		 setView();
		 return v;
	}

	public HotSpot getCurrHotSpot(){
		return HotSpotManager.INSTANCE.getItemById(mHotSpotDataId);
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
		ImageButton share = ((ImageButton)mView.findViewById(R.id.shareImgBtn));
		share.setVisibility(View.VISIBLE);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onShareBtnClick();
			}
		});
    	
		//handle join-leave
		ImageButton joinLeave = ((ImageButton)mView.findViewById(R.id.joinImgBtn));
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
		ImageButton pinUnpin = ((ImageButton)mView.findViewById(R.id.pinImgBtn));
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
    	((TextView) mView.findViewById(R.id.name)).setText(name);
    	
    	Long time = mHotSpotData.getmTime();
		String timeStr = (new SimpleDateFormat("HH:mm dd/MM/yyyy").format(new Date(time)));

    	((TextView)mView.findViewById(R.id.timeStr)).setText(timeStr);
    	
    	resetInfoBoxBtn();
	}
	

	public void onJoinBtnClick() {
		if (mListener != null) {
			mListener.onJoinBtnClick();
		}
	}
	
	public void onLeaveBtnClick() {
		if (mListener != null) {
			mListener.onLeaveBtnClick();
		}
	}

	public void onShareBtnClick() {
		if (mListener != null) {
			mListener.onShareBtnClick();
		}
	}
	public void onPinBtnClick() {
		if (mListener != null) {
			mListener.onPinBtnClick();
		}
	}
	public void onUnpinBtnClick() {
		if (mListener != null) {
			mListener.onUnpinBtnClick();
		}
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
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

	public interface OnFragmentInteractionListener {
		public void onJoinBtnClick();
		public void onLeaveBtnClick();
		public void onShareBtnClick();
		public void onUnpinBtnClick();
		public void onPinBtnClick();
	}

}
