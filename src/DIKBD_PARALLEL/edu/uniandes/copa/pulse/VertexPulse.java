package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.crypto.Data;

/**
 * Node data structure
 * Contains the Pulse function
 * @author Daniel Duque, Leonardo Lozano, Andrés Medaglia
 * d.duque25@uniandes.edu.co
 *
 */
public class VertexPulse {
	
	public static final int infinity = (int)Double.POSITIVE_INFINITY;
	
	private EdgePulse reverseEdges;
	ArrayList<Integer> magicIndex;
	private int id;
	private VertexPulse leftDist;
	private VertexPulse rigthDist;
	private VertexPulse leftTime;
	private VertexPulse rigthTime;
	
	int minDist;
	int maxTime;
	int minTime;
	int maxDist;
	
	private boolean insertedDist;
	private boolean insertedTime;
	/**
	 * PULSE
	 */
	public int usedLabels = 0;
//	double[] timeLabel;
//	double[] distLabel;
	ArrayList<Label> labels;


	boolean firstTime = true; ;

	public VertexPulse(int i) {
		id = i;
		insertedDist = false;
		minDist = infinity;
		minTime = infinity;
		maxTime = 0;
		maxDist = 0;

		leftDist = this;
		rigthDist = this;
		leftTime = this;
		rigthTime = this;
//		timeLabel = new double[DataHandler.numLabels];
//		distLabel = new double[DataHandler.numLabels];
//		for (int j = 0; j < DataHandler.numLabels; j++) {
//			timeLabel[j] = Double.POSITIVE_INFINITY;
//			distLabel[j] = Double.POSITIVE_INFINITY;
//		}
		labels=new ArrayList<Label>();
		magicIndex = new ArrayList<Integer>();
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
		if(rigthDist.getID() == id){
			leftDist=this;
			rigthDist=this;
			return true;
		}else{
			leftDist.setRigthDist(rigthDist);
			rigthDist.setLeftDist(leftDist);
			leftDist = this;
			rigthDist = this;
			return false;
		}
	}
	public boolean unLinkVertexTime(){
		if(rigthTime.getID() == id){
			leftTime=this;
			rigthTime=this;
			return true;
		}else{
			leftTime.setRigthTime(rigthTime);
			rigthTime.setLeftTime(leftTime);
			leftTime = this;
			rigthTime = this;
			return false;
		}
	}

	public void fastUnlinkDist(){
		leftDist=this;
		rigthDist=this;
	}
	public void fastUnlinkTime(){
		leftTime = this;
		rigthTime = this;
	}
	public void unlinkRighBoundDist()
	{
		rigthDist = null;
	}
	public void unlinkRighBoundTime()
	{
		rigthTime = null;
	}
	/**
	 * Insert a vertex in a bucket. 
	 * New vertex is inserted on the left of the bucket entrance 
	 * @param v vertex in progress to be inserted
	 */
	public void insertVertexDist(VertexPulse v) {
		v.setLeftDist(leftDist);
		v.setRigthDist(this);
		leftDist.setRigthDist(v);
		leftDist = v;
	}
	
	public void insertVertexTime(VertexPulse v) {
		v.setLeftTime(leftTime);
		v.setRigthTime(this);
		leftTime.setRigthTime(v);
		leftTime = v;
	}
	
	/**
	 * Distance basic methods
	 */
	public void setLeftDist(VertexPulse v){
		leftDist= v;
	}
	public void setRigthDist(VertexPulse v){
		rigthDist= v;
	}
	public VertexPulse getBLeftDist(){
		return leftDist;
	}
	public VertexPulse getBRigthDist(){
		return rigthDist;
	}
	public void setInsertedDist(){
		insertedDist = true;
	}
	public boolean isInserteDist(){
		return insertedDist;
	}
	/**
	 * Time basic methods
	 */
	public void setLeftTime(VertexPulse v){
		leftTime= v;
	}
	public void setRigthTime(VertexPulse v){
		rigthTime= v;
	}
	public VertexPulse getBLeftTime(){
		return leftTime;
	}
	public VertexPulse getBRigthTime(){
		return rigthTime;
	}
	public void setInsertedTime(){
		insertedTime = true;
	}
	public boolean isInsertedTime(){
		return insertedTime;
	}
	
	
	
	

	public void reset(){
		insertedDist = false;
	}
	

	/**
	 * Pulse recursive function
	 * @param PTime cumulative time
	 * @param PDist cumulative cost
	 * @param path current path
	 */
	public void pulse(int PTime, int PDist, ArrayList<Integer> path) 
	{
	
		if (this.firstTime) {
			this.firstTime = false;
			this.Sort(this.magicIndex);
			leftDist = null;
			rigthDist = null;
			reverseEdges = null;
		}
		changeLabels( PDist, PTime);
		if(System.currentTimeMillis()-Main_BSP_RN.Atime<=3600000) {
			if (!path.contains(id)){
				path.add(id);
				for (int i = 0; i < magicIndex.size(); i++) {
					int NewTime = 0;
					int NewDist = 0;
					int head = DataHandler.Arcs[magicIndex.get(i)][1];
					NewTime = (PTime + DataHandler.Time[magicIndex.get(i)]);
					NewDist = (PDist + DataHandler.Distance[magicIndex.get(i)]);
					if (NewTime <= (PulseGraph.MaxTime - PulseGraph.vertexes[head].getMinTime())
							&& NewDist <= (PulseGraph.MaxDist - PulseGraph.vertexes[head].getMinDist())) {
						if (!this.CheckDominance(NewDist, NewTime, head)) {
							if (!PulseGraph.vertexes[head].CheckLabels(NewDist , NewTime)) {
								PulseGraph.vertexes[DataHandler.Arcs[magicIndex.get(i)][1]].pulse(NewTime, NewDist, path);
							}
						}
					}
				}
				path.remove((path.size() - 1));
			}
		}else{
			Main_BSP_RN.optimality=false;
		}
	}

