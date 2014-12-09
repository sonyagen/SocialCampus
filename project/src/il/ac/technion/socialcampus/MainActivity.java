package il.ac.technion.socialcampus;

import il.ac.technion.logic.LocationFactury;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.HotSpot;
import il.ac.technion.logic.ServerCommunication.ServerRequestManager;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MainActivity extends BaseLoginActivity implements InfoBoxFragment.ButtonInteraction	{
	//codes
	int CREATE_NEW = 100;
	int PROFILE = 200;
	int HS_VIEW = 300;
	int LOGIN = 400;
	int EDIT = 500;
	Long currHotSpotOnDisplay;
	
	private ImageView imgProfilePic;
	private ImageButton addNewBtn;
	private GoogleMap mMap;
	private final Context mContext = this;
	private CameraPosition cameraPos = LocationFactury.currentPositionOrCornellTeach();;
	private HashMap<String,Long> mMarkersHotSpotsTrans = new HashMap<String,Long>();
	private HashMap<Long,Marker> mHotSpotsMarkersTrans = new HashMap<Long,Marker>();
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	
    @Override
	protected void onStart() {
		super.onStart();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        if (savedInstanceState==null){
        	syncDb();
        }else{
        	setUpMapIfNeeded();
        }
        setListeners();
        hideInfoBox();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UserManager.INSTANCE.isLoggedIn()){
        	UserManager.INSTANCE.getMyData().setUserPhoto(imgProfilePic);
        	addNewBtn.setVisibility(ImageButton.VISIBLE);
        }else{
        	addNewBtn.setVisibility(ImageButton.INVISIBLE);
        }
        hideInfoBox();
        setMapCenterAndMarkers();
        focusMapOnHotSpot(currHotSpotOnDisplay);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	super.onActivityResult(requestCode,resultCode,data);
    	//setUpMap();
        if (requestCode == CREATE_NEW) {
            if(resultCode == RESULT_OK){
                currHotSpotOnDisplay = data.getLongExtra("hotSpoId", -1L);
//                if(result != -1L){
//                	focusMapOnHotSpot(result);
//                }
            }
        }
        if(requestCode == EDIT){
        	currHotSpotOnDisplay = data.getLongExtra("hotSpoId", -1L);
        }
    }
    
    private void focusMapOnHotSpot(Long hid){
    	if(hid == null || hid == -1L) return;
    	HotSpot h = LocalDBManager.INSTANCE.HotSpotDB.getItemById(hid);
    	if(h==null) return;
    	cameraPos = LocationFactury.buildCameraPosition(h.getLangt(), h.getLongt());
    	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
    	ShowInfoBox(hid);
    }
    
    private void syncDb(){
    	
    	ServerRequestManager.INSTANCE.syncHotSpots(new UiOnDone() {
			@Override
			public void execute() {
				setUpMapIfNeeded();
			}
		}, new UiOnError(this));
    	ServerRequestManager.INSTANCE.syncUsers(null, new UiOnError(this));
    	ServerRequestManager.INSTANCE.syncTags(null, new UiOnError(this));
    
    	ServerRequestManager.INSTANCE.syncRelationsTagHotSpot(null,null);
    	ServerRequestManager.INSTANCE.syncRelationsUserTag(null,null);
    	ServerRequestManager.INSTANCE.syncRelationsHotSpotUser(null,null);

    }
	
    private void setListeners(){
    	imgProfilePic = (ImageView) findViewById(R.id.ProfilePic);
        imgProfilePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//old profile
//				startActivityForResult(new Intent(getApplicationContext(), 
//						ProfileActivity.class), PROFILE);
				
				
				
				if(!UserManager.INSTANCE.isLoggedIn()){
//					startActivityForResult(new Intent(getApplicationContext(),
//							BaseLoginActivity.class), LOGIN);
					showLoginDialog();
				}else{
					startActivityForResult(new Intent(getApplicationContext(), 
						ProfileTabActivity.class), PROFILE);
				}
			}
		});
        addNewBtn = (ImageButton) findViewById(R.id.addNewBtn);
        addNewBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(mContext, CreateNewHotSpotActivity.class), CREATE_NEW);
			}
		});
    }
    
    private void resetMarkers(){
    	
    	
    	////List<HotSpot> hs = HotSpotManager.INSTANCE.getAllObjs();
    	// TODO:
    	//THIS IS A Try for the new syncer
    	List<HotSpot>hs = LocalDBManager.INSTANCE.HotSpotDB.getAllObjs();
    	//
    	if (mMap==null || hs==null) return; 
    	for(HotSpot h: hs){
        	Marker m = mMap.addMarker(new MarkerOptions().
        			position(new LatLng(h.getLangt(),h.getLongt())));

        	mMarkersHotSpotsTrans.put(m.getId(), h.getmId());
        	mHotSpotsMarkersTrans.put(h.getmId(), m);
    	}
    }
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maimMap))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setMapCenterAndMarkers();
                addMapListeners();
            }
        }
    }

    private void setMapCenterAndMarkers() {
    	if (mMap==null) return;
    	mMap.clear();
    	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
    	resetMarkers();
    }
    
    private void hideInfoBox(){
    //	reColorLastMarker();
    	currHotSpotOnDisplay = -1L;
    	FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.infoBoxFrag);
        fm.beginTransaction().hide(fr).commit();
    }
    
    private void ShowInfoBox(Long hid){
    //	reColorMarker(mHotSpotsMarkersTrans.get(hid));
    	FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.infoBoxFrag);
        fr.resetInfoBox(hid);
        fm.beginTransaction().show(fr).commit();
    }
    
//    static Marker last;
//    private void reColorMarker(Marker m){
//    	reColorLastMarker();
//    	m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//    	last = m;
//    }
    
//    private void reColorLastMarker(){
//    	if(last != null) 
//    		last.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//    }
    
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
	        	//focusMapOnHotSpot(hid);
	        	ShowInfoBox(hid);
	        	currHotSpotOnDisplay = hid;
	            return true;
	        }
	      });
	    findViewById(R.id.infoBoxFrag).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivityForResult(new Intent(mContext, HotSpotInfoActivity.class).
						putExtra("id",getInfoBox().getCurrHotSpotId()), HS_VIEW);

			}
		});

    }
    
	@Override
	public void joinBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void leaveBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pinBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void unpinBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void shareBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void editBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void discardBtnClick(Long id) {
		mHotSpotsMarkersTrans.get(id).remove();
		hideInfoBox();
	}

	@Override
	protected void updateUIAfterLogin() {
		 if(UserManager.INSTANCE.isLoggedIn()){
	        	UserManager.INSTANCE.getMyData().setUserPhoto(imgProfilePic);
	        	addNewBtn.setVisibility(ImageButton.VISIBLE);
		 }
	}

	@Override
	public int getRequestCodeForEditHotSpot() {
		return EDIT;
	}



}
