package il.ac.technion.logic.DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdMap<T,L> {
	
	Map<T,List<L>> map = new HashMap<T, List<L>>();
	
	public void addEntry(T target, L param){
		
		if(map.get(target) == null){
			map.put(target, new ArrayList<L>());
		}
		
		map.get(target).add(param);
	}
	
	public List<L> getList(T target){
		if(target!=null && map.get(target) != null){
			return map.get(target);
		}
		return new ArrayList<L>();
	}

	public void removeEntry(T target, L param){
		if(map.get(target) != null)		
			map.get(target).remove(param);
	}

	public boolean isExist(T target, L param){
		
		if(map.get(target) != null && map.get(target).contains(param))
			return true;
		return false;
	}

	public void removeAllEntrys(T target){
		map.remove(target);
	}
}
