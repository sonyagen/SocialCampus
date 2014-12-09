package il.ac.technion.logic.DataBase;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RelationsUserTag extends Relations<String, Long> {
	
	private class UT {
		Long id;
		String mUserId;
		Long mTagId;
	}
	
	protected UT toObject(String str){
		Type type = new TypeToken<UT>() {}.getType();
		return new Gson().fromJson(str, type);
	}
	
	protected List<UT> toList(String str) {
		Type listType = new TypeToken<List<UT>>() {}.getType();
		return new Gson().fromJson(str, listType);
	}
	
	public void sync(String str){
		
		for(UT ut : toList(str)){
			this.addEntry(ut.mUserId, ut.mTagId);
		}
		
	}
	
	public List<String> getUsersFromTag(Long tId){
		return T2_to_T1_list(tId);
	}
	
	public List<Long> getTagsFromUser(String uId){
		return T1_to_T2_list(uId);
	}

}
