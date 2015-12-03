package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.axes.OneStepIterator;


/**
 * last vertex
 * Modified pulse function 
 * Updates Pareto Font
 *  @author Daniel Duque, Leonardo Lozano, Andrés Medaglia
 *  d.duque25@uniandes.edu.co
 */
public class FinalVertexPulse extends VertexPulse {
	
	private int id;
	private VertexPulse bLeft;
	private VertexPulse bRigth;
	private EdgePulse reverseEdges;
	
	int minDist;
	int maxTime;
	int minTime;
	int maxDist;
	
	private boolean inserted;
	
	int c=0;
	int d=0;
	public FinalVertexPulse(int i) {
		super(i);
		id = i;
		inserted = false;
		minDist = infinity;
		minTime = infinity;
		maxTime = 0;
		maxDist = 0;
		bLeft = this;
		bRigth = this;
	}

	
	public int  getID()
	{
		return id;
	}
	
	public void addReversedEdge(EdgePulse e)
	{
		if(reverseEdges!=null){
			reverseEdges.addNextCommonTailEdge(e);
		}else
			reverseEdges = e;
	}
	
	
	public EdgePulse findEdgeByTarget(VertexPulse target){
		if(reverseEdges!=null){
			reverseEdges.findEdgebyTarget(target);
		}
		return null;
	}
	public EdgePulse getReversedEdges() {
		if(reverseEdges!= null){
			return reverseEdges;
		}return new EdgePulse(1,1, this,this , -1);
	}
	
	public void setMinDist(int c){
		minDist = c;
	}
	
	public int getMinDist(){
		return minDist;
	}
	
	public void setMaxTime(int mt){
		maxTime = mt;
	}
	
	public int getMaxTime(){
		return maxTime;
	}
	
	public void setMinTime(int t){
		minTime = t;
	}
	
	public int getMinTime(){
		return minTime;
	}
	
	public void setMaxDist(int md){
		maxDist = md;
	}
	
	public int getMaxDist(){
		return maxDist;
	}
	
	
	/**
	 * Unlink a vertex from the bucket
	 * @return true, if the buckets gets empty
	 */
	public boolean unLinkVertexDist(){
		if(bRigth.getID() == id){
			bLeft=this;
			bRigth=this;
			return true;
		}else{
			bLeft.setRigthDist(bRigth);
			bRigth.setLeftDist(bLeft);
			bLeft = this;
			bRigth = this;
			return false;
		}
	}
	/**
	 * Insert a vertex in a bucket. 
	 * New vertex is inserted on the left of the bucket entrance 
	 * @param v vertex in progress to be inserted
	 */
	public void insertVertexDist(VertexPulse v) {
		v.setLeftDist(bLeft);
		v.setRigthDist(this);
		bLeft.setRigthDist(v);
		bLeft = v;
	}
	
	
	public void setLeftDist(VertexPulse v){
		bLeft= v;
	}
	public void setRigthDist(VertexPulse v){
		bRigth= v;
	}
	public VertexPulse getBLeftDist(){
		return bLeft;
	}
	public VertexPulse getBRigthDist(){
		return bRigth;
	}
	public void setInsertedDist(){
		inserted = true;
	}
	public boolean isInserteDist(){
		return inserted;
	}

	public void reset(){
		inserted = false;
	}
	public void setBounds(int MT, int MD){
		maxDist = MD- minDist;
		maxTime = MT - minTime;
	}
		
	public void pulse(int PTime, int PDist, ArrayList<Integer> path) {
		path.add(id);
		if(this.CheckDominance(PDist, PTime)==false){
			addPathToEF(PDist, PTime, path);
		}
		path.remove((path.size()-1));
	}

	/**
	 * Binary Lex insertion of non-domminated points
	 * @param pDist new path dist
	 * @param pTime new path time
	 * @param path new path 
	 */
	private void addPathToEF(int pDist, int pTime, ArrayList<Integer> path) {
		Path np = new Path(pDist, pTime, path);
		double cScore = np.dist;
		
		boolean cond = true;
		int l = 0;
		int r = PulseGraph.onlineEF.size();
		int m = (int) ((l + r) / 2);
		double mVal = 0;
		if (PulseGraph.onlineEF.size() == 0) {
			PulseGraph.onlineEF.add(np);
			System.err.println("Adding a efficient path in an empty set");
			return;
		} else if (PulseGraph.onlineEF.size() == 1) {
			mVal = PulseGraph.onlineEF.get(m).dist;
			int insert = cScore < mVal ? 0 : 1;
			PulseGraph.onlineEF.add(insert, np);
			return;
		} else {
			mVal = PulseGraph.onlineEF.get(m).dist;
		}

		while (cond) {
			if (r - l > 1) {
				if (cScore < mVal) {
					r = m;
					m = (int) ((l + r) / 2);
				} else if (cScore > mVal) {
					l = m;
					m = (int) ((l + r) / 2);
				} else {
					PulseGraph.onlineEF.add(m, np);
					System.err.println(FinalVertexPulse.class + " method: addPathToEF: \n" + " this should not happend because for 2 paths with the same dist there is a dominance test" );
					return;
				}
				mVal = PulseGraph.onlineEF.get(m).dist;
			} else {
				cond = false;
				if (l == m) {
					int insert = cScore < mVal ? l : l + 1;
					PulseGraph.onlineEF.add(insert, np);
				} else if (r == m) {
					System.out.println("esto no pasa ");
					PulseGraph.onlineEF.add(cScore < mVal ? r : Math.min(r + 1, PulseGraph.onlineEF.size()), np);
				} else {
					System.err.println(FinalVertexPulse.class +  " addEvent, error");
				}
				return;

			}
		}
		
		
		
		
	}


	private boolean CheckDominance(int dist, int time) {

		for (int i = 0; i < PulseGraph.onlineEF.size(); i++) {

			if (time >= PulseGraph.onlineEF.get(i).time && dist >= PulseGraph.onlineEF.get(i).dist) {
				c++;
				return true;
			}

			if (time <= PulseGraph.onlineEF.get(i).time && dist <= PulseGraph.onlineEF.get(i).dist) {
				d++;
				
				if(i==0){
					//I am removing best sol in dist, ie, dist = onlineEF.get(0).dist
					PulseGraph.MaxTime = time;
				}else if(i==PulseGraph.onlineEF.size()-1){
					//I am removing best sol in time, ie, time = onlineEF.get(|Zn|-1).time
					PulseGraph.MaxDist = dist;
				}
				PulseGraph.onlineEF.remove(i);
				i--;
			}
		}
		return false;
	}
}
