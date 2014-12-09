package il.ac.technion.logic.DataBase;

import il.ac.technion.logic.Objects.IdiableObj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class LocalDB<T extends IdiableObj> implements ILocalDB {
	
	protected boolean completeSync = false;
	
	public boolean isSyncDone(){
		return completeSync;
	}
	
	protected HashMap<Long,T> mData = new HashMap<Long,T>();
	
	protected abstract List<T> toList(String str);
	protected abstract T toObject(String str);
	
	public void setAllObjs(String str) {	
		completeSync = true;
		List<T> list = toList(str);
		mData.clear();
		for(T a: list){
			mData.put(a.getId(), a);
		}
		
		setAllObjs(toList(str));
	}
	
	public T getItemById(Long id){
		return mData.get(id);
	}
	
	public  List<T> getAllObjs() {
		completeSync = true;
		List<T> $ = new ArrayList<T>();
		Collection<T> vs = mData.values();
		for(T v: vs){
			$.add(v);
		}
		return $;
	}
	
	public void setAllObjs(List<T> l) {
		mData.clear();
		for(T h: l){
			mData.put(h.getId(), h);
		}
	}

	public  List<T> getItemsbyIds(List<Long> Ids) {
		List<T> $ = new ArrayList<T>();
		for(Long id : Ids){
			$.add(getItemById(id));
		}
		return $;
	}

	@Override
	public void addObj(String str) {
		T t = toObject(str);
		mData.put(t.getId(), t);		
	}

	@Override
	public void updateObj(String str) {
		T t = toObject(str);
		mData.remove(t.getId());
		mData.put(t.getId(),t);
	}

	@Override
	public void removeObj(String str) {
		
		T t = toObject(str);
		mData.remove(t.getId());
		
	}

	public Long retriveId(String str){
		return toObject(str).getId();
	}
}
