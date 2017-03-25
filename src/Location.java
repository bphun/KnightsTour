public class Location {
	
	public int x,y;
	
	public Location(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}

	@Override
	public String toString() {
		return "X: " + x() + " Y: " + y();
	}
}