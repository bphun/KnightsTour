public class Location implements Comparable<Location> {
	
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

	public double distanceTo(Location other) {
		double otherX = other.x;
		double otherY = other.y;
		return Math.sqrt(Math.pow(otherX - x, 2) + Math.pow(otherY - y, 2));
	}

	public double distanceTo(int otherX, int otherY) {
		return Math.sqrt(Math.pow(otherX - x, 2) + Math.pow(otherY - y, 2));
	}

	@Override
	public String toString() {
		return "X: " + x() + " Y: " + y();
	}

	@Override
	public int compareTo(Location location) {
		if ((this.x < location.x) && (this.y < location.y)) {
			return -1;
		} else if ((this.x == location.x) && (this.y == location.y)) {
			return 0;
		} 
		return 1;
	}
}