	private void Sort(ArrayList<Integer> set) 
	{
		QS(magicIndex, 0, magicIndex.size()-1);
	}
	
	
	public int colocar(ArrayList<Integer> e, int b, int t)
	{
	    int i;
	    int pivote;
	    int valor_pivote;
	    int temp;
	   
	    pivote = b;
	    valor_pivote = PulseGraph.vertexes[DataHandler.Arcs[e.get(pivote)][1]].getCompareCriteria(e.get(pivote));
//		valor_pivote = PulseGraph.vertexes[DataHandler.Arcs[e.get(pivote)][1]].getCompareCriteria();
//	    valor_pivote = DataHandler.Distance[e.get(pivote)];
		 for (i=b+1; i<=t; i++){
			 if ( PulseGraph.vertexes[DataHandler.Arcs[e.get(i)][1]].getCompareCriteria(e.get(i)) < valor_pivote){
//	        if (PulseGraph.vertexes[DataHandler.Arcs[e.get(i)][1]].getCompareCriteria() < valor_pivote){
//	        if (DataHandler.Distance[e.get(i)] < valor_pivote){
	      	  		pivote++;    
	                temp= e.get(i);
	                e.set(i, e.get(pivote));
	                e.set(pivote, temp);
	        }
	    }
	    temp=e.get(b);
	    e.set(b, e.get(pivote));
        e.set(pivote, temp);
	    return pivote;
	} 
	 
	 
	 
	public void QS(ArrayList<Integer> e, int b, int t)
	{
		 int pivote;
	     if(b < t){
	        pivote=colocar(e,b,t);
	        QS(e,b,pivote-1);
	        QS(e,pivote+1,t);
	     }  
	}
	
	private boolean CheckDominance(int dist, int time, int h) {
		for (int i = 0; i < PulseGraph.onlineEF.size(); i++) {
			if (PulseGraph.onlineEF.get(i).dist <= dist + PulseGraph.vertexes[h].getMinDist()) {
				if ( PulseGraph.onlineEF.get(i).time <= time + PulseGraph.vertexes[h].getMinTime()) {
					return true;
				}
			}else{
				return false;
			}
			
		}
		return false;
	}
	

	public boolean CheckLabels(double PDist, double PTime) {
		
		for (int i = 0; i < labels.size(); i++) {
			if (labels.get(i).dist<= PDist ) {
				if (labels.get(i).time<=  PTime ) {
					return true;
				}
			}else{
				return false;
			}
		}
		
		return false;
//		for (int i = 0; i < usedLabels; i++) {
//			if (PDist >= distLabel[i] && PTime >= timeLabel[i]) {
//				return true;
//			}
//		}
//		return false;
	}
	
	
	private void changeLabels(int PDist, int PTime) {

		
		if (labels.size() == 0) {
			labels.add(new Label(PDist, PTime, id));
		} else if (labels.size() == 1) {
			labels.add(PDist < labels.get(0).dist ? 0 : 1, new Label(PDist,PTime, id));
		} else {
			if (labels.size() < DataHandler.numLabels) {
				insertLabel(PTime, PDist);
			} else {
				int luck = 1 + DataHandler.r.nextInt(DataHandler.numLabels - 2);
				labels.remove(luck);
				insertLabel(PTime, PDist);
			}
			
		}
	}
	
	/**
	 * Binary insertion for the nodes' labels
	 * @param pTime
	 * @param pDist
	 */
	private void insertLabel(int pTime, int pDist) {
		Label np = new Label(pDist, pTime, id);
		double cScore = np.dist;
		
		boolean cond = true;
		int l = 0;
		int r = labels.size();
		int m = (int) ((l + r) / 2);
		double mVal = 0;
		if (labels.size() == 0) {
			labels.add(np);
			return;
		} else if (labels.size() == 1) {
			mVal = labels.get(m).dist;
			labels.add(cScore < mVal ? 0 : 1, np);
			return;
		} else {
			mVal = labels.get(m).dist;
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
					labels.set(m, np);
					return;
				}
				mVal = labels.get(m).dist;
			} else {
				cond = false;
				if (l == m) {
					labels.add(cScore < mVal ? l : l + 1, np);
				} else if (r == m) {
					System.out.println("esto no pasa ");
					labels.add(cScore < mVal ? r : Math.min(r + 1, labels.size()), np);
				} else {
					System.err.println(VertexPulse.class +  " insert label, error");
				}
				return;

			}
		}
		
		
	}

	public int  getCompareCriteria(Integer arc){
//		return DataHandler.Distance[arc]+getMinDist()+ DataHandler.Time[arc]+getMinTime();
		return  DataHandler.Distance[arc]+getMinDist();

	}


}

