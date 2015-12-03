package DIKBD_PARALLEL.edu.uniandes.copa.pulse;


/**
 * Data structure for the shortest path initialization
 * @author Daniel Duqe, Leonardo Lozano, Andrés Medaglia
 * d.duque25@uniandes.edu.co
 */
public class Bucket {
	
	private VertexPulse entrance;
	private int key;
	
	/**
	 * Create an instance of a bucket. If a bucket
	 * is opened, a new vertex is being added
	 * @param v
	 */
	public Bucket(VertexPulse v, int nKey){
		entrance = v;
		key = nKey;
	}
	
	public Bucket(int nKey){
		key = nKey;
	}
	
	
	/**
	 * Insert a vertex in the bucket.
	 * @param v Vertex being inserted
	 */
	public void insertVertexDist(VertexPulse v){
		if(entrance!=null){
			entrance.insertVertexDist(v);
		}else{
			entrance = v;
		}
	}
	public void insertVertexTime(VertexPulse v){
		if(entrance!=null){
			entrance.insertVertexTime(v);
		}else{
			entrance = v;
		}
	}
	
	
	/**
	 * 
	 * @param v
	 * @return
	 */
	public boolean deleteLabeledVertexDist(){
		//Delete entrance / FIFO policy 
		entrance = entrance.getBRigthDist();
		boolean emp = entrance.getBLeftDist().unLinkVertexDist();
		if(emp){
			entrance = null;
			return true;
		}
		return false;
	}
	public boolean deleteLabeledVertexTime(){
		//Delete entrance / FIFO policy 
		entrance = entrance.getBRigthTime();
		boolean emp = entrance.getBLeftTime().unLinkVertexTime();
		if(emp){
			entrance = null;
			return true;
		}
		return false;
	}
	
	
	
	public boolean deleteToMoveDist(VertexPulse v){
		if(entrance.getID() == v.getID()){
			entrance = entrance.getBRigthDist();
		}
		if(v.unLinkVertexDist()){
			entrance = null;
			return true;
		}
		return false;
	}
	public boolean deleteToMoveTime(VertexPulse v){
		if(entrance.getID() == v.getID()){
			entrance = entrance.getBRigthTime();
		}
		if(v.unLinkVertexTime()){
			entrance = null;
			return true;
		}
		return false;
	}
	
	public int getKey(){
		return key;
	}
	public void setKey(int nKey){
		key = nKey;
	}
	
	public VertexPulse getEntrance(){
		return  entrance;
	}
	
	public boolean empty() {
		if(entrance!=null){
			return false;
		}
		return true;
	}
}
