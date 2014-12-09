package il.ac.technion.logic.DataBase;

import il.ac.technion.logic.Objects.HotSpot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class RelationsUserHotSpot extends Relations<String, Long> {
	
	private class UHS {
		Long id;
		String mUserId;
		Long mHotspotId;
	}
	
	protected UHS toObject(String str){
		Type type = new TypeToken<UHS>() {}.getType();
		return new Gson().fromJson(str, type);
	}
	
	protected List<UHS> toList(String str) {
		Type listType = new TypeToken<List<UHS>>() {}.getType();
		return new Gson().fromJson(str, listType);
	}
	
	public void sync(String str){
		
		for(UHS ut : toList(str)){
			this.addEntry(ut.mUserId, ut.mHotspotId);
		}
	}
	
	public List<String> getUsersFromHotSpot(Long hId){
		return T2_to_T1_list(hId);
	}
	
	public List<Long> getHotSpotsFromUser(String uId){
		return T1_to_T2_list(uId);
	}
	
	public List<Long> getHotSpotsAdminedByUser(String uId){
		
		List<Long> res = new ArrayList<Long>();
		for(Long hsid : T1_to_T2_list(uId)){
			HotSpot h = LocalDBManager.INSTANCE.HotSpotDB.getItemById(hsid);
			if (h==null) continue;
			String admin = LocalDBManager.INSTANCE.HotSpotDB.getItemById(hsid).getAdminId();
			if(admin.equals(uId)) res.add(hsid);
		}
		return res;
	}
	
	public List<Long> getHotSpotsNOTAdminedByUser(String uId){
		
		List<Long> res = new ArrayList<Long>();
		for(Long hsid : T1_to_T2_list(uId)){
			if(LocalDBManager.INSTANCE.HotSpotDB.getItemById(hsid)==null) continue;
			String admin = LocalDBManager.INSTANCE.HotSpotDB.getItemById(hsid).getAdminId();
			if(!admin.equals(uId)) res.add(hsid);
		}
		return res;
	}

	
	public void removeHotSpot(Long hId) {
		List<String> l = getUsersFromHotSpot(hId);
		for(String uid : l){
			removeEntry(uid, hId);
		}
		
	}

}
