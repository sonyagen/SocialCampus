package il.ac.technion.logic.ServerCommunication;

import il.ac.technion.logic.Objects.User;

public class RequestDetailsForUser extends RequestDetails{

	private User user;
	
	RequestDetailsForUser(User u){
		super();
		requestUrl = "/user";
		user = u;
	}

	@Override
	public void buildeAddRequestParameter() {
		addRequestParameter("mStrId", user.getStringId());
		addRequestParameter("mName", user.getmName());
		addRequestParameter("mImage", user.getmImage());		
	}

	@Override
	public void buildeRemoveRequestParameter() {
		addRequestParameter("mId", user.getStringId());
		requestUrl += "/" + String.valueOf( user.getId());
	}
	
	@Override
	public void buildeUpdateRequestParameter() {
		addRequestParameter("mId", user.getStringId());
		addRequestParameter("mName", user.getmName());
		addRequestParameter("mImage", user.getmImage());
	}
	
}