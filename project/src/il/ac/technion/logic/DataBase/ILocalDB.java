package il.ac.technion.logic.DataBase;

public interface ILocalDB {
	
	public void setAllObjs(String str);

	public void addObj(String str) throws DBNotSynced;

	public void updateObj(String str) throws DBNotSynced;

	public void removeObj(String str) throws DBNotSynced;

}

