package il.ac.technion.socialcampus;

import java.util.Set;

import il.ac.technion.logic.HotSpot;
import il.ac.technion.logic.HotSpotManager;
import il.ac.technion.logic.User;
import il.ac.technion.logic.UserManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class HotSpotDetailsFragment extends InfoBoxFragment {

	GoogleMap mMap;
	
	public HotSpotDetailsFragment() {
		// Required empty public constructor
	}
	
	public static HotSpotDetailsFragment newInstance(Long mHotSpotId) {
		HotSpotDetailsFragment fragment = new HotSpotDetailsFragment();
		Bundle args = new Bundle();
		args.putLong(HotSpotId, mHotSpotId);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_hot_spot_details, container,
				false);
		mView = v;
		share = ((ImageButton)mView.findViewById(R.id.shareImgBtn));
		joinLeave = ((ImageButton)mView.findViewById(R.id.joinImgBtn));
		pinUnpin = ((ImageButton)mView.findViewById(R.id.pinImgBtn));
		headline = ((TextView) mView.findViewById(R.id.name));
		timeStr = ((TextView)mView.findViewById(R.id.timeStr));
				
		setView();
		setMap();
		return v;
		
	}
	
	private void setGoing() {
		Set<User> u = UserManager.INSTANCE.getItemsbyIds(
				HotSpotManager.INSTANCE.getItemById(mHotSpotDataId).getmUseres());
		//TODO set the view
		//min(5,u.size)
		//for: make view
	}

	private void setMap() {
		if(mHotSpotDataId==null) return;
		
		HotSpot hs = HotSpotManager.INSTANCE.getItemById(mHotSpotDataId);
		LatLng ll = new LatLng( hs.getLangt(),hs.getLongt());
		SupportMapFragment f = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.My_map));
		mMap = f.getMap();

		CameraPosition CurrPos = new CameraPosition.Builder().target(ll).zoom(17f).bearing(300).tilt(50).build();
        if (mMap != null) {
        	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CurrPos));
        	mMap.addMarker(new MarkerOptions().position(ll));
        }
	}

	public void refresh(Long id){
		if (id==null)return;
		resetInfoBox(id);
		setMap();
		
	}
}
