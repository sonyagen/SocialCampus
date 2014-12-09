package il.ac.technion.socialcampus;

import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.ServerCommunication.ServerRequestManager;
import android.app.Application;
import android.content.Context;
	
public class MyApplication extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
        initializeCurrentUser();
        initializeDBSync();
    }


	private void initializeCurrentUser() {
		UserManager.INSTANCE.initializeUserManager();
		
	}
	
    private void initializeDBSync() {
    	//syncDb();
		
	}

	public static Context getAppContext() {
        return MyApplication.context;
    }
	
	private void syncDb(){
    	
    	ServerRequestManager.INSTANCE.syncHotSpots(new UiOnDone() {
			@Override
			public void execute() {
//				setUpMapIfNeeded();
			}
		}, new UiOnError(this));
    	ServerRequestManager.INSTANCE.syncUsers(new UiOnDone() {
			@Override
			public void execute() {
				
//				UserManager.INSTANCE.setCurrentUser(
//						UserManager.getLoggedInStrId(getApplicationContext() )
//				);
			}
		}, new UiOnError(this));
    	ServerRequestManager.INSTANCE.syncTags(new UiOnDone() {
			@Override
			public void execute() {}
		}, new UiOnError(this));
    }
	
}


