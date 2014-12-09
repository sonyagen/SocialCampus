package il.ac.technion.logic;

import il.ac.technion.logic.Objects.HotSpot;
import il.ac.technion.logic.Objects.Tag;
import il.ac.technion.logic.Objects.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;

/**
 * 
 * This singleton supply implementation for the LetsEatHotSpot, ILetsEatRestaurant,
 *	ILetsEatUser interfaces  these functions must be called in the back ground by using service or Asyntasck.
 *
 */
public enum DBManager  {
	INSTANCE;

//	public User getSonya(){
//		return new User("103967014019877216822","https://lh6.googleusercontent.com/-ajL_apwzsJ4/AAAAAAAAAAI/AAAAAAAADK0/RmYXAMzxYOo/photo.jpg?sz=50","Sonya Gendelman");
//	}
//	private SCReturnCode resCode = SCReturnCode.SUCCESS;
//	public SCReturnCode getResult(){
//		return resCode;
//	}

	ArrayList<HotSpot> HS = new ArrayList<HotSpot>();
	ArrayList<User> USR = new ArrayList<User>();
	ArrayList<Tag> TAG = new ArrayList<Tag>();

	DBManager(){
		

	}
	//get by id
	private Tag getTag(Long id){
		for(Tag t:TAG){
			if (t.getmId().equals(id))
				return t;
		}
		return null;
	}
	private User getUser(String id){
		for(User t:USR){
			if (t.getStringId().equals(id))
				return t;
		}
		return null;
	}
	private HotSpot getSpot(Long id){
		for(HotSpot t:HS){
			if (t.getmId().equals(id))
				return t;
		}
		return null;
	}

	//sync
//	public List<HotSpot> getAllHotSpots() {
//		List<HotSpot>  list = new LinkedList<HotSpot>();
//		try {
//			APIRequest req = new APIRequest();
//			req.setRequestType(RequestType.GET);
//			req.setRequestUrl("/hotspot");
//			String str =  Communicator.execute(req);
//
//		
//			Type listType = new TypeToken<List<HotSpot>>() {}.getType();
//			 list = new Gson().fromJson(str, listType);
//			
//			if (list == null){
//				resCode =  SCReturnCode.FAILURE;
//			}else{
//				resCode = SCReturnCode.SUCCESS;
//			}
//
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		} catch(Exception e){
//			Log.e(null, "failureeeeee");
//		}
//
//		return list;
//	}
//	List<HotSpot> getHotSpotsByRadios(double latitude,double lontitude, double radios){
//		return HS;
//	}
//	
//	List<User> getUsers(){
//		List<User>  list = new LinkedList<User>();
//		try {
//			APIRequest req = new APIRequest();
//			req.setRequestType(RequestType.GET);
//			req.setRequestUrl("/user");
//			String str =  Communicator.execute(req);
//
//		
//			Type listType = new TypeToken<List<User>>() {}.getType();
//			 list = new Gson().fromJson(str, listType);
//			
//			if (list == null){
//				resCode =  SCReturnCode.FAILURE;
//			}else{
//				resCode = SCReturnCode.SUCCESS;
//			}
//
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		} catch(Exception e){
//			Log.e(null, "failureeeeee");
//		}
//
//		return list;
//	}
//	
//	List<Tag> getTags(){
//		List<Tag>  list = new LinkedList<Tag>();
//		try {
//			APIRequest req = new APIRequest();
//			req.setRequestType(RequestType.GET);
//			req.setRequestUrl("/tag");
//			String str =  Communicator.execute(req);
//
//		
//			Type listType = new TypeToken<List<Tag>>() {}.getType();
//			 list = new Gson().fromJson(str, listType);
//			
//			if (list == null){
//				resCode =  SCReturnCode.FAILURE;
//			}else{
//				resCode = SCReturnCode.SUCCESS;
//			}
//
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		} catch(Exception e){
//			Log.e(null, "failureeeeee");
//		}
//
//		return list;
//	}
//
//	//add
//	Tag addTag(Tag tag){
//		TAG.add(tag);
//		return tag;
//	}
//	HotSpot addHotSpot(HotSpot hotspot){
//		HotSpot h = new HotSpot();
//		try {
//			APIRequest req = new APIRequest();
//			req.addRequestParameter("mTime", String.valueOf(hotspot.getmTime()));
//			req.addRequestParameter("mEndTime", String.valueOf(hotspot.getEndTime()));
//			req.addRequestParameter("mName",hotspot.getmName());
//			req.addRequestParameter("mLong", String.valueOf(hotspot.getLongt()));
//			req.addRequestParameter("mLat", String.valueOf(hotspot.getLangt()));
//			req.addRequestParameter("mLocation", hotspot.getmLocation());
//			req.addRequestParameter("mDescription", hotspot.getmDesc());
//			req.addRequestParameter("mAdminId", String.valueOf(hotspot.getAdminId()));
//			req.addRequestParameter("mImageURL", hotspot.getImageURL());
//			
//			req.setRequestType(RequestType.POST);
//			
//			req.setRequestUrl("/hotspot");
//			String str = Communicator.execute(req);
//			h = new Gson().fromJson(str, HotSpot.class);
//			if (h == null){
//				resCode =  SCReturnCode.FAILURE;
//			}else{
//				resCode = SCReturnCode.SUCCESS;
//			}
//
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		}
//
//		return h;
//	}
//	//TODO:
//	public User addUser(User user) {
//		User u = new User();
//		try {
//			APIRequest req = new APIRequest();
//			req.addRequestParameter("mId", user.getmId());
//			req.addRequestParameter("mName", user.getmName());
//			req.addRequestParameter("mImage", user.getmImage());
//			req.setRequestType(RequestType.POST);
//			
//			req.setRequestUrl("/user");
//			String str = Communicator.execute(req);
//			u = new Gson().fromJson(str, User.class);
//			if (u == null){
//				resCode =  SCReturnCode.FAILURE;
//			}else{
//				resCode = SCReturnCode.SUCCESS;
//			}
//
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		}
//
//		return u;
//	}
//
//	//remove
//	void removeTag(Tag tag){
//		TAG.remove(tag);
//	}
//	void removeHotSpot(HotSpot h){
//		HS.remove(h);
//	}
//	void removeUser(User u){
//		USR.remove(u);
//	}
//
//	//update
//	void updateHotSpot(HotSpot hotSpot){
//
//	}
//	void updateTag(Tag t){
//
//	}
//	void updateUser(User u){
//
//	}

