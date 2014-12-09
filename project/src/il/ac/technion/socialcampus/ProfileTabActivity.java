package il.ac.technion.socialcampus;

import il.ac.technion.logic.UserManager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class ProfileTabActivity extends FragmentActivity implements  
	ActionBar.TabListener, TagsBoxFragment.OnTagClickListener, InfoBoxFragment.ButtonInteraction{

	private ViewPager pager;
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setResult(RESULT_OK, new Intent());
		setContentView(R.layout.activity_profile_tab);
		pager = (ViewPager) findViewById(R.id.pager);
		
		updatePager();
	}

	private void updatePager() {
		
		// Set up the action bar.
		final ActionBar actionBar =  getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//create and set adapter
		mPagerAdapter = new PagerAdapter(
				getSupportFragmentManager());
		pager.setAdapter(mPagerAdapter);
		
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		actionBar.addTab(actionBar.newTab()
				.setText("Profile")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText("Admin")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText("Joined")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText("Pinned")
				.setTabListener(this));	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_tab, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	
	public void onBackPressed() {
		mPagerAdapter.kill();
		finish();
	}

	//override interface TagBoxFragment.OnTagClickListener
	@Override
	public void onTagClick(long tid) {
		startActivity(new Intent(this,TagViewActivity.class).putExtra("id", tid));
	}

	@Override
	public String getUserId() {
		return UserManager.INSTANCE.getMyID();
	}

	@Override
	public Long getHotSpotId() {
		return null;
	}

	@Override
	public boolean isHotSpot() {
		return false;
	}

	//end interface TagBoxFragment.OnTagClickListener
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		pager.setCurrentItem(tab.getPosition());		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void joinBtnClick(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveBtnClick(Long id) {
		mPagerAdapter.refreshJoined();
	}

	@Override
	public void pinBtnClick(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unpinBtnClick(Long id) {
		mPagerAdapter.refreshPinned();
	}

	@Override
	public void shareBtnClick(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editBtnClick(Long id) {
		mPagerAdapter.refreshAdmin();
	}

	@Override
	public void discardBtnClick(Long id) {
		mPagerAdapter.refreshAdmin();
	}
	
	int EDIT = 500;
	int CREATE = 600;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//if(requestCode == EDIT || requestCode == CREATE){
			mPagerAdapter.refreshAdmin();
		//}
	}
	
	@Override
	public int getRequestCodeForEditHotSpot() {
		return EDIT;
	}

}
