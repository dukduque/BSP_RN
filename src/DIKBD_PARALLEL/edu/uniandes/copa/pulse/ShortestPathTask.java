package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

public class ShortestPathTask implements Runnable {

	private DukqstraDist spDist; 
	private DukqstraTime spTime;
	private int algoRuning;
	public ShortestPathTask(int quienEs, DukqstraDist dD, DukqstraTime dT) {
		//quienES 1 Dist, 0 Time;
		algoRuning = quienEs;
		if(quienEs==1){
			spDist = dD;
		}else{
			spTime = dT;
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(algoRuning==1){
			spDist.runAlgDist();
		}else{
			spTime.runAlgTime();
		}
	}

	
	
}
