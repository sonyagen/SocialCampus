package il.ac.technion.socialcampus;

import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {
	private final List<Fragment> fragments = getFragments();
 
	private List<Fragment> getFragments() {
		ProfileDetailsFragment userDetailsFrag = ProfileDetailsFragment.
				newInstance(UserManager.INSTANCE.getMyID());

		List<Fragment> fl = new ArrayList<Fragment>();
		fl.add(userDetailsFrag);
		fl.add(new EventAdminBoardFrag());
		fl.add(new EventJoinedBoardFrag());
		fl.add(new EventPinnedBoardFrag());

		return fl;
	}
  
	public PagerAdapter(final FragmentManager fm) {
		super(fm);
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
	public void refreshAdmin(){
		((EventAdminBoardFrag)fragments.get(1)).refreshFragView();
	}
	
	public void refreshJoined(){
		((EventJoinedBoardFrag)fragments.get(2)).refreshFragView();
	}
	
	public void refreshPinned(){
		((EventPinnedBoardFrag)fragments.get(3)).refreshFragView();
	}
	
 	public void kill() {
		
		for(Fragment f : fragments){
			((SmartDestroyFragment)f).KillBeforeActivityIsDestroyed();
		}
	}
	  
}
