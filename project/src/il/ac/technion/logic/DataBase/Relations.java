package il.ac.technion.logic.DataBase;

import java.util.List;

public class Relations<T1,T2> {
	
	IdMap<T1,T2> AToBList = new IdMap<T1,T2>();
	IdMap<T2,T1> BToAList = new IdMap<T2,T1>();
	
	public void addEntry(T1 aId, T2 bId){
		AToBList.addEntry(aId, bId);
		BToAList.addEntry(bId, aId);
	}
	
	public void removeEntry(T1 aId, T2 bId){
		AToBList.removeEntry(aId, bId);
		BToAList.removeEntry(bId, aId);
	}
	
	public List<T2> T1_to_T2_list(T1 aId){
		return AToBList.getList(aId);
	}
	
	public List<T1> T2_to_T1_list(T2 bId){
		return BToAList.getList(bId);
	}

	public boolean isCombined(T1 aId, T2 bId){
		return BToAList.isExist(bId, aId) && AToBList.isExist(aId,bId);
	}
}
