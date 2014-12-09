package il.ac.technion.socialcampus;

import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.HotSpot;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class HotSpotDetailsFragment extends InfoBoxFragment {

	GoogleMap mMap;
	TagsBoxFragment tagsBox;
	
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
		setHasOptionsMenu(true);
	}

	RelativeLayout goingStrip;
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
		desc = ((TextView)mView.findViewById(R.id.description));
		image = ((ImageView)mView.findViewById(R.id.image));
		attending1 = (ImageView)mView.findViewById(R.id.usr1);
		attending2 = (ImageView)mView.findViewById(R.id.usr2);
		attending3 = (ImageView)mView.findViewById(R.id.usr3);
		
		place = ((TextView)mView.findViewById(R.id.place));

		tagsBox = (TagsBoxFragment)getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.hsTagBox);
		tagsBox.buildTags(LocalDBManager.INSTANCE.TagHotSpot.getTagsFromHotSpot(mHotSpotDataId));
		
		goingStrip = (RelativeLayout)mView.findViewById(R.id.goingStrip);
		goingStrip.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),GoingUsersActivity.class)
				.putExtra("id",mHotSpotDataId));
			}
		});
		
		setView();
		
		return v;	
	}
		
	@Override
	protected void setView() {
		super.setView();
		setMap();
		joinLeave.setVisibility(View.GONE);
		pinUnpin.setVisibility(View.GONE);
		share.setVisibility(View.GONE);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(tagsBox!=null) 
			tagsBox.buildTags(LocalDBManager.INSTANCE.TagHotSpot.getTagsFromHotSpot(mHotSpotDataId));
	}

	MenuItem joinLEaveEditItem;
	MenuItem pinUnpinDeleteItem;
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.hot_spot_info, menu);
		
		joinLEaveEditItem = menu.findItem(R.id.joinLEaveEditItem);
		pinUnpinDeleteItem = menu.findItem(R.id.pinUnpinDeleteItem);
	
		//handle share	
		MenuItem shareItem = menu.findItem(R.id.shareItem);
		shareItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				onShareBtnClick();
				return true;
			}
		});
		resetActionBarBtn();
	}
	
	public void resetActionBarBtn(){
		if (!validateHotSpot()) return;
		
		MenuItem joinLeave = joinLEaveEditItem;
		MenuItem pinUnpin = pinUnpinDeleteItem;
		
		//if owner
		if (LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId).getAdminId()
				.equals( UserManager.INSTANCE.getMyID()) ){
			
			//set edit btn instead of join-leave btn
			
			
			joinLeave.setIcon(R.drawable.ic_action_edit);
			joinLeave.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					onEditBtnClick();
					return true;
				}
			});
			
			//set discard btn instead of pin-unpin
			pinUnpin.setIcon(R.drawable.ic_action_discard);
			pinUnpin.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					onDiscardBtnClick();
					return true;
				}
			});
			 
			return;
		}
		
		HotSpot mHotSpotData = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId);
		
		//handle share
		share.setVisibility(View.VISIBLE);
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
			joinLeave.setIcon(R.drawable.leave);
			joinLeave.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					onLeaveBtnClick();
					return true;
				}
			});
    	}else{
    		joinLeave.setIcon(R.drawable.join);
    		joinLeave.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					onJoinBtnClick();
					return true;
				}
			});
			
    	}
		
		//handle pin-unpin
		if (UserManager.INSTANCE.isPinned(mHotSpotData.getmId())){
			pinUnpin.setIcon(R.drawable.ic_pinned);
			pinUnpin.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					onUnpinBtnClick();
					return true;
				}
			});
    	}else{
    		pinUnpin.setIcon(R.drawable.ic_pin);
    		pinUnpin.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					onPinBtnClick();
					return true;
				}
			});
			
    	}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
	    	case R.id.ok:
//	    		if (!validateInput()) return true;
//	    		final HotSpot fromView = createHotSpotFromView();
//				if (isEdit){
//					ServerRequestManager.INSTANCE.updateHotSpots(fromView, new UiOnDone() {
//						@Override
//						public void execute() {
//							setReturnAndFinish(fromView.getId());
//						}
//					}, new UiOnError(getActivity()));
//				}else{
//					//create new: 
//					ServerRequestManager.INSTANCE.addHotSpots(fromView, new UiOnDone() {
//						@Override
//						public void execute() {
//							setReturnAndFinish(fromView.getId());
//						}
//					}, new UiOnError(getActivity()));
//				}
//				return true;
//		      case R.id.cancel:
//		    	  setReturnAndFinish(-1L);
//		    	  return true;
		      default:
		    	  return super.onOptionsItemSelected(item);
	
	   }
	
	}
	
	private void setGoing() {
//		List<Long> a = LocalDBManager.INSTANCE.UserHotSpot.getUsersFromHotSpot(mHotSpotDataId);
//				//HotSpotDB.getItemById(mHotSpotDataId).getmUseres();
//		List<Long> b = new ArrayList<Long>();
//		for(Long s : a){
//			b.add(s);
//		}
//		Set<User> u = LocalDBManager.INSTANCE.UserDB.getItemsbyIds(b);
//		//TODO set the view
//		//min(5,u.size)
//		//for: make view
	}

	private void setMap() {
		if(mHotSpotDataId==null) return;
		
		HotSpot hs = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId);
		LatLng ll = new LatLng( hs.getLangt(),hs.getLongt());
		SupportMapFragment f = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.My_map));
		mMap = f.getMap();

		CameraPosition CurrPos = new CameraPosition.Builder().target(ll).zoom(17f).bearing(300).tilt(50).build();
        if (mMap != null) {
        	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CurrPos));
        	mMap.addMarker(new MarkerOptions().position(ll));
        	
        	mMap.setOnMapClickListener(new OnMapClickListener() {
				
				@Override
				public void onMapClick(LatLng arg0) {
					
					HotSpot hs = LocalDBManager.INSTANCE.HotSpotDB.getItemById(mHotSpotDataId);
					
					String uri = "http://maps.google.com/maps?" 
							+ "&daddr=" + String.valueOf(hs.getLangt()) + "," + String.valueOf(hs.getLongt());
					
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
					startActivity(intent);
					
				}
			});
        }
	}

	public void refresh(Long id){
		if (id==null)return;
		resetInfoBox(id);
		setMap();
		
	}

	public void onJoinBtnClick() {
		super.onJoinBtnClick();
		resetActionBarBtn();
	}
	
	public void onLeaveBtnClick() {
		super.onLeaveBtnClick();
		resetActionBarBtn();
	}
	
	public void onPinBtnClick() {
		super.onPinBtnClick();
		resetActionBarBtn();
	}
	
	public void onUnpinBtnClick() {
		super.onUnpinBtnClick();
		resetActionBarBtn();
	}
	
	public void onEditBtnClick() {
		super.onEditBtnClick();
		resetActionBarBtn();
	}
	
	public void onDiscardBtnClick() {
		super.onDiscardBtnClick();
		resetActionBarBtn();
	}

	
//	@Override
//	public void onTagClick(long tid) {
//		Tag t = TagManager.INSTANCE.getItemsbyId(tid);
//		// TODO Auto-generated method stub
//		Toast.makeText(mContext, t.getmName(), Toast.LENGTH_SHORT).show();
//		
//	}
}
