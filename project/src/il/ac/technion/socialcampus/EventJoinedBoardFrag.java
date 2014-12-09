package il.ac.technion.socialcampus;

import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventJoinedBoardFrag extends BaseEventBoardFragment{

	@Override
	public String getAlias() {
		return "Joined";
	}

	@Override
	public List<Long> getHotSpotIdsToDisplay() {
		String myId = UserManager.INSTANCE.getMyID();
		return LocalDBManager.INSTANCE.UserHotSpot.getHotSpotsNOTAdminedByUser(myId);
	}

	
	@Override
	protected View inflateView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_joined_event_board, container, false);
	}
	
	@Override
	protected int getLinearLayoutID() {
		return R.id.infoJoinedBoxesHolder;
	}

	@Override
	protected String getNoHotSpotsToDisplayStr() {
		return "You're not going any events\n Browes the evnets availble and join!";
	}
}
