package our_plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import our_plugin.handlers.SampleHandler;

public class MethodsAnalyser{
	
	public MethodsAnalyser() throws IOException
	{
		Analyze(SampleHandler.f.MCCCLONES); // Analyse the universal list of all method clones
	}
	
	private void Analyze(ArrayList<MethodClone> methodclones) throws IOException // Analyse the universal list of all method clones
	{
		for( MethodClone mc : methodclones)
		{
			analyzeMethodClone(mc); // Now Analyse each Method clone set ( MCC CLUSTER )
		}
	}
	
	private void analyzeMethodClone(MethodClone mc) throws IOException
	{
		ArrayList<MethodCloneInstance> methodcloneinstances = mc.getMethod_Clones();
		for( MethodCloneInstance mci : methodcloneinstances)
		{
			analyzeMethodCloneInstance(mci, mc); // Now analyze each Method clone instance
		}
	}
	
	private void analyzeMethodCloneInstance(MethodCloneInstance mci, MethodClone parentClone) throws IOException // This method will analyze each method clone instances to find out the different gaps 
	// overlaps between consecutive SCC instances
	{
		String path=Parser.getFile(mci.getMethod().getFile_number()); // The path to the file which contains the method clone instance;
		SortMethodCloneInstance(mci);
		AnalyzeForOverlaps(mci); // check all the partial,complete overlaps for this specific instance
		GraphSolver.SolveGraph(mci);
		mci.getAnalysisDetails().clear();
		for(SCCInstanceCustom k : mci.getNewSccs_Contained())
		{
			mci.getAnalysisDetails().add(" ");
			mci.getAnalysisDetails().add(Integer.toString(k.getSCCID()) + "," + k.getCode());//+ "," + Parser.getCodeSegment(path, start_line, start_col, end_line, end_col));
		}
		mci.getAnalysisDetails().add(" "); // THE LAST INDEX THAT WILL HOLD THE CODE AFTER LAST SSC and till the end of method
		try {
			if(mci.getMethod().getStart_line()==mci.getNewSccs_Contained().get(0).getStart_line())
			{
				String completelinecode=Parser.getCodeSegment(path,mci.getMethod().getStart_line(),1,mci.getMethod().getStart_line()+1,1,null);
				//String scclinecode = Parser.getCodeSegment(path,getSCC(SortedSCCIDS.get(0),mci).getStart_line(),getSCC(SortedSCCIDS.get(0),mci).getStart_col(),getSCC(SortedSCCIDS.get(0),mci).getStart_line()+1,1);
				String stringtobeadded=completelinecode.substring(0,mci.getNewSccs_Contained().get(0).getStart_col()-1);
				mci.getAnalysisDetails().set(0, stringtobeadded);
			}
			else
			{
				mci.getAnalysisDetails().set(0,Parser.getCodeSegment(path,mci.getMethod().getStart_line(),1,mci.getNewSccs_Contained().get(0).getStart_line(),mci.getNewSccs_Contained().get(0).getStart_col()-1,null));
			}
			// The above line adds the code to the first index of Analysis details which is the code from the start of the method till the start of the first sccid;
			mci.getAnalysisDetails().set(mci.getAnalysisDetails().size()-1,Parser.getCodeSegment(path,getSCCInstanceWithLastLine(mci).getEnd_line(),getSCCInstanceWithLastLine(mci).getEnd_col()+1,mci.getMethod().getEnd_line()+1,1,null));
			// the above line adds the last piece of code to the last index of the getAnalysisDetails() that has the code which starts from the end of the last SCC INSTANCE in the method till the end of the method
			int currIndex=2;
			for(int i=2;i<mci.getAnalysisDetails().size()-2;i=i+2) // This loops now fills the in between gaps between each sccinstances with gaps or partial overlaps markers
			{
				SCCInstanceCustom PrevInstance = getInstance(mci.getAnalysisDetails().get(currIndex-1),mci);
				SCCInstanceCustom NextInstance = getInstance(mci.getAnalysisDetails().get(currIndex+1),mci);
				boolean check = checkInBetweenSCCInstances(mci,currIndex,PrevInstance,NextInstance,path); // if check is false , it means there is complete overlap\
				if(check==true) // if check is false it means that a complete overlap is detected and we don't want to increment our current pointer
				{
					currIndex=currIndex+2;
				}
				if(currIndex>mci.getAnalysisDetails().size()-3)
				{
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void AnalyzeForOverlaps(MethodCloneInstance mci) {
		// TODO Auto-generated method stub
		int[][] temp=new int[mci.getSCCs().size()][mci.getSCCs().size()];
		mci.setAdjeceny_Matrix_For_Overlaps(temp);
		Adjency_Matrix_Initializer(mci.getAdjeceny_Matrix_For_Overlaps(),mci);
	}

	private void Adjency_Matrix_Initializer(int[][] adjeceny_Matrix_For_Overlaps, MethodCloneInstance mci) {
		// TODO Auto-generated method stub
		for(int i = 0; i < mci.getSCCs().size();i++)
		{
			for(int j = i ; j < mci.getSCCs().size();j++)
			{
				CompareSCCInstances(adjeceny_Matrix_For_Overlaps, mci.getSCCs().get(i),mci.getSCCs().get(j),i,j);
			}
		}
	}

	private void CompareSCCInstances(int[][] matrix, SCCInstanceCustom prevInstance,
			SCCInstanceCustom nextInstance, int index1, int index2) {
		// TODO Auto-generated method stub
		if(index1==index2)
		{
			matrix[index1][index2]=0;
			return;
		}
		else if(checkForGap(prevInstance,nextInstance)) // GAP DETECTED
		{
			matrix[index1][index2]=1;
			matrix[index2][index1]=1;
			return;
		}
		else // OVERLAP DETECTED
		{
			matrix[index1][index2]=0;
			matrix[index2][index1]=0;
			return;
		}
	}

	private boolean checkForGap(SCCInstanceCustom prevInstance,
			SCCInstanceCustom nextInstance) { // THIS FUNCTION CHECKS FOR GAPS BETWEEN TWO CONSECTUVE SCC INSTANCES
		// TODO Auto-generated method stub
		if(prevInstance.getEnd_line()<nextInstance.getStart_line()) // GAPPED CLONES WITH ATLEAST 1 LINE IN BETWEEN THEM
		{
			return true;
		}
		else if(prevInstance.getEnd_line()==nextInstance.getStart_line())
		{
			if(prevInstance.getEnd_col()<nextInstance.getStart_col())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}

	private boolean checkInBetweenSCCInstances(MethodCloneInstance mci, int index,
			SCCInstanceCustom prevInstance, SCCInstanceCustom nextInstance, String path) throws IOException {
		// TODO Auto-generated method stub
		/*if(prevInstance.getStart_line() == nextInstance.getStart_line()) // if both scc instances start at the same line
		{
			if(prevInstance.getStart_col()== nextInstance.getStart_col()) // if both scc instances start at the same column
			{
				if(prevInstance.getEnd_line()>nextInstance.getEnd_line()) // if the next instance is a subset of the previous sc instance ( complete overlap
				{
					// swap places now 
					String temp=mci.getAnalysisDetails().get(index+1);
					mci.getAnalysisDetails().set(index+1,mci.getAnalysisDetails().get(index-1));
					mci.getAnalysisDetails().set(index-1,temp);
					return false;
				}
				else if(prevInstance.getEnd_line()<nextInstance.getEnd_line())
				{
					mci.getAnalysisDetails().set(index,"PARTIALOVERLAP");
					return true;
				}
			}
		}
		else */if(prevInstance.getEnd_line()>=nextInstance.getEnd_line()) // COMPLETE OVERLAP
		{
			mci.getAnalysisDetails().remove(index+1);
			mci.getAnalysisDetails().remove(index+1);
			return false;
		}
		else if(prevInstance.getEnd_line()>nextInstance.getStart_line()) // PARTIAL OVERLAP
		{
			mci.getAnalysisDetails().set(index,"PARTIALOVERLAP");
			int x=0;
			while(x<100)
			{
				System.out.println("hehehehehehe1");
			}
			return true;
		}
		else if(prevInstance.getEnd_line()==nextInstance.getStart_line())// SAME LINE
		{
			if(prevInstance.getEnd_col()>nextInstance.getStart_col())// PARTIAL OVERLAP ON SAME ENDING LINES 
			{
				mci.getAnalysisDetails().set(index,"PARTIALOVERLAP");
				int x=0;
				while(x<100)
				{
					System.out.println("hehehehehehe1");
				}
				return true;
			}
			else if(prevInstance.getEnd_col()<nextInstance.getStart_col())// GAP ON SAME LINE BETWEEN CONSECUTIVE SCC INSTANCES
			{
				int effectivestartcol=prevInstance.getEnd_col()+1;
				int effectivecol=nextInstance.getStart_col()-1;
				mci.getAnalysisDetails().set(index,("GAP" + "," + prevInstance.getEnd_line() + "," + effectivestartcol + "," + nextInstance.getStart_line()
						+ ","+ effectivecol + "," + Parser.getCodeSegment(path, prevInstance.getEnd_line(), effectivestartcol, nextInstance.getStart_line(), effectivecol,null)));
				return true;
//				mci.getAnalysisDetails().set(index,("GAP" + "," + effectivestartcol + "," + prevInstance.getEnd_col() + "," + nextInstance.getStart_line()
	//					+ ","+ effectivecol + "," + Parser.getCodeSegment(path, prevInstance.getEnd_line(), effectivestartcol, nextInstance.getStart_line(), effectivecol,null)));
//				return true;
			}
			else // The TWO SCCINSTANCES HAVE NOTHING IN BETWEEN THEM AS THE SECOND STARTS RIGHT AFTER THE FIRST ONE
			{
				mci.getAnalysisDetails().set(index,"");
				return true;
			}
		}
		else if(prevInstance.getEnd_line()<nextInstance.getStart_line()) // GAPPED CLONES WITH ATLEAST 1 LINE IN BETWEEN THEM
		{
			int effectivestartcol=prevInstance.getEnd_col()+1;
			int effectivecol=nextInstance.getStart_col()-1;
			mci.getAnalysisDetails().set(index,("GAP" + "," + prevInstance.getEnd_line() + "," + effectivestartcol + "," + nextInstance.getStart_line()
					+ ","+ effectivecol + "," + Parser.getCodeSegment(path, prevInstance.getEnd_line(), effectivestartcol, nextInstance.getStart_line(), effectivecol,null)));
			return true;
		}
		return true;
		
	}

	private SCCInstanceCustom getInstance(String string, MethodCloneInstance mci) {
		// TODO Auto-generated method stub
		String[] parts = string.split(",");
		SCCInstanceCustom scc = getSCC(Integer.parseInt(parts[0]),mci);
		return scc;
		
	}

	private SCCInstanceCustom getSCCInstanceWithLastLine(MethodCloneInstance mci)
	{
		int minimum=0;
		SCCInstanceCustom last=null;
		for(SCCInstanceCustom scc : mci.getNewSccs_Contained())
		{
			if(scc.getEnd_line()>minimum)
			{
				minimum=scc.getEnd_line();
				last=scc;
			}
		}
		return last;
	}
	
	private SCCInstanceCustom getSCC(int sccid, MethodCloneInstance mci)
	{
		for(SCCInstanceCustom scc : mci.getNewSccs_Contained())
		{
			if(sccid==scc.getSCCID())
			{
				return scc;
			}
		}
		return null;
	}

	private void SortMethodCloneInstance(MethodCloneInstance mci) {// This method will sort the SCCIDS according to their startlines
		// TODO Auto-generated method stub
		//List<SCCInstanceCustom> sortedlist=new ArrayList<>();
		/*ArrayList<Integer> temporary = new ArrayList<>();
		GetStartLines(temporary, mci);
		sortedlist.clear();
		addSortedIDList(sortedlist,temporary, mci);
		temporary.clear();*/
		//sortedlist=mci.getSCCs().subList(0,mci.getSCCs().size()-1);
		Collections.sort(mci.getSCCs());
		//return Collections.sort(mci.getSCCs());
	}

	/*private void addSortedIDList(ArrayList<Integer> sortedIDs,
			ArrayList<Integer> sortedLines, MethodCloneInstance mci) { // This function will insert the relevant SCCIDS in the array according to the sorted start lines
		// TODO Auto-generated method stub
		for(int line : sortedLines)
		{
			for(SCCInstanceCustom inst : mci.getSCCs())
			{
				if(line == inst.getStart_line())
				{
					sortedIDs.add(inst.getSCCID());
				}
			}
		}
	}

	private void GetStartLines(ArrayList<Integer> startlines , MethodCloneInstance mci) { // This Method will set the start lines of the SCCIDS
		// TODO Auto-generated method stub
		for(SCCInstanceCustom inst : mci.getSCCs())
		{
			startlines.add(inst.getStart_line());
		}
	}*/
}
