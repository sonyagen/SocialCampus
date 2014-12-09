package il.ac.technion.socialcampus;

import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link UserListFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link UserListFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class UserListFragment extends Fragment {

	Long HotSpotId;
	Boolean isHotSpot;
	
	public static UserListFragment newInstance(Long hId, Boolean isHotSpot) {
		UserListFragment fragment = new UserListFragment();
		Bundle args = new Bundle();
		args.putLong("hid", hId);
		args.putBoolean("type", isHotSpot);
		fragment.setArguments(args);
		return fragment;
	}

	public UserListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			HotSpotId = getArguments().getLong("hid");
			isHotSpot = getArguments().getBoolean("type");
		}
	}

	UserListAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_user_list, container, false);
		ListView lv = (ListView)v.findViewById(R.id.userListView);
		
		List<Long> Ids = new ArrayList<Long>();
		if(isHotSpot){
			List<String> s = LocalDBManager.INSTANCE.UserHotSpot.getUsersFromHotSpot(HotSpotId);
			
			for(String strId : s){
				Ids.add(LocalDBManager.INSTANCE.UserDB.getItemById(strId).getId());
			}
		}else{
			Long TagId = HotSpotId;
			List<String> s = LocalDBManager.INSTANCE.UserTag.getUsersFromTag(TagId);
			for(String strId : s){
				Ids.add(LocalDBManager.INSTANCE.UserDB.getItemById(strId).getId());
			}
		}
		ArrayList<User> objects = (ArrayList<User>) LocalDBManager.INSTANCE.UserDB.getItemsbyIds(Ids);
		//ArrayList<User> objects = new ArrayList<User>();
		//objects.addAll(u);
		
		adapter = new UserListAdapter(getActivity(), R.layout.tags_view_row, objects);
		if (lv!=null) lv.setAdapter(adapter);
		
		return v;
	}


}
