package il.ac.technion.logic.ServerCommunication;

public class RequestDetailsReletionsUsetHotSpot extends RequestDetails{

	public RequestDetailsReletionsUsetHotSpot(){
		super();
		requestUrl = "/userhotspot";
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
	
	public void setParams(String strId, Long hid) {
		addRequestParameter("mUserId", strId);
		addRequestParameter("mHotspotId", String.valueOf(hid));
	}
	
	public void setParamsDelete(String strId, Long hid) {
		requestUrl = "/user/" + strId + "/hotspot/" + String.valueOf(hid); 
		//requestUrl = "/hotspot/" + String.valueOf(hid) + "/user/" + strId;
		
	}
	
	

}
