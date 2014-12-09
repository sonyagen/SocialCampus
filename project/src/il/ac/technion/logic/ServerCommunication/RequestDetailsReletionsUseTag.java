package il.ac.technion.logic.ServerCommunication;

public class RequestDetailsReletionsUseTag extends RequestDetails{

	public RequestDetailsReletionsUseTag(){
		super();
		requestUrl = "/usertag";
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
	
	public void setParams(String strId, Long tid) {
		addRequestParameter("mUserId", strId);
		addRequestParameter("mTagId", String.valueOf(tid));
		
	}
	
	public void setParamsDelete(String strId, Long tid) {
		//requestUrl = "/user/" + strId + "/tag/" + String.valueOf(tid); 
		requestUrl = "/tag/" + String.valueOf(tid) + "/user/" + strId;
	}

}
