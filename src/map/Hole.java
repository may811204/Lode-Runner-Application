package map;

public class Hole extends MapChunk {
	private int counter;

	public Hole (int i, int j, int counter) {
		this.i=i;
		this.j=j;
		this.counter = counter;
	}
	public void refresh () {
		counter--;
	}
	public boolean isTimeUp () {
		return counter == 0;
	}
	@Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (!(o instanceof Hole))
            return false;
        Hole hole = (Hole) o;
        return (i == hole.i) && (j == hole.j);
    }
}