//	//join/break 
//	void breakUserHotSpot(Long hid, String uid){
//		getSpot(hid).getmUsers().remove(uid);
//		getUser(uid).getmHotSpots().remove(hid);
//	}
//	void joinUserHotSpot(Long hid, String uid){
//		getSpot(hid).getmUsers().add(uid);
//		getUser(uid).getmHotSpots().add(hid);
//	}
//
//	void breakUserTag(String uid, Long tid){
//		getTag(tid).getmUsers().remove(uid);
//		getUser(uid).getmTags().remove(tid);
//	}
//	void joinUserTag(String uid, Long tid){
//		getTag(tid).getmUsers().add(uid);
//		getUser(uid).getmTags().add(tid);
//	}
//
//	void breakSpotTag(Long hid, Long tid){
//		getSpot(hid).getmTags().remove(tid);
//		getTag(tid).getmHotSpots().remove(hid);
//	}
//	void joinSpotTag(Long hid, Long tid){
//		getSpot(hid).getmTags().add(tid);
//		getTag(tid).getmHotSpots().add(hid);
//	}



	////////////////////////////////	
	// 	Old file
	////////////////////////////////
	//
	//	private final String servletName = "LetsEatServlet";
	//	private final String operation = "function";
	//	private SCReturnCode resCode ;
	//
	//	public SCReturnCode getResult(){
	//		return resCode;
	//	}
	//
	//	
	//	public User addUser(User user) {
	//		User u = null;
	//		try {
	//			u = new Gson().fromJson(Communicator.execute(servletName,
	//					operation, LetsEatFunctions.ADD_USER.toString(), "user",
	//					new Gson().toJson(user)), User.class);
	//			if (u == null){
	//				resCode =  SCReturnCode.FAILURE;
	//			}else{
	//				resCode = SCReturnCode.SUCCESS;
	//			}
	//
	//		} catch (JsonSyntaxException e) {
	//			resCode = SCReturnCode.BAD_PARAM;
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			resCode = SCReturnCode.BAD_CONNECTION;
	//			e.printStackTrace();
	//		}
	//	
	//		return u;
	//	}
	//
	//
	//	
	//	public User updateUser(User user) {
	//		User u = null;
	//		try {
	//			u = new Gson().fromJson(Communicator.execute(servletName,
	//					operation, LetsEatFunctions.UPDATE_USER.toString(), "user",
	//					new Gson().toJson(user)), User.class);
	//			if (u == null){
	//				resCode =  SCReturnCode.FAILURE;
	//			} else{
	//				resCode = SCReturnCode.SUCCESS;
	//			}
	//
	//		} catch (JsonSyntaxException e) {
	//			resCode = SCReturnCode.BAD_PARAM;
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			resCode = SCReturnCode.BAD_CONNECTION;
	//			e.printStackTrace();
	//		}
	//		
	//		return u;
	//	}
	//
	//	
	////	public SCReturnCode removeUser(Long userID) {
	////		try {
	////			return new Gson().fromJson(Communicator.execute(servletName,
	////					operation, LetsEatFunctions.REMOVE_USER.toString(), "userID",
	////					new Gson().toJson(userID)), SCReturnCode.class);
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return resCode;
	////
	////	}
	//
	//	
	////	public User getUser(Long userID) {
	////		User u = null;
	////		try {
	////			u = new Gson().fromJson(Communicator.execute(servletName, "query",
	////			        "yes", operation, LetsEatFunctions.GET_USER.toString(), "userID",
	////					new Gson().toJson(userID)), User.class);
	////			if (u == null){
	////				resCode =  SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return u;
	////	}
	//
	//	
	////	public SCReturnCode updateUserLocation(Long userID, Double latitude,
	////			Double longitude, Long timeStamp) {
	////		try{
	////			return new Gson().fromJson(Communicator.execute(servletName,
	////					operation, LetsEatFunctions.UPDATE_USER_LOCATION.toString(), 
	////					"userID", new Gson().toJson(userID),
	////					"latitude", new Gson().toJson(latitude),
	////					"longitude", new Gson().toJson(longitude),
	////					"timeStamp", new Gson().toJson(timeStamp)), SCReturnCode.class);
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return resCode;
	////	}
	//
	//	
	////	public List<User> getUsersByRadios(Double latitude, Double longitude,
	////			Double radios) {
	////		List<User> l = null;
	////		try {
	////			l =  new Gson().fromJson(Communicator.execute(servletName, "query",
	////				        "yes", operation, LetsEatFunctions.GET_USERS_BY_RADIOS.toString(),
	////				        "latitude", new Gson().toJson(latitude),
	////				        "longitude", new Gson().toJson(longitude),
	////				        "radios", new Gson().toJson(radios)), new TypeToken<List<User>>() {}.getType());
	////			if (l == null){
	////				resCode =  SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////			
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return l;
	////		
	////	}
	//	
	////	public Restaurant addRestaurant(Restaurant restaurant) {
	////		Restaurant r = null;
	////		try {
	////			r = new Gson().fromJson(Communicator.execute(servletName,
	////					operation, LetsEatFunctions.ADD_RESTAURANT.toString(), "restaurant",
	////			        new Gson().toJson(restaurant)), Restaurant.class);
	////			if (r == null){
	////				resCode= SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////
	////		} catch (JsonSyntaxException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_PARAM;
	////		} catch (IOException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_CONNECTION;
	////		}
	////		return r;
	////	}
	//
	//
	//	
	////	public Restaurant updateRestaurant(Restaurant restaurant) {
	////		Restaurant r = null;
	////		try {
	////			r = new Gson().fromJson(Communicator.execute(servletName,
	////					operation, LetsEatFunctions.UPDATE_RESTAURANT.toString(), "restaurant",
	////			        new Gson().toJson(restaurant)), Restaurant.class);
	////			if (r == null){
	////				resCode= SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////
	////		} catch (JsonSyntaxException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_PARAM;
	////		} catch (IOException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_CONNECTION;
	////		}
	////		return r;
	////	}
	//
	//	
	////	public SCReturnCode removeRestaurant(Long restaurantID) {
	////		try {
	////			return new Gson().fromJson(Communicator.execute(servletName,
	////					operation, LetsEatFunctions.REMOVE_RESTAURANT.toString(), "restaurantID",
	////			        new Gson().toJson(restaurantID)), SCReturnCode.class);
	////
	////		} catch (JsonSyntaxException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_PARAM;
	////		} catch (IOException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_CONNECTION;
	////		}
	////		return resCode;
	////	}
	//
	//	
	////	public Restaurant getRestaurant(Long restaurantID) {
	////		Restaurant r = null;
	////		try {
	////			r = new Gson().fromJson(Communicator.execute(servletName, "query",
	////			        "yes", operation, LetsEatFunctions.GET_RESTAURANT.toString(), "restaurantID",
	////					new Gson().toJson(restaurantID)), Restaurant.class);
	////			if (r == null){
	////				resCode =  SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return r;
	////	}
	//
	//	
	////	public HotSpot addHotSpot(HotSpot delivery) {
	////		HotSpot d = null;
	////		try {
	////			d = new Gson().fromJson(Communicator.execute(servletName,
	////					operation, LetsEatFunctions.ADD_DELIVERY.toString(), "delivery",
	////			        new Gson().toJson(delivery)), HotSpot.class);
	////			if (d == null){
	////				resCode= SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////
	////		} catch (JsonSyntaxException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_PARAM;
	////		} catch (IOException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_CONNECTION;
	////		}
	////		return d;
	////	}
	//
	//	
	////	public HotSpot updateHotSpot(HotSpot delivery) {
	////		HotSpot d = null;
	////		try {
	////			d = new Gson().fromJson(Communicator.execute(servletName,
	////					operation, LetsEatFunctions.UPDATE_DELIVERY.toString(), "delivery",
	////			        new Gson().toJson(delivery)), HotSpot.class);
	////			if (d == null){
	////				resCode= SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////
	////		} catch (JsonSyntaxException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_PARAM;
	////		} catch (IOException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_CONNECTION;
	////		}
	////		return d;
	////	}
	//
	//	
	////	public SCReturnCode removeHotSpot(Long deliveryID) {
	////		try {
	////			return new Gson().fromJson(Communicator.execute(servletName,
	////					operation, LetsEatFunctions.REMOVE_DELIVERY.toString(), "deliveryID",
	////			        new Gson().toJson(deliveryID)), SCReturnCode.class);
	////		} catch (JsonSyntaxException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_PARAM;
	////		} catch (IOException e) {
	////			e.printStackTrace();
	////			resCode= SCReturnCode.BAD_CONNECTION;
	////		}
	////		return resCode;
	////	}
	//
	//	
	////	public HotSpot getHotSpot(Long deliveryID) {
	////		HotSpot d = null;
	////		try {
	////			d = new Gson().fromJson(Communicator.execute(servletName, "query",
	////			        "yes", operation, LetsEatFunctions.GET_DELIVERY.toString(), "deliveryID",
	////					new Gson().toJson(deliveryID)), HotSpot.class);
	////			if (d == null){
	////				resCode =  SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return d;
	////	}
	//
	//	
	////	public List<HotSpot> getHotSpotsByRadios(Double latitude,
	////			Double longitude, Double radios) {
	////		List<HotSpot> l = null;
	////		try {
	////			l = new Gson().fromJson(Communicator.execute(servletName, "query",
	////				        "yes", operation,
	////				        LetsEatFunctions.GET_DELIVERIES_BY_RADIOS.toString(),
	////				        "latitude", new Gson().toJson(latitude),
	////				        "longitude", new Gson().toJson(longitude),
	////				        "radios", new Gson().toJson(radios)), new TypeToken<List<HotSpot>>() {}.getType());
	////			if (l == null){
	////				resCode =  SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////			
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return l;
	////	}
	//
	//	
	////	public List<HotSpot> getDeliveriesByOwner(Long ownerID) {
	////		List<HotSpot> l = null;
	////		try {
	////			l = new Gson().fromJson(Communicator.execute(servletName, "query",
	////				        "yes", operation, LetsEatFunctions.GET_DELIVERIES_BY_OWNER.toString(),
	////				        "ownerID", new Gson().toJson(ownerID)), 
	////				        new TypeToken<List<HotSpot>>() {}.getType());
	////			
	////			if (l == null){
	////				resCode =  SCReturnCode.FAILURE;
	////			} else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////			
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return l;
	////	}
	////
	////	
	////	public void joinHotSpot(Long hotSpotID, Long UserId) {
	////
	////		try {
	////			new Gson().fromJson(Communicator.execute(servletName, "query",
	////				        "yes", operation, LetsEatFunctions.JOIN_DELIVERY.toString(),
	////				        "deliveryID", new Gson().toJson(hotSpotID),
	////				        "UserId", new Gson().toJson(UserId)), Order.class);
	////			if (or == null){
	////				resCode =  SCReturnCode.FAILURE;
	////			}else{
	////				resCode = SCReturnCode.SUCCESS;
	////			}
	////	
	////		} catch (JsonSyntaxException e) {
	////			resCode = SCReturnCode.BAD_PARAM;
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			resCode = SCReturnCode.BAD_CONNECTION;
	////			e.printStackTrace();
	////		}
	////		return or;
	////	}
	//
	//	
	//	public SCReturnCode leaveHotSpot(Long deliveryID, Long UserID) {
	//		try {
	//			return new Gson().fromJson(Communicator.execute(servletName, "query",
	//				        "yes", operation, LetsEatFunctions.LEAVE_DELIVERY.toString(),
	//				        "deliveryID",  new Gson().toJson(deliveryID),
	//				        "UserID",  new Gson().toJson(UserID)), 
	//				        SCReturnCode.class);
	//	
	//		} catch (JsonSyntaxException e) {
	//			resCode = SCReturnCode.BAD_PARAM;
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			resCode = SCReturnCode.BAD_CONNECTION;
	//			e.printStackTrace();
	//		}
	//		return resCode;
	//	}
	//
	//	
	//	public List<Order> getOrders(Long deliveryID) {
	//		List<Order> l = null;
	//		try {
	//			l = new Gson().fromJson(Communicator.execute(servletName, "query",
	//				        "yes", operation, LetsEatFunctions.GET_ORDERS.toString(),
	//				        "deliveryID", new Gson().toJson(deliveryID)), 
	//				        new TypeToken<List<Order>>() {}.getType());
	//			
	//			if (l == null){
	//				resCode =  SCReturnCode.FAILURE;
	//			} else{
	//				resCode = SCReturnCode.SUCCESS;
	//			}
	//			
	//		} catch (JsonSyntaxException e) {
	//			resCode = SCReturnCode.BAD_PARAM;
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			resCode = SCReturnCode.BAD_CONNECTION;
	//			e.printStackTrace();
	//		}
	//		return l;
	//	}
	//
	//
	//	public List<BaseObj> getTagsByRadios(Double latitude,
	//			Double lontitude, Double radios) {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//
	//	public Tag addTag(Tag hotSpot) {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//
	//	public void updateTag(Tag mTag) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void removeTag(Long getmId) {
	//		// TODO Auto-generated method stub
	//		
	//		
	//	}
	//
	//
	//	public void leaveTag(Long getmId, Long uid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void joinTag(Long getmId, Long uid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void joinTag2(Long getmId, Long hsid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void breakUserHotSpot(Long getmId, Long uid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void joinUserHotSpot(Long getmId, Long uid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void breakUserTag(Long getmId, Long uid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void joinUserTag(Long getmId, Long uid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void breakSpotTag(Long getmId, Long hsid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public void joinSpotTag(Long getmId, Long hsid) {
	//		// TODO Auto-generated method stub
	//		
	//	}
	//
	//
	//	public Collection<? extends User> getUsers() {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//
	//	public Collection<? extends Tag> getTags() {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//
	//	DBManager.INSTANCE.getAllHotSpots() Collection<? extends HotSpot> getAllHotSpots() {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//	
}
