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
	private Long mEndTime;
	private String mName;
	private double mLong;
	private double mLat;

	private String mLocation;
	private String mdescription;
	private Long mTimeZone;
	private String mAdminId;
	private String mImageURL;
	private Set<Long> mTags = new TreeSet<Long>();//tag ids 
	private Set<String> mUsers = new TreeSet<String>();	

	

	
	public HotSpot(HotSpot hs) {
		  mId = hs.mId;
		  mTime = hs.mTime;
		  mEndTime = hs.mEndTime;
		  mName = hs.mName;
		  mLong = hs.mLong;
		  mLat = hs.mLat;

		  mLocation = hs.mLocation;
		  mdescription = hs.mdescription;
		  mTimeZone = hs.mTimeZone;
		  mAdminId = hs.mAdminId;
		  mImageURL = hs.mImageURL;
		 mTags .addAll(hs.mTags);
		  mUsers .addAll(hs.mUsers);
	
	}	

	public HotSpot(Long mId, Long mTime,Long mEndTime,String mName,Double lat, Double lon ,
			String mLocation,String mdescription , Long mTimeZone ,String mAdminId,String mImageURL) {
		  this.mId = mId;
		  this.mTime = mTime;
		  this.mEndTime = mEndTime;
		  this.mName = mName;
		  this.mLong = lon;
		  this.mLat = lat;

		  this.mLocation = mLocation;
		  this.mdescription = mdescription;
		  this.mTimeZone = mTimeZone;
		  this. mAdminId = mAdminId;
		  this.mImageURL = mImageURL;
	

	}
		
	public HotSpot(Long mId, Long mTime,Long mEndTime,String mName,Double lat, Double lon ,
			String mLocation,String mdescription , Long mTimeZone ,String mAdminId,String mImageURL,
			Set<Long> mTags, Set<String> mUseres) {
		  this.mId = mId;
		  this.mTime = mTime;
		  this.mEndTime = mEndTime;
		  this.mName = mName;
		  this.mLong = lon;
		  this.mLat = lat;

		  this.mLocation = mLocation;
		  this.mdescription = mdescription;
		  this.mTimeZone = mTimeZone;
		  this. mAdminId = mAdminId;
		  this.mImageURL = mImageURL;
		this.mTags.addAll(mTags);
		this.mUsers.addAll( mUseres);


	}

	public HotSpot() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Set<String> getmUsers() {
		return mUsers;
	}

	public void setmUsers(Set<String> mUsers) {
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

	public String getmLocation() {
		return mLocation;
	}

	public void setmLocation(String mLocation) {
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

	public Set<String> getmUseres() {
		return mUsers;
	}

	public void setmUseres(Set<String> mUseres) {
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
	


	

	public void leaveHotSpot(String userId){
		if(mUsers.contains(userId)){
			mUsers.remove(userId);
		}
	}
	public void joinHotSpot(String userId){
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
