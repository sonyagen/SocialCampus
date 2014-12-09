package il.ac.technion.logic.DataBase;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RelationsTagHotSpot extends Relations<Long, Long>{

	class THS {
		Long id;
		Long mTagId;
		Long mHotspotId;
	}
	
	protected THS toObject(String str){
		Type type = new TypeToken<THS>() {}.getType();
		return new Gson().fromJson(str, type);
	}
	
	protected List<THS> toList(String str) {
		Type listType = new TypeToken<List<THS>>() {}.getType();
		return new Gson().fromJson(str, listType);
	}
	
	public void sync(String str){
		
		for(THS ut : toList(str)){
			this.addEntry(ut.mTagId, ut.mHotspotId);
		}
	}
	
	public List<Long> getHotSpotsFromTag(Long tId){
		return T1_to_T2_list(tId);
	}
	
	public List<Long> getTagsFromHotSpot(Long hId){
		return T2_to_T1_list(hId);
	}

	public void removeHotSpot(Long hId) {
		List<Long> l = getTagsFromHotSpot(hId);
		for(Long tid : l){
			removeEntry(tid, hId);
		}
		
	}

}
