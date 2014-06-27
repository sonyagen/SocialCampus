package il.ac.technion.socialcampus;

import il.ac.technion.logic.HotSpot;
import il.ac.technion.logic.HotSpotManager;
import il.ac.technion.logic.HotSpotManager.UiOnDone;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements 
	InfoBoxFragment.OnFragmentInteractionListener{
  
	private GoogleMap mMap;
	private final Context mContext = this;
	
	static final CameraPosition TECHNION =
            new CameraPosition.Builder().target(new LatLng(32.776778,35.023127))
                    .zoom(17f)
                    .bearing(300)
                    .tilt(50)
                    .build();
	
	private HashMap<String,Long> mMarkersHotSpotsTrans = new HashMap<String,Long>();
	
	

    @Override
	protected void onStart() {
		super.onStart();
		syncDb();
	}

    private void syncDb(){
    	HotSpotManager.INSTANCE.syncHotSpots(new UiOnDone() {
			@Override
			public void execute() {
				setUpMapIfNeeded();
			}
		}, new UiOnError(this));
    	UserManager.INSTANCE.syncUsers(new UserManager.UiOnDone() {
			@Override
			public void execute() {
				UserManager.INSTANCE.setCurrentUser(1L);
			}
		}, new UiOnError(this));
    	TagManager.INSTANCE.syncTags(new TagManager.UiOnDone() {
			@Override
			public void execute() {}
		}, new UiOnError(this));
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        hideInfoBox();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void resetMarkers(){
    	List<HotSpot> hs = HotSpotManager.INSTANCE.getAllObjs();
    	if (mMap==null || hs==null) return; 
    	for(HotSpot h: hs){
        	String id = mMap.addMarker(new MarkerOptions().
        			position(new LatLng(h.getLangt(),h.getLongt()))).getId();
        	mMarkersHotSpotsTrans.put(id, h.getmId());
    	}
    }
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                addMapListeners();
            }
        }
    }

    private void setUpMap() {
    	if (mMap==null) return;
    	
    	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(TECHNION));
    	resetMarkers();
    }
    
    private void hideInfoBox(){
    	FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.infoBoxFrag);
        fm.beginTransaction().hide(fr).commit();
    }
    
    private void ShowInfoBox(Long hid){
    	FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.infoBoxFrag);
        fr.resetInfoBox(hid);
        fm.beginTransaction().show(fr).commit();
    }
    
	private InfoBoxFragment getInfoBox() {
		FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.infoBoxFrag);
        return fr;
	}
	
    private void addMapListeners() {
    	mMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				hideInfoBox();
			}
		});
	    mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	        @Override 
	        public boolean onMarkerClick(final Marker m) {
	        	Long hid = mMarkersHotSpotsTrans.get(m.getId());	        	
	        	ShowInfoBox(hid);
	            return true;
	        }
	      });

    }

	@Override
	public void onJoinBtnClick() {
		HotSpot mCurrSpot = getInfoBox().getCurrHotSpot();
		HotSpotManager.INSTANCE.joinUserHotSpot(mCurrSpot, 
				UserManager.INSTANCE.getMyID(), new HotSpotManager.UiOnDone() {
					
					@Override
					public void execute() {
						getInfoBox().resetInfoBoxBtn();
					}
				}, new UiOnError(mContext));
		
	}
	

	@Override
	public void onLeaveBtnClick() {
		HotSpot mCurrSpot = getInfoBox().getCurrHotSpot();
		HotSpotManager.INSTANCE.breakUserHotSpot(mCurrSpot, 
				UserManager.INSTANCE.getMyID(), new HotSpotManager.UiOnDone() {
					
					@Override
					public void execute() {
						getInfoBox().resetInfoBoxBtn();
					}
				}, new UiOnError(mContext));
		
	}

	
	@Override
	public void onShareBtnClick() {
		// TODO for John??
		
	}

}
