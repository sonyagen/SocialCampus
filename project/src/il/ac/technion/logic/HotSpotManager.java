package il.ac.technion.logic;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public enum HotSpotManager {
	INSTANCE;
	
	UserManager mUserManager = UserManager.INSTANCE;
	TagManager mTagManager = TagManager.INSTANCE;
	
	public static interface UiOnDone{
		public void execute();
	}
	
	protected HashMap<Long,HotSpot> mData = new HashMap<Long,HotSpot>();
	
	public HotSpot getItemById(Long id){
		return mData.get(id);
	}
	
	public  List<HotSpot> getAllObjs() {
		List<HotSpot> $ = new ArrayList<HotSpot>();
		Collection<HotSpot> vs = mData.values();
		for(HotSpot v: vs){
			$.add(v);
		}
		//$.addAll(mData.values());  
		return $;
	}
	
	public  Set<HotSpot> getItemsbyIds(Set<Long> Ids) {
		Set<HotSpot> $ = new TreeSet<HotSpot>();
		for(Long id : Ids){
			$.add(getItemById(id));
		}
		return $;
	}

	public void syncHotSpots(final UiOnDone onRes, final UiOnError onErr){
		
		final List<HotSpot> res = new ArrayList<HotSpot>();
		SCAsyncRequest r = new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status == SCConnectionStatus.RESULT_OK){
					mData.clear();
					for (HotSpot h:res){
						mData.put(h.getmId(), h);
					}
					
					if(onRes != null){
						onRes.execute();
					}	
				}	else {
					if(onErr != null){
						onErr.execute();
					}
				}
				return null;
			}

			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				res.clear();
				res.addAll(DBManager.INSTANCE.getAllHotSpots());
						
				return null;
			}
		};
		r.run();
		return;
	}
	
	public void syncHotSpots(final Double latitude,final Double lontitude,final Double radios,
		final UiOnDone onRes, final UiOnError onErr){
		
		final List<HotSpot> res = new ArrayList<HotSpot>();
		SCAsyncRequest r = new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status == SCConnectionStatus.RESULT_OK){
					mData.clear();
					for (HotSpot h:res){
						mData.put(h.getmId(), h);
					}
					
					if(onRes != null){
						onRes.execute();
					}	
				}	else {
					if(onErr != null){
						onErr.execute();
					}
				}
				return null;
			}

			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				res.clear();
				res.addAll(DBManager.INSTANCE.getHotSpotsByRadios(latitude,lontitude, radios));
						
				return null;
			}
		};
		r.run();
		return;
	}
	
	public void addNewHotSpot(final HotSpot hotSpot, final UiOnDone uif, 
			final UiOnError uierror){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uierror.execute();
					return null;
				}
				//add the hotSpot with the new id to owned list
				mData.put(hotSpot.getmId(), hotSpot);
				uif.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				hotSpot.setmId( DBManager.INSTANCE.addHotSpot(hotSpot).getmId());
				return null;
			}
		}.run();
	}
	
	public void editHotSpot(final HotSpot hotSpot, final UiOnDone uiOnDone,
			final UiOnError uiOnError) {
			new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.updateHotSpot(hotSpot);
				return null;
			}
		}.run();
	}
	
	public void removeHotSpot(final HotSpot hotSpot, final UiOnDone uiOnDone,
			final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				Set<Long> users = hotSpot.getmUseres();
				for(Long i: users){
					((User)mUserManager.getItemById(i)).leaveHotSpot(hotSpot.getmId());
				}
				Set<Long> tags = hotSpot.getmTags();
				for(Long i: tags){
					((Tag)mTagManager.getItemById(i)).leaveHotSpot(hotSpot.getmId());
				}
				mData.remove(hotSpot);
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				//TODO: this function on the server removes all tags and users
				DBManager.INSTANCE.removeHotSpot(hotSpot);
				return null;
			}
		}.run();
	}
	
	public void breakUserHotSpot(final HotSpot hotSpot, final Long uid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				//user.removeHotSpot
				((User)mUserManager.getItemById(uid)).leaveHotSpot(hotSpot.getmId());
				//hotSpot.removeUser
				hotSpot.leaveHotSpot(mUserManager.getMyID());
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.breakUserHotSpot(hotSpot.getmId(), uid);
				return null;
			}
		}.run();
	}
	
	public void joinUserHotSpot(final HotSpot hotSpot, final Long uid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				((User)mUserManager.getItemById(uid)).joinHotSpot(hotSpot.getmId());
				hotSpot.joinHotSpot(mUserManager.getMyID());
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.joinUserHotSpot(hotSpot.getmId(), uid);
			return null;
			}
		}.run();
	}
}


