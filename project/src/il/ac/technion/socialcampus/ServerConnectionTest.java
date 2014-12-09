package il.ac.technion.socialcampus;

import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.HotSpot;
import il.ac.technion.logic.Objects.Tag;
import il.ac.technion.logic.Objects.User;
import il.ac.technion.logic.ServerCommunication.ServerRequestManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ServerConnectionTest extends Activity {

	Button syncUsers;
	Button syncHotSpots;
	Button syncTags;
	
	Button addUsers;
	Button addHotSpots;
	Button addTags;
	
	Button removeUsers;
	Button removeHotSpots;
	Button removeTags;
	
	Button updateUsers;
	Button updateHotSpots;
	Button updateTags;
	
	
	TextView syncUsersres;
	TextView syncHotSpotsres;
	TextView syncTagsres;
	
	TextView addUsersres;
	TextView addHotSpotsres;
	TextView addTagsres;
	
	TextView removeUsersres;
	TextView removeHotSpotsres;
	TextView removeTagsres;
	
	TextView updateUsersres;
	TextView updateHotSpotsres;
	TextView updateTagsres;
	
	Context mContext = this;
	
	User oldUser = new User("5L","https://lh3.googleusercontent.com/-47yBoS19cSw/AAAAAAAAAAI/AAAAAAAAAAA/rqg003pc3OQ/photo.jpg","Victoria Bellotti");
	User newUser = new User("123456789","https://lh3.googleusercontent.com/-47yBoS19cSw/AAAAAAAAAAI/AAAAAAAAAAA/rqg003pc3OQ/photo.jpg","Xin Song");
	HotSpot newHotSpot = new HotSpot(5L,0L,0L,"Social Campus Meeting #5",32.777929, 35.021593,"at taub 5","Social Campus Meeting #5","2L","http://img1.wikia.nocookie.net/__cb20120402214339/masseffect/images/d/db/Citadel_Space_Codex_Image.jpg");
	Tag newTag = new Tag(5L,"a new tag2");
	Tag oldTag = new Tag(6L,"Colloquium");
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_connection_test);
		
		syncUsers = (Button)findViewById(R.id.syncUsers);
		syncHotSpots = (Button)findViewById(R.id.syncHotSpots);
		syncTags = (Button)findViewById(R.id.syncTags);
		
		addUsers = (Button)findViewById(R.id.addUser);
		addHotSpots = (Button)findViewById(R.id.addHotSpot);
		addTags = (Button)findViewById(R.id.addTag);
		
		removeUsers = (Button)findViewById(R.id.removeUser);
		removeHotSpots = (Button)findViewById(R.id.removeHotSpot);
		removeTags = (Button)findViewById(R.id.removeTag);
		
		updateUsers = (Button)findViewById(R.id.updateUser);
		updateHotSpots = (Button)findViewById(R.id.updateHotSpot);
		updateTags = (Button)findViewById(R.id.updateTag);
		
		
		
		syncUsersres = (TextView)findViewById(R.id.syncUsersres);
		syncHotSpotsres = (TextView)findViewById(R.id.syncHotSpotsres);
		syncTagsres = (TextView)findViewById(R.id.syncTagsres);
		
		addUsersres = (TextView)findViewById(R.id.addUserres);
		addHotSpotsres = (TextView)findViewById(R.id.addHotSpotres);
		addTagsres = (TextView)findViewById(R.id.addTagres);
		
		removeUsersres = (TextView)findViewById(R.id.removeUserres);
		removeHotSpotsres = (TextView)findViewById(R.id.removeHotSpotres);
		removeTagsres = (TextView)findViewById(R.id.removeTagres);
		
		updateUsersres = (TextView)findViewById(R.id.updateUserres);
		updateHotSpotsres = (TextView)findViewById(R.id.updateHotSpotres);
		updateTagsres = (TextView)findViewById(R.id.updateTagres);	
		
		setFunc();

	}

	private void setFunc() {
		
		syncUsers.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.syncUsers(new UiOnDone() {	
					@Override public void execute() {
						Integer localy = LocalDBManager.INSTANCE.UserDB.getAllObjs().size();
						syncUsersres.setText("OK " + localy.toString());	}
				}, null);
			}
		});
		
		syncHotSpots.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.syncHotSpots(new UiOnDone() {	
					@Override public void execute() {
						Integer localy = LocalDBManager.INSTANCE.HotSpotDB.getAllObjs().size();
						syncHotSpotsres.setText("OK " + localy.toString());	}
				}, null);
			}
		});
		
		syncTags.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.syncTags(new UiOnDone() {	
					@Override public void execute() {
						Integer localy = LocalDBManager.INSTANCE.TagDB.getAllObjs().size();
						syncTagsres.setText("OK " + localy.toString());	}
				}, null);
			}
		});
		
		//	add	///////
		
		addUsers.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.addUser(newUser, new UiOnDone() {	
					@Override public void execute() {addUsersres.setText("OK");	}
				}, null);
			}
		});
		removeUsers.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.removeUser(oldUser, new UiOnDone() {	
					@Override public void execute() {removeUsersres.setText("OK");	}
				}, null);
			}
		});
		
		addHotSpots.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.addHotSpots(newHotSpot, new UiOnDone() {	
					@Override public void execute() {addHotSpotsres.setText("OK");	}
				}, null);
			}
		});
		removeHotSpots.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.removeHotSpots(newHotSpot, new UiOnDone() {	
					@Override public void execute() {removeHotSpotsres.setText("OK");	}
				}, null);
			}
		});
		
		addTags.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.addTag(newTag, new UiOnDone() {	
					@Override public void execute() {addTagsres.setText("OK");	}
				}, null);
			}
		});
		removeTags.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				ServerRequestManager.INSTANCE.removeTag(oldTag, new UiOnDone() {	
					@Override public void execute() {removeTagsres.setText("OK");	}
				}, null);
			}
		});
		
		////////
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server_connection_test, menu);
		return true;
	}

}
