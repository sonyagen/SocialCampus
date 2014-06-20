package il.ac.technion.logic;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public enum UserManager{
	INSTANCE;
	
	TagManager mTagManager = TagManager.INSTANCE;
	HotSpotManager mHotSpotManager = HotSpotManager.INSTANCE;
	
	public static interface UiOnDone{
		public void execute();
	}
	
	protected HashMap<Long,User> mData = new HashMap<Long,User>();
	
	User getItemById(Long id){
		return mData.get(id);
		
	}
	
	public  Set<User> getAllObjs() {
		return (Set<User>) mData.values();
	}
	
	public  Set<User> getItemsbyIds(Set<Long> Ids) {
		Set<User> $ = new TreeSet<User>();
		for(Long id : Ids){
			$.add(getItemById(id));
		}
		return $;
	}

	
	private User currentUser = createAnonimus();
	
	User createAnonimus(){
		return new User(0L,"","Anonymous",new TreeSet<Long>(),new TreeSet<Long>());
		//TODO: what anonymous user is? default tags?
	}

	UserManager(){
		//TODO update all users from database
	}
	
	public void setCurrentUser(long id){
		if (!mData.containsKey(id))
			currentUser = createAnonimus();
		else
			currentUser = (User) getItemById(id);
	}
	
	public Long getMyID() {
		return currentUser.getmId();
	}
	
	public User getMyData() {
		return currentUser;
	}
	
	public void syncUsers(final UiOnDone onRes, final UiOnError onErr){
		
		final List<User> res = new ArrayList<User>();
		SCAsyncRequest r = new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status == SCConnectionStatus.RESULT_OK){
					mData.clear();
					for (User h:res){
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
				res.addAll(DBManager.INSTANCE.getUsers());
						
				return null;
			}
		};
		r.run();
		return;
	}
	
	public void addNewUser(final User user, final UiOnDone uif, 
			final UiOnError uierror){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uierror.execute();
					return null;
				}
				//add the hotSpot with the new id to owned list
				mData.put(user.getmId(), user);
				uif.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				user.setmId( DBManager.INSTANCE.addUser(user).getmId());
				return null;
			}
		}.run();
	}

	public void editUser(final User user, final UiOnDone uiOnDone,
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
					DBManager.INSTANCE.updateUser(user);
					return null;
				}
			}.run();
		}

	public void removeUser(final User user, final UiOnDone uiOnDone,
			final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				Set<Long> tags = user.getmTags();
				for(Long i: tags){
					((Tag)mTagManager.getItemById(i)).removeUser(user.getmId());
				}
				Set<Long> hots = user.getmHotSpots();
				for(Long i: hots){
					((HotSpot)mHotSpotManager.getItemById(i)).leaveHotSpot(user.getmId());
				}
				mData.remove(user);
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				//TODO: this function on the server removes all tags and users
				DBManager.INSTANCE.removeUser(user);
				return null;
			}
		}.run();
	}

	

}

