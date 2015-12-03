package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * 	Pulse algortihm for the biobjective shortest path problem
 *	Duque, D. , Lozano, L., and Medaglia, A. L.(2015). An exact method for the 
 *  biobjective shortest path problem for large-scale road networks. European 
 *  Journal of Operational Research, 242 (3), 788-797. 
 *  http://dx.doi.org/10.1016/j.ejor.2014.11.003
 *  @author Daniel Duque, Leonardo Lozano, Andrés Medaglia
 *  d.duque25@uniandes.edu.co
 */
public class Main_BSP_RN {

	static long Atime = 0;	
	static boolean  optimality = true;	
	static Path p = Paths.get(System.getProperty("user.dir"));
	public static void main(String[] args) {
		String[][] inst = instances();

		try {

			for (int i = 0; i <inst.length; i++) {
				//IO - Starts
				//Readers (you might put your own reader)
				int logradas = 0;
				int solved = 0;
				int ii = 1;
				String ini;
				String sub = inst[i][0];
				String pre = ".ini";
				BufferedWriter ww = new BufferedWriter(new FileWriter(new File("Result_"+sub+".txt")));
				
				Settings Instance;
				ArrayList<Integer> souces = new ArrayList<Integer>();
				ArrayList<Integer> sinks = new ArrayList<Integer>();
				loadSourceAndSink(sub, souces, sinks);
				int top = souces.size() == 0 ? 9 : souces.size();
				
				System.out.println("---------------Results---------------");
				for (int k = 1; k <= top; k++) {
					String line="";
					if (inst[i][1].equals("R")) {
						ini =  "./ini/" + sub + k + pre;
					} else {
						ini =  "./ini/DIMACS/" + sub + pre;
					}
					optimality = true;
					Instance = new Settings(ini);
					int type = 1;
					if (ini.contains("DIMACS")) {
						Instance.Source = souces.get(k - 1);
						Instance.LastNode = sinks.get(k - 1);
						type = 2;
						if (souces.size() == 0) {
							System.out.println("ERROR READING FILE");
						}
					}

					DataHandler data = new DataHandler(Instance, type);
					data.ReadC();
					System.out.print("Pulse_RN:/" + sub + "" + k + "/source:/" + Instance.Source + "/sink:/" + Instance.LastNode);
					line = "Pulse_RN:/" + sub + "" + k + "/source:/" + Instance.Source + "/sink:/" + Instance.LastNode;
					PulseGraph netWork = data.getGd();
					//IO - ENDS
					
					//Initialization - Starts
					Thread mainThread = Thread.currentThread();
					Thread tTime = new Thread();
					Thread tDist = new Thread();
				
					Atime = System.currentTimeMillis();
					DukqstraDist spDist = new DukqstraDist(netWork,Instance.LastNode - 1);
					DukqstraTime spTime = new DukqstraTime(netWork,Instance.LastNode - 1);

					tDist = new Thread(new ShortestPathTask(1, spDist, null));
					tTime = new Thread(new ShortestPathTask(0, null, spTime));

					tDist.start();
					tTime.start();

					tDist.join();
					tTime.join();

					int MD = netWork.getVertexByID(Instance.Source - 1).getMaxDist();
					int MT = netWork.getVertexByID(Instance.Source - 1).getMaxTime();
				
					ArrayList<Integer> Path = new ArrayList<Integer>();
					int MinD=netWork.getVertexByID(Instance.Source-1).getMinDist();
					int MinT=netWork.getVertexByID(Instance.Source-1).getMinTime();

					netWork.addCorners(MinT, MD, MinD, MT);
					netWork.setMT_MD(Instance.Source-1);
					//Initialization - Ends
					
					//Pulse - Starts
					double pulseStart = System.currentTimeMillis();
					netWork.getVertexByID(Instance.Source-1).pulse(0, 0, Path);
					double pulseEndTime = System.currentTimeMillis();
					//Pulse - Endss
					
					/**
					 * Print results
					 */
					System.out.print("/INI:/"+(pulseStart-Atime)/1000.0);
					line +="/INI:/"+(pulseStart-Atime)/1000.0;
					System.out.print("Pulse:/" + (pulseEndTime-pulseStart)/1000.0);
					line+="Pulse:/" + (pulseEndTime-pulseStart)/1000.0;
					System.out.print("/EXE:/"+(pulseEndTime-Atime)/1000.0);
					line +="/EXE:/"+(pulseEndTime-Atime)/1000.0;
					System.out.print("/X:/"+ netWork.getEF().size()+ "/optimaliy:/"+ optimality + "\n");
					line+="/X:/"+ netWork.getEF().size()+ "/optimaliy:/"+ optimality +"/";
					line+= PulseGraph.onlineEF+"\n";
					//System.out.println(PulseGraph.onlineEF);
					
					ww.write(line);
					line="";
					solved = k;
					if (optimality) {
						logradas++;
					}
					if(logradas>=30){
						System.out.println("All optimal");
						k=10000;
						ii = 10000;
					}
					
					//Reset every thing
					PulseGraph.vertexes = null;
					PulseGraph.onlineEF = null;
					data.Arcs = null;
					data.Distance = null;
					data.Time = null;
					data = null;
					netWork = null;
					System.gc();
					
					if (k == top && ii <= 0) {
						logradas=0;
						k = 0;
						ii++;
						System.out.println("---------------Results---------------");
					}
					
				}
				ww.close();
				System.out.println("Solved " + logradas +" out of " + solved + " \n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String[][] instances() {
		String[][] ins = new String[13][3];
		
		ins[0][0]="DC";ins[0][1]="R";
		ins[1][0]="RI";ins[1][1]="R";
		ins[2][0]="NJ";ins[2][1]="R";
		ins[3][0]="NY";ins[3][1]="D";
		ins[4][0]="BAY";ins[4][1]="D";
		ins[5][0]="COL";ins[5][1]="D";
		ins[6][0]="FLA";ins[6][1]="D";
		ins[7][0]="NW";ins[7][1]="D";
		ins[8][0]="NE";ins[8][1]="D";
		ins[9][0]="CAL";ins[9][1]="D";
		ins[10][0]="LKS";ins[10][1]="D";
		ins[11][0]="E";ins[11][1]="D";
		ins[12][0]="W";ins[12][1]="D";
		
		
		return ins;
	}

	private static void loadSourceAndSink(String sub, ArrayList<Integer> souces, ArrayList<Integer> sinks) throws NumberFormatException, IOException {
		File file = new File(p.getParent() + "/ini/DIMACS/" + sub + ".startEnd.ini");
		if (file.exists()) {
			BufferedReader bufRdr = new BufferedReader(new FileReader(file));
			String line = null;

			while ((line = bufRdr.readLine()) != null) {
				String[] splited = line.split(",");
				int source = Integer.parseInt(splited[1]);
				int sink = Integer.parseInt(splited[2]);
				souces.add(source);
				sinks.add(sink);
			}
		}
	}
}
