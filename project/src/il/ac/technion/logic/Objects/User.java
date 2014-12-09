package il.ac.technion.logic.Objects;
import il.ac.technion.socialcampus.LoadProfileImage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


public class User extends TagableObject implements BitmapDisplayer,Comparable<User>{
	
	private Long mId;
	private String mStrId;
	private String mImage;
	private String mName;
	private Bitmap m_iconBitmap = null;

	public User(String mId, String mImage, String mName) {
		
		//This constractor is super Wrong!!
		//its only until Xin will seng me object with a Long id
		super(Long.parseLong(mId.substring(3), 10));
		this.mStrId = mId;
		this.mImage = mImage;
		this.mName = mName;
	}
	
	public User(Long id, String mId, String mImage, String mName) {
		super(id);
		this.mStrId = mId;
		this.mImage = mImage;
		this.mName = mName;
	}

	public User(String mId, String mImage, String mName,
			Set<Long> mTags, Set<Long> mSpots) {
		super();
		this.mStrId = mId;
		this.mImage = mImage;
		this.mName = mName;
	}

	public User() {
		super();
	}

	@Override
	public Long getId() {
		return mId;
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

	public String getStringId() {
		return mStrId;
	}

	public void setmId(String mId) {
		this.mStrId = mId;
	}
	
	public void setLongId(Long mId) {
		this.mId = mId;
	}

	public void setUserPhoto(ImageView imageViewToHostImage){
		if(mImage == null || mImage.isEmpty()){
			return;
		}
		if(m_iconBitmap == null){
			new LoadProfileImage(this, imageViewToHostImage, this.m_iconBitmap, true).execute(this.mImage);
		}else{
			imageViewToHostImage.setImageBitmap(m_iconBitmap); 
		}
	}

	@Override
	public int compareTo(User another) {
		return 0;
	}

	@Override
	public void setBitmap(Bitmap bitmap) {
		m_iconBitmap = bitmap;
	}
}
