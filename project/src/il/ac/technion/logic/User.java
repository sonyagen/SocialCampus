package il.ac.technion.logic;
import java.util.Set;
import java.util.TreeSet;

import android.R.bool;
import android.location.Location;


public class User {
	private Long mId;
	private String mImage;
	private String mName;
	private Set<Long> mTags = new TreeSet<Long>();//tag ids 
	private Set<Long> mHotSpots = new TreeSet<Long>();
	private Set<Long> mPinnedSpots = new TreeSet<Long>();


	public User(User hs) {
		this.mId = hs.mId;
		this.mName = hs.mName;
		this.mTags.addAll(hs.mTags);
		this.mHotSpots.addAll( hs.mHotSpots);
	
	}	

	public User(Long mId, String mImage, String mName) {
		super();
		this.mId = mId;
		this.mImage = mImage;
		this.mName = mName;
	}

	public User(Long mId, String mImage, String mName,
			Set<Long> mTags, Set<Long> mSpots) {
		super();
		this.mId = mId;
		this.mImage = mImage;
		this.mName = mName;
		this.mTags.addAll(mTags);
		this.mHotSpots.addAll( mSpots);
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getmImage() {
		return mImage;
	}

	public void setmImage(String mIm) {
		this.mImage = mIm;
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
	
	public Set<Long> getmTags() {
		return mTags;
	}

	public void setmTags(Set<Long> mTags) {
		this.mTags = mTags;
	}

	public Set<Long> getmHotSpots() {
		return mHotSpots;
	}

	public void setmHotSpots(Set<Long> HotSpots) {
		this.mHotSpots = HotSpots;
	}

	public Boolean IsUserJoined(Long id){
		return mHotSpots.contains(id);
	}


	public void leaveHotSpot(long hotSpotId){
		if(mHotSpots.contains(hotSpotId)){
			mHotSpots.remove(hotSpotId);
		}
	}
	public void joinHotSpot(long hotSpotId){
		mHotSpots.add(hotSpotId);
	}
	
	public void removeTag(long tId){
		if(mTags.contains(tId)){
			mTags.remove(tId);
		}
	}
	public void addTag(long tId){
		mTags.add(tId);
	}
	
	public Set<Long> getmPinnedSpots() {
		return mPinnedSpots;
	}

	public void setmPinnedSpots(Set<Long> mPinnedSpots) {
		this.mPinnedSpots = mPinnedSpots;
	}

	public boolean isJoined(long hid){
		 for(Long h: mHotSpots){
			 if(h.equals(hid));
			 return true;
		 }
		 return false;
	}
	
	public boolean isPinned(long hid){
		 for(Long h: mPinnedSpots){
			 if(h.equals(hid));
			 return true;
		 }
		 return false;
	}
}
