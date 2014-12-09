package il.ac.technion.logic.DataBase;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import il.ac.technion.logic.Objects.HotSpot;
import il.ac.technion.logic.Objects.Tag;

public class HotSpotLocalDB extends LocalDB<HotSpot>{

	@Override
	public List<HotSpot> toList(String str) {
		Type listType = new TypeToken<List<HotSpot>>() {}.getType();
		return new Gson().fromJson(str, listType);
	}

	@Override
	public HotSpot toObject(String str) {
		
		String regex = "^HotSpot mId =([0-9]+) has been deleted\n$";
		if(str.matches(regex)){
			String s = str.replaceAll(regex, "$1");
			Long id = Long.getLong(s);
			return new HotSpot(id,0L,0L,"",0.0, 0.0,"","","","");
		}
		
		Type type = new TypeToken<HotSpot>() {}.getType();
		return new Gson().fromJson(str, type);
	}	

	public void removeObj(Long id){
		mData.remove(id);
	}
}
