package il.ac.technion.socialcampus;

import il.ac.technion.logic.DataBase.LocalDBManager;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventTagBoardFrag extends BaseEventBoardFragment{

	
	Long TagID;
	//instance
	public static EventTagBoardFrag newInstance(Long TagID) {
		EventTagBoardFrag fragment = new EventTagBoardFrag();
		Bundle args = new Bundle();
		args.putLong("tid", TagID);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			TagID = getArguments().getLong("tid");
		}
	}

	@Override
	public String getAlias() {
		return "Tag";
	}

	@Override
	public List<Long> getHotSpotIdsToDisplay() {
		return LocalDBManager.INSTANCE.TagHotSpot.getHotSpotsFromTag(TagID);
	}
	
	@Override
	protected View inflateView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_tag_event_board, container, false);
	}

	@Override
	protected int getLinearLayoutID() {
		return R.id.infoTagBoxesHolder;
	}

	@Override
	protected String getNoHotSpotsToDisplayStr() {
		return "No upcoming events for this tag";
	}

	
}
