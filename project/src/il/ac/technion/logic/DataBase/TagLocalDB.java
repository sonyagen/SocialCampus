package il.ac.technion.logic.DataBase;

import il.ac.technion.logic.Objects.Tag;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TagLocalDB extends LocalDB<Tag>{
	
	@Override
	protected List<Tag> toList(String str) {
		Type listType = new TypeToken<List<Tag>>() {}.getType();
		return new Gson().fromJson(str, listType);
	}
	@Override
	protected Tag toObject(String str) {
		
		String regex = "^Tag mId =([0-9]+) has been deleted\n$";
		if(str.matches(regex)){
			String s = str.replaceAll(regex, "$1");
			Long id = Long.getLong(s);
			return new Tag(id,"");
		}
		
		Type type = new TypeToken<Tag>() {}.getType();
		return new Gson().fromJson(str, type);
	}
	
	public Collection<String> getAllTagStrings() {
		Collection<String> c = new ArrayList<String>();
		for(Tag t : mData.values()){
			c.add(t.getmName());
		}
		return c;
	}
	public Long getIdByTagName(String tagName) {
		
		for(Tag t : mData.values()){
			if (t.getmName().equals(tagName)){
				return t.getId();
			}
		}
		return -1L;
	}
}
