package il.ac.technion.socialcampus;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class TagViewActivity extends FragmentActivity implements ActionBar.TabListener, InfoBoxFragment.ButtonInteraction{

	private ViewPager pager;
	private TagPagerAdapter mPagerAdapter;
	Long tId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_view);
		pager = (ViewPager) findViewById(R.id.pager);
		tId = (Long)getIntent().getExtras().get("id");
		updatePager();
	}
	
	private void updatePager(){
		final ActionBar actionBar =  getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mPagerAdapter = new TagPagerAdapter();
		pager.setAdapter(mPagerAdapter);
		
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		actionBar.addTab(actionBar.newTab()
				.setText("People")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText("Events")
				.setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_view, menu);
		return true;
	}
	
	class TagPagerAdapter extends FragmentPagerAdapter {
		private final List<Fragment> fragments = getFragments();
		 
		private List<Fragment> getFragments() {
			UserListFragment userDetailsFrag = UserListFragment.newInstance(tId,false);
			EventTagBoardFrag evntTagFrag = EventTagBoardFrag.newInstance(tId);

			List<Fragment> fl = new ArrayList<Fragment>();
			fl.add(userDetailsFrag);
			fl.add(evntTagFrag);

			return fl;
		}
	  
		public TagPagerAdapter() {
			super(getSupportFragmentManager());
		}
	  
		@Override
		public Fragment getItem(final int position) {
			return fragments.get(position);
		}
		  
		@Override 
		public int getCount() {
			return fragments.size();
		}
		
		//refresh
		public void refresh(){
			((EventTagBoardFrag)fragments.get(1)).refreshFragView();
		}
		
		public void kill() {
			((SmartDestroyFragment)fragments.get(1)).KillBeforeActivityIsDestroyed();
		}
		
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void joinBtnClick(Long id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void leaveBtnClick(Long id) {
		
	}
	@Override
	public void pinBtnClick(Long id) {
		
		
	}
	@Override
	public void unpinBtnClick(Long id) {
		
	}
	@Override
	public void shareBtnClick(Long id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void editBtnClick(Long id) {
		mPagerAdapter.refresh();
	}
	@Override
	public void discardBtnClick(Long id) {
		mPagerAdapter.refresh();
	}
	
	int EDIT = 500;
	int CREATE = 600;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == EDIT ){
			mPagerAdapter.refresh();
		}
	}
	
	@Override
	public int getRequestCodeForEditHotSpot() {
		return EDIT;
	}

	public void onBackPressed() {
		mPagerAdapter.kill();
		finish();
	}
}
