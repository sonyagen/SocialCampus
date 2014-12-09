package il.ac.technion.logic.DataBase;

public enum LocalDBManager {
	INSTANCE;
	
	public HotSpotLocalDB HotSpotDB = new HotSpotLocalDB();
	public UserLocalDB UserDB = new UserLocalDB();
	public TagLocalDB TagDB = new TagLocalDB();
	
	public RelationsUserTag UserTag = new RelationsUserTag();
	public RelationsUserHotSpot UserHotSpot = new RelationsUserHotSpot();
	public RelationsTagHotSpot TagHotSpot = new RelationsTagHotSpot();
}
