import java.util.Random;

import DIKBD_PARALLEL.edu.uniandes.copa.pulse.FinalVertexPulse;


public class RND {
	public static void main(String[] args) {
		Random r1 = new Random(0);
		int nodes  = 321270;
		for (int i = 1; i <=50; i++) {
			int source = 1+ r1.nextInt(nodes); 
			int sink = 1+ r1.nextInt(nodes); 
			System.out.println(i+ "," + source + "," +sink);
		}
	}
}
