package il.ac.technion.logic.DataBase;

import il.ac.technion.logic.Objects.User;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserLocalDB extends BaseObjectDB<User>{

	protected Map<String,Long> idMAp = new HashMap<String, Long>();
	
	
	
	@Override
	public void addObj(String str) {
		super.addObj(str);
		User u = toObject(str);
		idMAp.put(u.getStringId(), u.getId());
	}
	
	public void addObj(User u) {
		mData.put(u.getId(), u);
		idMAp.put(u.getStringId(), u.getId());
	}
	
		@Override
	public void setAllObjs(String str) {
		super.setAllObjs(str);
		
		List<User> list = getAllObjs();
		for(User a: list){
			idMAp.put(a.getStringId(), a.getId());
		}
	}
		
	public Long retriveId(String str){
		User u = toObject(str);
		return u.getId();
	}


	@Override
	public void removeObj(String str) {
		super.removeObj(str);
		User u = toObject(str);
		idMAp.remove(u.getStringId());
	}

	@Override
	protected List<User> toList(String str) {
		Type listType = new TypeToken<List<User>>() {}.getType();
		return new Gson().fromJson(str, listType);
	}

	@Override
	protected User toObject(String str) {
		String regex = "^User mId =([0-9]+) has been deleted\n$";
		if(str.matches(regex)){
			String s = str.replaceAll(regex, "$1");
			//Long id = Long.getLong(s);
			return new User(s,"","");
		}
		Type type = new TypeToken<User>() {}.getType();
		return new Gson().fromJson(str, type);
	}

	public User getItemById(String strId){
		return getItemById(idMAp.get(strId));
	}
}
