package il.ac.technion.socialcampus;

import il.ac.technion.logic.HotSpot;
import il.ac.technion.logic.HotSpotManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InfoBoxFragment extends Fragment{
	private static final String HotSpotId = "id";

	private HotSpot mHotSpotData;
	private OnFragmentInteractionListener mListener;
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
			Long Id = getArguments().getLong(HotSpotId);
			mHotSpotData = HotSpotManager.INSTANCE.getItemById(Id);
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

	public void resetInfoBox(){
		setView();
	}
	
	public void resetInfoBox(Long newId){
		if(HotSpotManager.INSTANCE.getItemById(newId) == null) return;
		mHotSpotData = HotSpotManager.INSTANCE.getItemById(newId);
		resetInfoBox();
	}
	
	protected void setView() {
		
		if (mHotSpotData == null) return;
		
		//TODO: get the inflater instead using mView
		
		String name = mHotSpotData.getmName();
    	((TextView) mView.findViewById(R.id.name)).setText(name);
//    	
//    	Long time = mHotSpotData.getmTime();
//		String timeStr = (new SimpleDateFormat("dd/MM HH:mm").format(new Date(time)));
//
//    	((TextView)mView.findViewById(R.id.timeStr)).setText(timeStr);
//    	
//    	if (UserManager.INSTANCE.getMyData().isJoined(mHotSpotData.getmId())){
//    		mView.findViewById(R.id.joinBtn).setVisibility(View.GONE); 
//    		mView.findViewById(R.id.leaveBtn).setVisibility(View.VISIBLE);
//    	}else{
//    		mView.findViewById(R.id.joinBtn).setVisibility(View.VISIBLE); 
//    		mView.findViewById(R.id.leaveBtn).setVisibility(View.GONE);
//    	}
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
	}

}
