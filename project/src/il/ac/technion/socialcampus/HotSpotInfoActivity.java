package il.ac.technion.socialcampus;

import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.Tag;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class HotSpotInfoActivity extends FragmentActivity 
implements TagsBoxFragment.OnTagClickListener, HotSpotDetailsFragment.ButtonInteraction{

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

		setResult(RESULT_OK, new Intent());
		
		((HotSpotDetailsFragment)getSupportFragmentManager().
				findFragmentById(R.id.frame)).resetInfoBox();
		
	}


	@Override
	public void onTagClick(long tid) {
//		Tag t = LocalDBManager.INSTANCE.TagDB.getItemById(tid);
//		Toast.makeText(this, t.getmName(), Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this,TagViewActivity.class).putExtra("id", tid));
	}


	@Override
	public void joinBtnClick(Long id) {
		// TODO Auto-generated method stub	
	}


	@Override
	public void leaveBtnClick(Long id) {
		// TODO Auto-generated method stub
	}


	@Override
	public void pinBtnClick(Long id) {
		// TODO Auto-generated method stub
	}


	@Override
	public void unpinBtnClick(Long id) {
		// TODO Auto-generated method stub
	}


	@Override
	public void shareBtnClick(Long id) {
		// TODO Auto-generated method stub
	}


	@Override
	public void editBtnClick(Long id) {
		// TODO Auto-generated method stub
	}


	@Override
	public void discardBtnClick(Long id) {
		// TODO Auto-generated method stub
		finish();
	}


	@Override
	public boolean isHotSpot() {
		return true;
	}


	@Override
	public String getUserId() {
		return null;
	}


	@Override
	public Long getHotSpotId() {
		return getIntent().getExtras().getLong("id");
	}


	
	int EDIT = 500;
	@Override
	public int getRequestCodeForEditHotSpot() {
		return EDIT;
	}
	
	

}
