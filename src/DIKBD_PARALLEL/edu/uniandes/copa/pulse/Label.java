package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

/**
 * Label data structure
 *  @author Daniel Duque, Leonardo Lozano, Andrés Medaglia
 *  d.duque25@uniandes.edu.co
 */
public class Label {
	
	
	public int time;
	public int dist;
	//public Label parent;
	//public int node;
	
	public Label(int nDist , int nTime, int node) {
		time = nTime;
		dist = nDist;
		//node = node; 
	}
	
	public boolean dominateLabel(int cDist, int cTime) {
		if (time <= cDist && dist <= cTime) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return ""+ dist;
	}

}
