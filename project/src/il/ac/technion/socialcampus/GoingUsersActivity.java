package il.ac.technion.socialcampus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class GoingUsersActivity extends FragmentActivity {

	UserListAdapter adapter;
	ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_going_users);
		
		if(savedInstanceState==null){
			Long HotSpotId = (Long)getIntent().getExtras().get("id");
			UserListFragment f = (UserListFragment) 
					UserListFragment.newInstance(HotSpotId,true);
			
			getSupportFragmentManager().beginTransaction().add(R.id.frame, f).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.going_users, menu);
		return true;
	}

}
