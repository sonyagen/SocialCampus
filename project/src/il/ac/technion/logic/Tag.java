package il.ac.technion.logic;
import java.util.Set;
import java.util.TreeSet;

import android.location.Location;


public class Tag {
	private Long mId;
	private String mName;
	private Set<Long> mUsers = new TreeSet<Long>();//tag ids 
	private Set<Long> mHotSpots = new TreeSet<Long>();


	public Tag(Tag hs) {
		this.mId = hs.mId;
		this.mName = hs.mName;
		this.mUsers.addAll(hs.mUsers);
		this.mHotSpots.addAll( hs.mHotSpots);
	}	

	public Tag(Long mId, String mName,
			Set<Long> mUsers, Set<Long> mSpots) {
		super();
		this.mId = mId;
		this.mName = mName;
		this.mUsers.addAll(mUsers);
		this.mHotSpots.addAll(mSpots);
	}
	
	public Tag(Long mId, String mName) {
		super();
		this.mId = mId;
		this.mName = mName;
	}

	public Tag() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Long getmId() {
		return mId;
	}

	public void setmId(Long mId) {
		this.mId = mId;
	}
	
	public Set<Long> getmUsers() {
		return mUsers;
	}

	public void setmUsers(Set<Long> mUsers) {
		this.mUsers = mUsers;
	}

	public Set<Long> getmHotSpots() {
		return mHotSpots;
	}

	public void setmHotSpots(Set<Long> mHotSpots) {
		this.mHotSpots = mHotSpots;
	}

	public Boolean IsUserJoined(Long id){
		return mHotSpots.contains(id);
	}


	public void leaveHotSpot(long hsId){
		if(mHotSpots.contains(hsId)){
			mHotSpots.remove(hsId);
		}
	}
	public void joinHotSpot(long userId){
		mHotSpots.add(userId);
	}
	
	public void removeUser(long tId){
		if(mUsers.contains(tId)){
			mUsers.remove(tId);
		}
	}
	public void addUser(long tId){
		mUsers.add(tId);
	}

}
