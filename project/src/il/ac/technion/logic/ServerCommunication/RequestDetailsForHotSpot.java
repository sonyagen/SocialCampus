package il.ac.technion.logic.ServerCommunication;

import il.ac.technion.logic.Objects.HotSpot;

public class RequestDetailsForHotSpot extends RequestDetails{

	private HotSpot hotspot;
	
	RequestDetailsForHotSpot(HotSpot h){
		super();
		requestUrl = "/hotspot";
		hotspot = h;
	}

	@Override
	public void buildeAddRequestParameter() {
		addRequestParameter("mTime", String.valueOf(hotspot.getmTime()));
		addRequestParameter("mEndTime", String.valueOf(hotspot.getEndTime()));
		addRequestParameter("mName",hotspot.getmName());
		addRequestParameter("mLong", String.valueOf(hotspot.getLongt()));
		addRequestParameter("mLat", String.valueOf(hotspot.getLangt()));
		addRequestParameter("mLocation", hotspot.getmLocation());
		addRequestParameter("mDescription", hotspot.getmDesc());
		addRequestParameter("mAdminId", String.valueOf(hotspot.getAdminId()));
		addRequestParameter("mImageURL", hotspot.getImageURL());
	}

	@Override
	public void buildeRemoveRequestParameter() {
		addRequestParameter("mId", String.valueOf(hotspot.getmId()));
		requestUrl += "/" + String.valueOf(hotspot.getmId());
	}
	
	@Override
	public void buildeUpdateRequestParameter() {

		requestUrl += "/" + String.valueOf(hotspot.getmId());

		addRequestParameter("mTime", String.valueOf(hotspot.getmTime()));
		addRequestParameter("mEndTime", String.valueOf(hotspot.getEndTime()));
		addRequestParameter("mName",hotspot.getmName());
		addRequestParameter("mLong", String.valueOf(hotspot.getLongt()));
		addRequestParameter("mLat", String.valueOf(hotspot.getLangt()));
		addRequestParameter("mLocation", hotspot.getmLocation());
		addRequestParameter("mDescription", hotspot.getmDesc());
		addRequestParameter("mAdminId", String.valueOf(hotspot.getAdminId()));
		addRequestParameter("mImageURL", hotspot.getImageURL());
		
	}

}
