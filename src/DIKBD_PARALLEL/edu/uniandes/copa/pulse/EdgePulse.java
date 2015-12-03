
package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

/**
 * Edge data structure
 *  @author Daniel Duqe, Leonardo Lozano, Andrés Medaglia
 *  d.duque25@uniandes.edu.co
 */
public class EdgePulse {
	
	private int eDist;
	private int eTime;
	
	
	
	private EdgePulse nextE;
	private int id;
	private VertexPulse source;
	private VertexPulse target;
	
	public EdgePulse(int d , int t,  VertexPulse nT, VertexPulse nH, int nid) {
		// TODO Auto-generated constructor stub
		eDist = d;
		eTime = t;
		this.source = nT;
		this.target = nH;
		this.id = nid;
	}
	
	public void addNextCommonTailEdge(EdgePulse e){
		if(nextE!= null){
			nextE.addNextCommonTailEdge(e);
		}
		else{
			nextE = e;
		}
	}

	
	public EdgePulse getNext()
	{
		return nextE;
	}
	public void setNextE(EdgePulse e ){
		nextE = e;
	}
	public int getWeightDist(){
		return eDist;
	}
	public int getWeightTime(){
		return eTime;
	}
	public VertexPulse getSource(){
		return source;
	}
	
	public VertexPulse getTarget(){
		return target;
	}
	public int getID()
	{
		return id;
	}
	public EdgePulse findEdgebyTarget( VertexPulse targetT)
	{
		if(targetT.getID() == this.target.getID())
		{
			return this;
		}else{
			if(nextE!= null)
			{
				return nextE.findEdgebyTarget(targetT);
			}
		}
		return null;
	}
	
	
	public int getCompareCriteria(){
		return target.getMinDist() + target.getMinTime();
	}

	
}
