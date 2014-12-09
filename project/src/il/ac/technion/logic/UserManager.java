package il.ac.technion.logic;

import il.ac.technion.logic.DataBase.DBNotSynced;
import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.User;
import il.ac.technion.logic.ServerCommunication.ServerRequestManager;
import il.ac.technion.socialcampus.MyApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/***
 * 
 * @author Sofia Gendelman
 *
 * @precondition
 * this is a static object. however, to be properly initialized the object must have
 * app's context. since this object is static it will be initialized before the context 
 * can be reached.
 * to correct this make sure to call initializeUserManager() before using the object.
 * 
 * make sure initializeUserManager() is called from 
 * SocialCampusApp.onCreate();
 * 
 */

public enum UserManager{
	
	INSTANCE();
	
	public static final String anonymousUID = "";
	private static User currentUser = createAnonymous();
	private static Set<String> pinnedIds = new HashSet<String>();
	
	private static User createAnonymous(){
		return new User(-1L,anonymousUID,"","Anonymous");
	}
	
	private boolean isSavedInPrefs(Context appContext){
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		return !prefs.getString("il.ac.technion.socialcampus.LoggedIn",UserManager.anonymousUID)
				.equals(UserManager.anonymousUID);
	}

	public void initializeUserManager(){
		if(isSavedInPrefs(MyApplication.getAppContext())){
			
			//TODO: when you have a local DB, save in prefs only the Long id and get 
			//the object from DB instead of creating new instance
			
			// when local DB done syncing, rebuild this object in case user was revoked or the
			//name or picture was edited.
		
			SharedPreferences prefs = MyApplication.getAppContext().getSharedPreferences(
					"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
			Long lId = prefs.getLong("il.ac.technion.socialcampus.LoggedInLongId",-1L);
			String id = prefs.getString("il.ac.technion.socialcampus.LoggedIn",anonymousUID);
			String name = prefs.getString("il.ac.technion.socialcampus.LoggedInName","");
			String image = prefs.getString("il.ac.technion.socialcampus.LoggedInImage","");
			
			currentUser = new User(lId,id,image,name);
			
			pinnedIds = prefs.getStringSet("il.ac.technion.socialcampus.PinnedHotSpotIDs", new HashSet<String>());
		}
	}
	
	public String getMyID() {
		return currentUser.getStringId();
	}
	
	public Long getMyLongID() {
		return currentUser.getId();
	}


	public User getMyData() {
		return currentUser;
	}
	
	public boolean isLoggedIn(){
		return !currentUser.getStringId().equals(anonymousUID);
	}
	
	//TODO- works fine only if DB was synced or if we have a local DB
	//better way is to ask the server DB if this one exists.
	//if there is no connection to the server relay on info in local DB.
	public boolean isRegistered(String uid) throws DBNotSynced {
		if(!LocalDBManager.INSTANCE.UserDB.isSyncDone()){
			throw new DBNotSynced();
		}
		return LocalDBManager.INSTANCE.UserDB.getItemById(uid) != null;
	}
	
	
	public void loginUser(final User u, final UiOnDone uiOnDone, UiOnError uiOnError) {
		boolean registrated;
		
		u.setmId("1234567891");
		
		
		try {
			registrated = UserManager.INSTANCE.isRegistered(u.getStringId());
		} catch (DBNotSynced e) {
			e.printStackTrace();
			Toast.makeText(MyApplication.getAppContext(), "slow connection. please try again", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(registrated){
			User nu = LocalDBManager.INSTANCE.UserDB.getItemById(u.getStringId());
			setLoggedIn(nu);
			uiOnDone.execute();
		}
		else{
			ServerRequestManager.INSTANCE.addUser(u,new UiOnDone() {
				
				@Override
				public void execute() {
					User nu = LocalDBManager.INSTANCE.UserDB.getItemById(u.getStringId());
					setLoggedIn(nu);
					uiOnDone.execute();					
				}
			},uiOnError);
		}
		
	}
	
	private static void setLoggedIn(User u){
		Context appContext = MyApplication.getAppContext();
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		prefs.edit().putLong("il.ac.technion.socialcampus.LoggedInLongId", u.getId());
		prefs.edit().putString("il.ac.technion.socialcampus.LoggedIn", u.getStringId()).commit();
		prefs.edit().putString("il.ac.technion.socialcampus.LoggedInImage", u.getmImage()).commit();
		prefs.edit().putString("il.ac.technion.socialcampus.LoggedInName", u.getmName()).commit();
		
		currentUser = u;
	}

	public void logout() {
		
		Context appContext = MyApplication.getAppContext();
		currentUser = createAnonymous();
		
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		prefs.edit().remove("il.ac.technion.socialcampus.LoggedInLongId").commit();
		prefs.edit().remove("il.ac.technion.socialcampus.LoggedIn").commit();
		prefs.edit().remove("il.ac.technion.socialcampus.LoggedInImage").commit();
		prefs.edit().remove("il.ac.technion.socialcampus.LoggedInName").commit();
	}

	
	
	
	public void pinHotSpot(Long hId){
	
		Context appContext = MyApplication.getAppContext();
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		
		String strid = String.valueOf(hId);
		pinnedIds.add(strid);
		prefs.edit().putStringSet("il.ac.technion.socialcampus.PinnedHotSpotIDs", pinnedIds);
	}
	
	public void unPinHotSpot(Long hId){
		Context appContext = MyApplication.getAppContext();
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		
		String strid = String.valueOf(hId);
		pinnedIds.remove(strid);
		prefs.edit().putStringSet("il.ac.technion.socialcampus.PinnedHotSpotIDs", pinnedIds);
	}
	
	public List<Long> getPinned(){
		List<Long> res = new  ArrayList<Long>();
		for(String s : pinnedIds){
			res.add(Long.getLong(s));
		}
		return res;
	}

	public boolean isPinned(Long hid){
		
		for(Long h: getPinned()){
			if(h == hid){
				return true;
			}
		}
		return false;
	}
}

