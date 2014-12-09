package il.ac.technion.logic.ServerCommunication;

public class RequestDetailsReletionsTagHotSpot extends RequestDetails{

	public RequestDetailsReletionsTagHotSpot(){
		super();
		requestUrl = "/taghotspot";
	}
	@Override
	public void buildeAddRequestParameter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildeRemoveRequestParameter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildeUpdateRequestParameter() {
		// TODO Auto-generated method stub
		
	}
	
	public void setParams(Long hid, Long tid) {
		addRequestParameter("mHotspotId", String.valueOf(hid));
		addRequestParameter("mTagId", String.valueOf(tid));
	}
	public void setParamsDelete(Long hid, Long tid) {
		requestUrl = "/tag/" + String.valueOf(tid) + "/hotspot/" + String.valueOf(hid); 
	}

}
