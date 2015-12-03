package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

import java.util.ArrayList;
/**
 * Path data structure
 * @author Daniel Duqe, Leonardo Lozano, Andrés Medaglia
 * d.duque25@uniandes.edu.co
 *
 */
public class Path {
	
	public ArrayList<Integer> path;
	public int dist;
	public int time;
	
	public Path(int nDist, int nTime, ArrayList<Integer> nPath) {
		path = new ArrayList<Integer>();
		path.addAll(nPath);
		dist = nDist;
		time = nTime;
	}
	
	/**
	 * Tests if the {@link Path} given as a parameter is dominated
	 * @param ptest testing {@link Path}
	 * @return true if ptest is dominated by this
	 */
	public boolean dominatePath(Path ptest){
		if(dist<=ptest.dist && time <=ptest.time){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
//		return ""+dist;
		return dist+":"+time;
	}

}
