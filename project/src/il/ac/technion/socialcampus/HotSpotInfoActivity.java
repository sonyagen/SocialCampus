package il.ac.technion.socialcampus;

import il.ac.technion.logic.UserManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class HotSpotInfoActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hot_spot_info);
		

		HotSpotDetailsFragment f = (HotSpotDetailsFragment) 
				HotSpotDetailsFragment.newInstance(getIntent().getExtras().getLong("id"));
		getSupportFragmentManager().beginTransaction().add(R.id.frame, f).commit();
	}


	@Override
	protected void onResume() {
		super.onResume();
		//((HotSpotDetailsFragment) getSupportFragmentManager().
		//findFragmentById(R.id.HotSpotInfoFrag)).refresh(getIntent().getExtras().getLong("id"));
		
	}
	
	

}
