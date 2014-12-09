package il.ac.technion.socialcampus;

import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventPinnedBoardFrag extends BaseEventBoardFragment{

	@Override
	public String getAlias() {
		return "Pinned";
	}

	@Override
	public List<Long> getHotSpotIdsToDisplay() {
		return  UserManager.INSTANCE.getPinned();
	}

	
	@Override
	protected View inflateView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_pinned_event_board, container, false);
	}

	@Override
	protected int getLinearLayoutID() {
		return R.id.infoPinnedBoxesHolder;
	}
	
	@Override
	protected String getNoHotSpotsToDisplayStr() {
		return "You haven't pinned any events to your board";
	}
}
