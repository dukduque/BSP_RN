package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;

public class DataHandler {
	String CvsInput;
	int NumArcs;
	int LastNode;
	int Source;
	static int[][] Arcs;
	//static Hashtable<String, Integer> arcID;
	
	static int NumNodes;
	static int[] Distance;
	static int[] Time;
	private PulseGraph Gd;
	static Random r = new Random(0);
	public static int numLabels = 20;
	
	/**
	 * 1 Raith & Ehrgott ,2009
	 * 2 DIMACS
	 */
	private int type;
	
	/**
	 * DataHandler builder
	 * @param Instance instance object that contains info about file, muber of arc and nodes and source an sink nodes.
	 * @param nType read format: 1 for Raith & Ehrgott,200 , 2 for DIMACS.
	 */
	public DataHandler(Settings Instance, int nType) {
		CvsInput = Instance.DataFile;
		NumArcs = Instance.NumArcs;
		NumNodes = Instance.NumNodes;
		LastNode = Instance.LastNode;
		Source = Instance.Source;
		Arcs = new int[Instance.NumArcs][2];
		Distance = new int[Instance.NumArcs];
		Time = new int[Instance.NumArcs];
//		arcID = new Hashtable<String, Integer>(NumArcs);
		
		Gd = new PulseGraph(NumNodes);
		type = nType;
	}

	public void ReadC() throws NumberFormatException, IOException {

		File file = new File(CvsInput);

		BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		String line = null;

		String[] readed = new String[10];

		int row = 0;
		int col = 0;
		
		upLoadNodes();
		int fileStep = type==1?3:1;
		
		while ((line = bufRdr.readLine()) != null && row < NumArcs + fileStep) {
			StringTokenizer st = new StringTokenizer(line, " ");
			while (st.hasMoreTokens()) {
				// get next token and store it in the array
				readed[col] = st.nextToken();
				col++;
			}

			if (row >= fileStep) {
				Arcs[row - fileStep][0] = (Integer.parseInt(readed[0]) - 1);
				Arcs[row - fileStep][1] = (Integer.parseInt(readed[1]) - 1);
				Distance[row - fileStep] = Integer.parseInt(readed[2]);
				Time[row - fileStep] = Integer.parseInt(readed[3]);
				String str = Arcs[row - fileStep][0]+","+Arcs[row - fileStep][1];
//				arcID.put( str, row - fileStep);
				Gd.addWeightedEdge( Gd.getVertexByID(Arcs[row - fileStep][0]), Gd.getVertexByID(Arcs[row - fileStep][1]),Distance[row - fileStep], Time[row - fileStep] , row-fileStep);
			}

			col = 0;
			row++;

		}

	
	}
	public void upLoadNodes(){
		for (int i = 0; i < NumNodes; i++) {
			if(i!=(LastNode-1)){
				Gd.addVertex(new VertexPulse(i) );
			}
		}
		FinalVertexPulse vv = new FinalVertexPulse(LastNode-1);
		Gd.addFinalVertex(vv);
	}
	
	public PulseGraph getGd()
	{
		return Gd;
	}
	
}

