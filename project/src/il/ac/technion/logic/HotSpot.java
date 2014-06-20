package il.ac.technion.logic;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.location.Location;


public class HotSpot {
	private Long mId;
	private Long mTime;
	private Location mLocation;
	private String mName;
	private Set<Long> mTags = new TreeSet<Long>();//tag ids 
	private Set<Long> mUsers = new TreeSet<Long>();	
	private double mLong;
	private double mLat;

	

	
	public HotSpot(HotSpot hs) {
		this.mId = hs.mId;
		this.mTime = hs.mTime;
		this.mLocation = hs.mLocation;
		this.mName = hs.mName;
		this.mTags.addAll(hs.mTags);
		this.mUsers.addAll( hs.mUsers);
		this.mLat = hs.mLat;
		this.mLong = hs.mLong;
	
	}	

	public HotSpot(Long mId, Long mTime, Location mLocation, Double lat, Double lon, String mName) {
		super();
		this.mId = mId;
		this.mTime = mTime;
		this.mLocation = mLocation;
		this.mName = mName;
		this.mLat = lat;
		this.mLong = lon;
	}
		
	public HotSpot(Long mId, Long mTime, Location mLocation, Double lat, Double lon, String mName,
			Set<Long> mTags, Set<Long> mUseres) {
		super();
		this.mId = mId;
		this.mTime = mTime;
		this.mLocation = mLocation;
		this.mName = mName;
		this.mTags.addAll(mTags);
		this.mUsers.addAll( mUseres);
		this.mLat = lat;
		this.mLong = lon;

	}

	public HotSpot() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Set<Long> getmUsers() {
		return mUsers;
	}

	public void setmUsers(Set<Long> mUsers) {
		this.mUsers = mUsers;
	}

	public double getLongt() {
		return mLong;
	}

	public void setLongt(double longt) {
		this.mLong = longt;
	}

	public double getLangt() {
		return mLat;
	}

	public void setLangt(double langt) {
		this.mLat = langt;
	}


	public Long getmId() {
		return mId;
	}

	public void setmId(Long mId) {
		this.mId = mId;
	}

	public Long getmTime() {
		return mTime;
	}

	public void setmTime(Long mTime) {
		this.mTime = mTime;
	}

	public Location getmLocation() {
		return mLocation;
	}

	public void setmLocation(Location mLocation) {
		this.mLocation = mLocation;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Set<Long> getmTags() {
		return mTags;
	}

	public void setmTags(Set<Long> mTags) {
		this.mTags = mTags;
	}

	public Set<Long> getmUseres() {
		return mUsers;
	}

	public void setmUseres(Set<Long> mUseres) {
		this.mUsers = mUseres;
	}

	


	public String getTimeStr(){
		//return (DateFormat.getTimeInstance()).format(new Date(m_orderingTime));
        return (new SimpleDateFormat("HH:mm").format(new Date(mTime)));
	}
	

	
	public Boolean IsUserJoined(Long id){
		return mUsers.contains(id);
	}

//	public String getDistance() {
//		double x = LocationManager.getLatitude() - this.m_latitude;
//		double y = LocationManager.getLongitude() - this.m_longitude;
//
//		return Integer.toString((int)Math.sqrt(x*x+y*y));
//	}
	


	

	public void leaveHotSpot(long userId){
		if(mUsers.contains(userId)){
			mUsers.remove(userId);
		}
	}
	public void joinHotSpot(long userId){
		mUsers.add(userId);
	}
	
	public void removeTag(long tId){
		if(mTags.contains(tId)){
			mTags.remove(tId);
		}
	}
	public void addTag(long tId){
		mTags.add(tId);
	}

}
