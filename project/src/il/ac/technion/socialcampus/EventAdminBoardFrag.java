package il.ac.technion.socialcampus;

import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class EventAdminBoardFrag extends BaseEventBoardFragment{

	private ImageButton createEventBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = super.onCreateView(inflater, container, savedInstanceState);
		createEventBtn = (ImageButton) v.findViewById(R.id.createEventBtn);
		createEventBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(),CreateNewHotSpotActivity.class), 600);
			}
		});
		
		return v;
	}

	
	@Override
	public String getAlias() {
		return "Admin";
	}

	@Override
	public List<Long> getHotSpotIdsToDisplay() {
		String myId = UserManager.INSTANCE.getMyID();
		return LocalDBManager.INSTANCE.UserHotSpot.getHotSpotsAdminedByUser(myId);
	}
	
	@Override
	protected View inflateView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_admin_event_board, container, false);
	}

	@Override
	protected int getLinearLayoutID() {
		return R.id.infoAdminBoxesHolder;
	}

	@Override
	protected String getNoHotSpotsToDisplayStr() {
		return "You're not hosting any events\n Craet a new event";
	}

	
}
