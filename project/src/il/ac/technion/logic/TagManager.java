package il.ac.technion.logic;


import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public enum TagManager {
	INSTANCE;
	
	UserManager mUserManager = UserManager.INSTANCE;
	HotSpotManager mHotSpotManager = HotSpotManager.INSTANCE;
	
	public static interface UiOnDone{
		public void execute();
	}
	
	protected HashMap<Long,Tag> mData = new HashMap<Long,Tag>();
	
	Tag getItemById(Long id){
		return mData.get(id);
		
	}
	
	public  Set<Tag> getAllObjs() {
		return (Set<Tag>) mData.values();
	}
	
	public  Set<Tag> getItemsbyIds(Set<Long> Ids) {
		Set<Tag> $ = new TreeSet<Tag>();
		for(Long id : Ids){
			$.add(getItemById(id));
		}
		return $;
	}

	
	
	public void syncTags(final UiOnDone onRes, final UiOnError onErr){
		
		final List<Tag> res = new ArrayList<Tag>();
		SCAsyncRequest r = new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status == SCConnectionStatus.RESULT_OK){
					mData.clear();
					for (Tag h:res){
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
				res.addAll(DBManager.INSTANCE.getTags());
						
				return null;
			}
		};
		r.run();
		return;
	}

	public void addNewTag(final Tag tag, final UiOnDone uif, 
			final UiOnError uierror){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uierror.execute();
					return null;
				}
				//add the hotSpot with the new id to owned list
				mData.put(tag.getmId(), tag);
				uif.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				tag.setmId( DBManager.INSTANCE.addTag(tag).getmId());
				return null;
			}
		}.run();
	}
	
	public void editTag(final Tag tag, final UiOnDone uiOnDone,
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
				DBManager.INSTANCE.updateTag(tag);
				return null;
			}
		}.run();
	}

	public void removeTag(final Tag tag, final UiOnDone uiOnDone,
			final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				Set<Long> users = tag.getmUsers();
				for(Long i: users){
					((User)mUserManager.getItemById(i)).removeTag(tag.getmId());
				}
				Set<Long> hots = tag.getmHotSpots();
				for(Long i: hots){
					((HotSpot)mHotSpotManager.getItemById(i)).removeTag(tag.getmId());
				}
				mData.remove(tag);
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				//TODO: this function on the server removes all tags and users
				DBManager.INSTANCE.removeTag(tag);
				return null;
			}
		}.run();
	}

	
	//user - tag
	public  void breakUserTag(final Tag tag, final Long uid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				//user.removeTag
				((User)mUserManager.getItemById(uid)).removeTag(tag.getmId());
				//tag.removeUser
				tag.removeUser(mUserManager.getMyID());
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.breakUserTag(tag.getmId(), uid);
				return null;
			}
		}.run();
	}
	//user - tag
	public  void joinUserTag(final Tag tag, final Long uid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				//user.addTag
				((User)mUserManager.getItemById(uid)).addTag(tag.getmId());
				//tag.addUser
				tag.addUser(mUserManager.getMyID());
		
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.joinUserTag(tag.getmId(), uid);
			return null;
			}
		}.run();
	}
	
	//hotspot - tag
	public  void breakSpotTag(final Tag tag, final Long hsid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				//hotSpot.removeTag
				((HotSpot)mHotSpotManager.getItemById(hsid)).removeTag(tag.getmId());
				//Tag.removeHotSpot
				tag.leaveHotSpot(hsid);
				
				uiOnDone.execute();
				return null;
			}
		
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.breakSpotTag(tag.getmId(), hsid);
				return null;
			}
		}.run();
	}
	//hotspot - tag
	public  void joinSpotTag(final Tag tag, final Long hsid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				//hotSpot.addTag
				((HotSpot)mHotSpotManager.getItemById(hsid)).addTag(tag.getmId());
				//Tag.addHotSpot
				tag.joinHotSpot(hsid);
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.joinSpotTag(tag.getmId(), hsid);
			return null;
			}
		}.run();
	}
}


