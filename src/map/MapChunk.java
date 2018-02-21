package map;

public abstract class MapChunk {
	protected int i = 0;
	protected int j = 0;
	protected String type = "";

	public int getX(){
		return i;
	}
	public int getY(){
		return j;
	}
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
}