package il.ac.technion.socialcampus;

import java.util.Calendar;

import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.ServerCommunication.ServerRequestManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
	
public class SociaCampusApplication extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        SociaCampusApplication.context = getApplicationContext();
        initializeCurrentUser();
        initializeDBSync();
    }


	private void initializeCurrentUser() {
		UserManager.INSTANCE.initializeUserManager();
		
	}
	

	public static Context getAppContext() {
        return SociaCampusApplication.context;
    }
	
	private void initializeDBSync(){
		final Calendar now = Calendar.getInstance();
		
		class OnDone{
			private boolean syncHotSpotsDone = false;
			private boolean syncUsersDone = false;
			private boolean syncTagsDone = false;
			
			public void errorSyncFinish(){
				finish();
			}
			public void syncHotSpotsDone(){
				syncHotSpotsDone = true;
				finishIfSynced();
			}
			public void syncUsersDone(){
				syncUsersDone = true;
				finishIfSynced();
			}
			public void syncTagsDone(){
				syncTagsDone = true;
				finishIfSynced();
			}

			private void finishIfSynced() {
				if (syncHotSpotsDone && syncUsersDone && syncTagsDone && waitIfNeeded())
					finish();
			}
			
			private void finish(){
				startActivity(new Intent(getAppContext(), MainActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}
						
			private boolean waitIfNeeded(){
				Long wait = 1500L;
				if (now.getTimeInMillis() - Calendar.getInstance().getTimeInMillis() < wait){
					try {
						Thread.sleep(wait);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		}
		
		final OnDone onDone = new OnDone();
    	
		ServerRequestManager.INSTANCE.syncHotSpots(new UiOnDone() {
			@Override
			public void execute() {
				onDone.syncHotSpotsDone();
			}
		}, new UiOnError(this){
			@Override
			public void execute(){
				onDone.errorSyncFinish();
			}
		});
    	ServerRequestManager.INSTANCE.syncUsers(new UiOnDone() {
			@Override
			public void execute() {
				onDone.syncUsersDone();
			}
		}, new UiOnError(this){
			@Override
			public void execute(){
				onDone.errorSyncFinish();
			}
		});
    	ServerRequestManager.INSTANCE.syncTags(new UiOnDone() {
			@Override
			public void execute() {
				onDone.syncTagsDone();
			}
		}, new UiOnError(this){
			@Override
			public void execute(){
				onDone.errorSyncFinish();
			}
		});
    	
    	ServerRequestManager.INSTANCE.syncRelationsTagHotSpot(null,null);
    	ServerRequestManager.INSTANCE.syncRelationsUserTag(null,null);
    	ServerRequestManager.INSTANCE.syncRelationsHotSpotUser(null,null);
    }
	
}


