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

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.map);
        fm.beginTransaction().hide(fr).commit();
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
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
    	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(TECHNION));
    	List<HotSpot> hs = HotSpotManager.INSTANCE.getAllObjs();
    	for(HotSpot h: hs){
        	String id = mMap.addMarker(new MarkerOptions().
        			position(new LatLng(h.getLangt(),h.getLongt()))).getId();
        	mMarkersHotSpotsTrans.put(id, h.getmId());

    	}
    }
    
    private void hideInfoBox(){
    	FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.map);
        fm.beginTransaction().hide(fr).commit();
    }
    
    private void ShowInfoBox(Long hid){
    	FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.map);
        fr.resetInfoBox(hid);
        fm.beginTransaction().show(fr).commit();
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
	    
//	    findViewById(R.id.shareBtn).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(mContext, "TODO: SHARE BTN", Toast.LENGTH_SHORT).show();
//			}
//		});
//	    findViewById(R.id.joinBtn).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				HotSpotManager.INSTANCE.joinUserHotSpot(mCurrSpot, 
//						UserManager.INSTANCE.getMyID(), new HotSpotManager.UiOnDone() {
//							
//							@Override
//							public void execute() {
//								findViewById(R.id.joinBtn).setVisibility(View.GONE); 
//			        			findViewById(R.id.leaveBtn).setVisibility(View.VISIBLE);
//							}
//						}, new UiOnError(mContext));
//				
//			}
//		});
//	    findViewById(R.id.leaveBtn).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				HotSpotManager.INSTANCE.breakUserHotSpot(mCurrSpot, 
//						UserManager.INSTANCE.getMyID(), new HotSpotManager.UiOnDone() {
//							
//							@Override
//							public void execute() {
//								findViewById(R.id.joinBtn).setVisibility(View.VISIBLE); 
//			        			findViewById(R.id.leaveBtn).setVisibility(View.GONE);
//								
//							}
//						}, new UiOnError(mContext));
//			}
//		});
    }

	@Override
	public void onJoinBtnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeaveBtnClick() {
		// TODO Auto-generated method stub
		
	}

}
