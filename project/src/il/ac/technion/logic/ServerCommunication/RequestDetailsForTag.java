package il.ac.technion.logic.ServerCommunication;

import il.ac.technion.logic.Objects.Tag;

public class RequestDetailsForTag extends RequestDetails{

	Tag tag;
	
	RequestDetailsForTag(Tag t){
		super();
		requestUrl = "/tag";
		tag = t;
	}

	@Override
	public void buildeAddRequestParameter() {
		addRequestParameter("mId", String.valueOf(tag.getmId()));
		addRequestParameter("mName", tag.getmName());
		addRequestParameter("mTime", String.valueOf(tag.getmTime()));
	}

	@Override
	public void buildeRemoveRequestParameter() {
		requestUrl += "/" + String.valueOf(tag.getmId());
	}
	
	@Override
	public void buildeUpdateRequestParameter() {
		addRequestParameter("mId", String.valueOf(tag.getmId()));
		addRequestParameter("mName", tag.getmName());
		addRequestParameter("mTime", String.valueOf(tag.getmTime()));
	}

}
