package il.ac.technion.socialcampus;

import il.ac.technion.logic.Tag;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.UserManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class HotSpotInfoActivity extends FragmentActivity implements TagsBoxFragment.OnTagClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hot_spot_info);
		
		if(savedInstanceState==null){
			HotSpotDetailsFragment f = (HotSpotDetailsFragment) 
					HotSpotDetailsFragment.newInstance(getIntent().getExtras().getLong("id"));
			getSupportFragmentManager().beginTransaction().add(R.id.frame, f).commit();
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		//((HotSpotDetailsFragment) getSupportFragmentManager().
		//findFragmentById(R.id.HotSpotInfoFrag)).refresh(getIntent().getExtras().getLong("id"));
		
	}


	@Override
	public void onTagClick(long tid) {
		Tag t = TagManager.INSTANCE.getItemsbyId(tid);
		Toast.makeText(this, t.getmName(), Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub
		
	}
	
	

}
