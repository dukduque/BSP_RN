package DIKBD_PARALLEL.edu.uniandes.copa.pulse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Settings {

	String DataFile;
	int NumArcs;
	int NumNodes;
	int LastNode;
	int Source;
	
	public Settings(String ConfigFile) throws IOException{
		
		File file = new File(ConfigFile);
		 
		BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
		String line = null;
		
		String [][] readed = new String [5][2];
		
		int row = 0;
		int col = 0;
	 
		//read each line of text file
		while((line = bufRdr.readLine()) != null && row < 5)
		{	
		StringTokenizer st = new StringTokenizer(line,":");
		while (st.hasMoreTokens())
		{
			//get next token and store it in the array
			readed[row][col] = st.nextToken();
			col++;
			
		}
		col = 0;
		row++;
		
		}
		Path p = Paths.get(System.getProperty("user.dir"));
		
		DataFile= p.getParent()+ "/" + readed[0][1];
		NumArcs=Integer.parseInt(readed[1][1]);
		NumNodes=Integer.parseInt(readed[2][1]);
		Source=Integer.parseInt(readed[3][1]);
		LastNode=Integer.parseInt(readed[4][1]);		 
		
		
		
		
	}
	
	
}
