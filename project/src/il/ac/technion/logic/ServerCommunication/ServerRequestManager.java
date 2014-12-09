package il.ac.technion.logic.ServerCommunication;

import com.google.android.gms.internal.hs;

import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;
import il.ac.technion.logic.DataBase.LocalDBManager;
import il.ac.technion.logic.Objects.HotSpot;
import il.ac.technion.logic.Objects.Tag;
import il.ac.technion.logic.Objects.User;
import android.net.Uri;

public enum ServerRequestManager {
	
	INSTANCE;
	
	//sync: HotSpos, User, Tag
	
	public void syncHotSpots(UiOnDone onDone, UiOnError onError){
		RequestDetailsForHotSpot reqDet = new RequestDetailsForHotSpot(null);
		reqDet.setRequestType(RequestType.GET);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.HotSpotDB.setAllObjs(serverRes);
			}
		} ;
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void syncUsers(UiOnDone onDone, UiOnError onError){
		RequestDetailsForUser reqDet = new RequestDetailsForUser(null);
		reqDet.setRequestType(RequestType.GET);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.UserDB.setAllObjs(serverRes);
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void syncTags(UiOnDone onDone, UiOnError onError){
		RequestDetailsForTag reqDet = new RequestDetailsForTag(null);
		reqDet.setRequestType(RequestType.GET);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.TagDB.setAllObjs(serverRes);
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	//add: HotSpos, User, Tag
	
	public void addHotSpots(final HotSpot hotSpot, UiOnDone onDone, UiOnError onError){
		RequestDetailsForHotSpot reqDet = new RequestDetailsForHotSpot(hotSpot);
		reqDet.buildeAddRequestParameter();
		reqDet.setRequestType(RequestType.POST);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.HotSpotDB.addObj(serverRes);
				hotSpot.setmId(LocalDBManager.INSTANCE.HotSpotDB.retriveId(serverRes));
				LocalDBManager.INSTANCE.UserHotSpot.addEntry(UserManager.INSTANCE.getMyID(), hotSpot.getmId());
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void addUser(final User user, UiOnDone onDone, UiOnError onError){
		RequestDetailsForUser reqDet = new RequestDetailsForUser(user);
		reqDet.buildeAddRequestParameter();
		reqDet.setRequestType(RequestType.POST);
		
		IServerRequest req = new ServerRequest(reqDet) {
			@Override
			void postAction(String serverRes) {
				//LocalDBManager.INSTANCE.UserDB.addObj(serverRes);
				Long id = LocalDBManager.INSTANCE.UserDB.retriveId(serverRes);
				user.setLongId(id);
				LocalDBManager.INSTANCE.UserDB.addObj(user);
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void addTag(final Tag tag, UiOnDone onDone, UiOnError onError){
		RequestDetailsForTag reqDet = new RequestDetailsForTag(tag);
		reqDet.buildeAddRequestParameter();
		reqDet.setRequestType(RequestType.POST);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.TagDB.addObj(serverRes);
				tag.setmId(LocalDBManager.INSTANCE.TagDB.retriveId(serverRes));
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	//remove: HotSpos, User, Tag
	
	public void removeHotSpots(final HotSpot hotSpot, UiOnDone onDone, UiOnError onError){
		RequestDetailsForHotSpot reqDet = new RequestDetailsForHotSpot(hotSpot);
		reqDet.buildeRemoveRequestParameter();
		reqDet.setRequestType(RequestType.DELETE);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.HotSpotDB.removeObj(hotSpot.getmId());
				LocalDBManager.INSTANCE.UserHotSpot.removeHotSpot(hotSpot.getmId());
				LocalDBManager.INSTANCE.TagHotSpot.removeHotSpot(hotSpot.getmId());
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void removeUser(User user, UiOnDone onDone, UiOnError onError){
		RequestDetailsForUser reqDet = new RequestDetailsForUser(user);
		reqDet.buildeRemoveRequestParameter();
		reqDet.setRequestType(RequestType.DELETE);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.UserDB.removeObj(serverRes);
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void removeTag(Tag tag, UiOnDone onDone, UiOnError onError){
		RequestDetailsForTag reqDet = new RequestDetailsForTag(tag);
		reqDet.buildeRemoveRequestParameter();
		reqDet.setRequestType(RequestType.DELETE);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.TagDB.removeObj(serverRes);
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	//update: HotSpos, User, Tag
	
	public void updateHotSpots(HotSpot hotSpot, UiOnDone onDone, UiOnError onError){
		RequestDetailsForHotSpot reqDet = new RequestDetailsForHotSpot(hotSpot);
		reqDet.buildeUpdateRequestParameter();
		reqDet.setRequestType(RequestType.PUT);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.HotSpotDB.updateObj(serverRes);
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void updateUser(User user, UiOnDone onDone, UiOnError onError){
		RequestDetailsForUser reqDet = new RequestDetailsForUser(user);
		reqDet.setRequestType(RequestType.DELETE);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.UserDB.updateObj(serverRes);
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void updateTag(Tag tag, UiOnDone onDone, UiOnError onError){
		RequestDetailsForTag reqDet = new RequestDetailsForTag(tag);
		reqDet.setRequestType(RequestType.DELETE);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.TagDB.updateObj(serverRes);
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	//join
	
	public void JoinUserHotSpot(final String strId, final Long hid, UiOnDone onDone, UiOnError onError){
		
		RequestDetailsReletionsUsetHotSpot reqDet = new RequestDetailsReletionsUsetHotSpot();
		reqDet.setParams(strId,hid);
		reqDet.setRequestType(RequestType.POST);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
		
				//check that the reqDet is ok and use it if you can.
				LocalDBManager.INSTANCE.UserHotSpot.addEntry(strId, hid);
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void JoinUserTag(final String strId, final Long tid, UiOnDone onDone, UiOnError onError){
		
	
		RequestDetailsReletionsUseTag reqDet = new RequestDetailsReletionsUseTag();
		reqDet.setParams(strId,tid);
		reqDet.setRequestType(RequestType.POST);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
		
				//check that the reqDet is ok and use it if you can.
				LocalDBManager.INSTANCE.UserTag.addEntry(strId, tid);//TagDB.getItemById(tid).addUser(uid);
				
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void JoinHotSpotTag(final Long hid, final Long tid, UiOnDone onDone, UiOnError onError){
	
		RequestDetailsReletionsTagHotSpot reqDet = new RequestDetailsReletionsTagHotSpot();
		reqDet.setParams(hid,tid);
		reqDet.setRequestType(RequestType.POST);
	
		IServerRequest req = new ServerRequest(reqDet) {
		
			@Override
			void postAction(String serverRes) {
		
			//check that the reqDet is ok and use it if you can.
				LocalDBManager.INSTANCE.TagHotSpot.addEntry(tid, hid);
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	//break
	
	public void BreakUserHotSpot(final String strId, final Long hid, UiOnDone onDone, UiOnError onError){
		
		RequestDetailsReletionsUsetHotSpot reqDet = new RequestDetailsReletionsUsetHotSpot();
		reqDet.setParamsDelete(strId,hid);
		reqDet.setRequestType(RequestType.DELETE);
	
		IServerRequest req = new ServerRequest(reqDet) {
		
			@Override
			void postAction(String serverRes) {
	
				//check that the reqDet is ok and use it if you can.
				LocalDBManager.INSTANCE.UserHotSpot.removeEntry(strId, hid);
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void BreakUserTag(final String strId, final Long tid, UiOnDone onDone, UiOnError onError){
		
		RequestDetailsReletionsUseTag reqDet = new RequestDetailsReletionsUseTag();
		reqDet.setParamsDelete(strId,tid);
		reqDet.setRequestType(RequestType.DELETE);
	
		IServerRequest req = new ServerRequest(reqDet) {
		
			@Override
			void postAction(String serverRes) {
	
				//check that the reqDet is ok and use it if you can.
				LocalDBManager.INSTANCE.UserTag.removeEntry(strId, tid);
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void BreakHotSpotTag(final Long hid, final Long tid, UiOnDone onDone, UiOnError onError){
	
		RequestDetailsReletionsTagHotSpot reqDet = new RequestDetailsReletionsTagHotSpot();
		reqDet.setParamsDelete(hid,tid);
		reqDet.setRequestType(RequestType.DELETE);
	
		IServerRequest req = new ServerRequest(reqDet) {
		
			@Override
			void postAction(String serverRes) {
	
				//check that the reqDet is ok and use it if you can.
				LocalDBManager.INSTANCE.TagHotSpot.removeEntry(tid, hid);
			}
		};
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	//sync relations
	
	public void syncRelationsUserTag(UiOnDone onDone, UiOnError onError){
		RequestDetailsReletionsUseTag reqDet = new RequestDetailsReletionsUseTag();
		reqDet.setRequestType(RequestType.GET);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.UserTag.sync(serverRes);
			}
		} ;
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void syncRelationsTagHotSpot(UiOnDone onDone, UiOnError onError){
		RequestDetailsReletionsTagHotSpot reqDet = new RequestDetailsReletionsTagHotSpot();
		reqDet.setRequestType(RequestType.GET);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.TagHotSpot.sync(serverRes);
			}
		} ;
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	public void syncRelationsHotSpotUser(UiOnDone onDone, UiOnError onError){
		RequestDetailsReletionsUsetHotSpot reqDet = new RequestDetailsReletionsUsetHotSpot();
		reqDet.setRequestType(RequestType.GET);
		
		IServerRequest req = new ServerRequest(reqDet) {
			
			@Override
			void postAction(String serverRes) {
				LocalDBManager.INSTANCE.UserHotSpot.sync(serverRes);
			}
		} ;
		new ServerInvocation().Invoke(req, onDone, onError);
	}
	
	//upload iamge
	public void uploadImageToServer(final HotSpot hs, Uri imageUri, UiOnDone onDone, UiOnError onError){
		IServerRequest req = new UploadImageServerRequest(imageUri) {
			
			@Override
			void postAction(String serverRes) {
				hs.setImageURL(serverRes);
			}
		} ;
		new ServerInvocation().Invoke(req, onDone, onError);
	}
}
