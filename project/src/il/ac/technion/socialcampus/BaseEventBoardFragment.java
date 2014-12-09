package il.ac.technion.socialcampus;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link BaseEventBoardFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link BaseEventBoardFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public abstract class BaseEventBoardFragment extends Fragment implements SmartDestroyFragment{

	private List<Long> mHotSpotIds = new ArrayList<Long>();
	LinearLayout mBoard;
	TextView err;

	public BaseEventBoardFragment() {
		// Required empty public constructor
	}

	public String alias;
	public abstract String getAlias();
	public abstract List<Long> getHotSpotIdsToDisplay();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alias = getAlias();
		
		
	}

	protected abstract View inflateView (LayoutInflater inflater, ViewGroup container);
	protected abstract int getLinearLayoutID();
	protected abstract String getNoHotSpotsToDisplayStr();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		 View v = inflateView(inflater,container);//inflater.inflate(R.layout.fragment_event_board, container, false);
		 mBoard = (LinearLayout)v.findViewById(getLinearLayoutID()); 
		 
		((TextView)v.findViewById(R.id.headline)).setText("Your " + alias + " board:");
		err = ((TextView)v.findViewById(R.id.errMsg));
		
		setView();
		  
		 return v;
	}
	
	private void setView(){
		mHotSpotIds = getHotSpotIdsToDisplay();
		if(mHotSpotIds.size()!=0 ){
			mBoard.removeAllViews();
			for(Long id : mHotSpotIds){
				InfoBoxFragment box = InfoBoxFragment.newInstance(id);
				getActivity().getSupportFragmentManager().beginTransaction()
				.add(getLinearLayoutID(), box, id.toString()).commit();
			}
		}
		else{
			err.setVisibility(View.VISIBLE);
			err.setText(getNoHotSpotsToDisplayStr());
		}
	}
	
	public void refreshFragView(){
		setView();
	}
	
	private boolean illigalToCommitAtThisPoint = false;
	
	public void KillBeforeActivityIsDestroyed(){
		onDestroyView();
		illigalToCommitAtThisPoint = true;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		for(Long id : mHotSpotIds){
			Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(id.toString());
			if(!illigalToCommitAtThisPoint)
				getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
		}
	}	
}
