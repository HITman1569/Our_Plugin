package our_plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import our_plugin.handlers.SampleHandler;

public class MethodCloneInstance {

	private double coverage;
	private int MethodCloneID;
	private MethodInstance Clone;
	private ArrayList<String> AnalysisDetails;
	private ArrayList<SCCInstanceCustom> SCCs_Contained;
	private ArrayList<SCCInstanceCustom> newSccs_Contained;
	private int[][] Adjeceny_Matrix_For_Overlaps;

	public MethodCloneInstance(int mCCID, double coverage2,
			MethodInstance methodInstance) {
		// TODO Auto-generated constructor stub
		Adjeceny_Matrix_For_Overlaps=null;
		coverage = coverage2;
		MethodCloneID = mCCID;
		Clone = methodInstance;
		AnalysisDetails = new ArrayList<>();
		SCCs_Contained = new ArrayList<>();
		newSccs_Contained = new ArrayList<>();
	}

	public void addSCCs_Contained(SCCInstanceCustom sccInstanceCustom) {
		// TODO Auto-generated method stub
		this.SCCs_Contained.add(sccInstanceCustom);
	}

	public double getCoverage() {
		return coverage;
	}

	public void setCoverage(double coverage) {
		this.coverage = coverage;
	}

	public int getMethodCloneID() {
		return MethodCloneID;
	}

	public void setMethodCloneID(int methodCloneID) {
		MethodCloneID = methodCloneID;
	}

	public MethodInstance getMethod() {
		return Clone;
	}

	public void setClone(MethodInstance clone) {
		Clone = clone;
	}

	public ArrayList<String> getAnalysisDetails() {
		return AnalysisDetails;
	}

	public void setAnalysisDetails(ArrayList<String> analysis) {
		AnalysisDetails = analysis;
	}

	public ArrayList<SCCInstanceCustom> getSCCs() {
		return SCCs_Contained;
	}

	public void setSCCs(ArrayList<SCCInstanceCustom> sCCs) {
		SCCs_Contained = sCCs;
	}
	
	public void DisplayAnalysisDetails() throws IOException {
		// TODO Auto-generated method stub
		
		/*if(this.getSCCs().size()==1)
		{*/
			String path = "\\My Output\\";
			path = Activator.getAbsolutePath(path);
			FileWriter name = new FileWriter(path + "fahad1.txt", true);
			BufferedWriter out = new BufferedWriter(name);
			out.write(this.getMethod().getCode());
			out.write("************THIS IS THE END OF THE METHOD*****************");
			out.write("\n");
			for(SCCInstanceCustom instance : this.getSCCs())
			{
				out.write(instance.getCode());
				out.write("***********THIS IS THE END OF THIS SCC CLONE INSTANCE CODE**************");
			}
			out.write("\n");
			for(SCCInstanceCustom instance : getNewSccs_Contained())
			{
				out.write(instance.getCode());
				out.write("***********THIS IS THE END OF THIS NEW SCC CLONE INSTANCE CODE**************");
			}
			
			for(int i=0;i<getAnalysisDetails().size();i++)
			{
				out.write("\n");
				out.write("AT INDEX : " + i + " : " + getAnalysisDetails().get(i));
			}
			out.write("****************** THATS IT FOR THIS SPECIFIC METHOD CLONE INSTANCE*********************");
			out.write("\n");	
			out.close();
			//}
		/*if(this.getSCCs().size()==2)
		{
			String path = "\\My Output\\";
			path = Activator.getAbsolutePath(path);
			FileWriter name = new FileWriter(path + "fahad2.txt", true);
			BufferedWriter out = new BufferedWriter(name);
			out.write(this.getMethod().getCode());
			out.write("************THIS IS THE END OF THE METHOD*****************");
			out.write("\n");
			for(SCCInstanceCustom instance : this.getSCCs())
			{
				out.write(instance.getCode());
				out.write("***********THIS IS THE END OF THIS SCC CLONE INSTANCE CODE**************");
			}
			out.write("\n");
			for(SCCInstanceCustom instance : getNewSccs_Contained())
			{
				out.write(instance.getCode());
				out.write("***********THIS IS THE END OF THIS NEW SCC CLONE INSTANCE CODE**************");
			}
			
			for(int i=0;i<getAnalysisDetails().size();i++)
			{
				out.write("\n");
				out.write("AT INDEX : " + i + " : " + getAnalysisDetails().get(i));
			}
			out.write("****************** THATS IT FOR THIS SPECIFIC METHOD CLONE INSTANCE*********************");
			out.write("\n");	
			out.close();
		}
		
		if(this.getSCCs().size()>2)
		{
			String path = "\\My Output\\";
			path = Activator.getAbsolutePath(path);
			FileWriter name = new FileWriter(path + "fahad3.txt", true);
			BufferedWriter out = new BufferedWriter(name);
			out.write(this.getMethod().getCode());
			out.write("************THIS IS THE END OF THE METHOD*****************");
			out.write("\n");
			for(SCCInstanceCustom instance : this.getSCCs())
			{
				out.write(instance.getCode());
				out.write("***********THIS IS THE END OF THIS SCC CLONE INSTANCE CODE**************");
			}
			out.write("\n");
			for(SCCInstanceCustom instance : getNewSccs_Contained())
			{
				out.write(instance.getCode());
				out.write("***********THIS IS THE END OF THIS NEW SCC CLONE INSTANCE CODE**************");
			}
			
			for(int i=0;i<getAnalysisDetails().size();i++)
			{
				out.write("\n");
				out.write("AT INDEX : " + i + " : " + getAnalysisDetails().get(i));
			}
			out.write("****************** THATS IT FOR THIS SPECIFIC METHOD CLONE INSTANCE*********************");
			out.write("\n");	
			out.close();
		}*/
		
	}

	public int[][] getAdjeceny_Matrix_For_Overlaps() {
		return Adjeceny_Matrix_For_Overlaps;
	}

	public void setAdjeceny_Matrix_For_Overlaps(
			int[][] adjeceny_Matrix_For_Overlaps) {
		Adjeceny_Matrix_For_Overlaps = adjeceny_Matrix_For_Overlaps;
	}

	public ArrayList<SCCInstanceCustom> getNewSccs_Contained() {
		return newSccs_Contained;
	}

	public void setNewSccs_Contained(ArrayList<SCCInstanceCustom> newSccs_Contained) {
		this.newSccs_Contained = newSccs_Contained;
	}

}
