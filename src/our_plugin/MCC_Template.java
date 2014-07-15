package our_plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import our_plugin.handlers.SampleHandler;

public class MCC_Template {

	final String OUTPUT_PATH = "\\VCL_Output\\MCC_Template\\";
	int curMCCID = -1;
	int curFileID = 0;
	int curMethodID = 0;
	int curVariableID = 0;
	ArrayList<String> FramePaths;
	ArrayList<Integer> MCC_Settings;
	ArrayList<MethodCloneInstance> MCI_List;
	ArrayList<MCI_Container> MCI_Container_List;
	ArrayList<SCCInstanceCustom> SCCInstanceList;
	ArrayList<SCC_Template> SCCTemplates;
	
	public MCC_Template(int MCCID, MethodClone MCC)
	{
		SCCInstanceList = new ArrayList();
		FramePaths = new ArrayList();
		SCCTemplates = new ArrayList();
		MCC_Settings = new ArrayList();
		MCI_List = MCC.getMethod_Clones();
		MCI_Container_List = new ArrayList();
		MCC_Settings.add(1); MCC_Settings.add(1);
		// MCC_Type = 1, toOutput = 1 //
		
		curMCCID = MCCID;
		
		extractSimpleClones(MCC);
		generateTemplate();
	}
	
	public void extractSimpleClones(MethodClone MCC)
	{
		boolean firstPass = true;
		
		
		for(int SCCID : MCC.getSCCIDs())
		{
			// Create frames for all required SCCIDs //
			String path = Activator.getAbsolutePath(OUTPUT_PATH);
			String filename = SCCID + "_Frame.vcl";
			SCC_Template template = null;
			if(!firstPass)
				MCC_Settings.set(1, 0);
			
			SCC SimpleClone = findSCC(SCCID);
			if(SimpleClone != null)
				template = new SCC_Template(SCCID, SampleHandler.f.SCCCLONES.get(SCCID), MCC_Settings);
			else
				System.out.println("Critical error, cannot find SCC!");
			SCCTemplates.add(template);
			FramePaths.add(path+filename);
		}
		// All Frames Of SCCs Obtained! //
		System.out.println("I have all the SCC_Templates!");
		
		// Spend A Little Time Extracting SCCID List From New Analysis List //
		int MCI_Index = 0;
		int counter = 0;
		for(MethodCloneInstance MCI : MCI_List)
		{
			MCI_Container container = new MCI_Container(SCCTemplates);
			counter = 0;
			System.out.println("*******MCI #" + MCI_Index + "*******");			
			for(String codeSegment : MCI.getAnalysisDetails())
			{
				if(counter % 2 != 0)	// If Odd Index Encountered //
				{
					// Give This Portion To MCI_Container //
					container.mapIndexes(codeSegment);
				}
				counter++;
			}
			MCI_Container_List.add(container);
			MCI_Index++;
			System.out.println("************************");
		}
		System.out.println("Done processing");
	}
	
	public SCC findSCC(int SCCID)
	{
		ArrayList<SCC> SCCList = SampleHandler.f.SCCCLONES;
		for(SCC SimpleClone : SCCList)
		{
			if(SimpleClone.getSCCID() == SCCID)
				return SimpleClone;
		}
		return null;
	}
	

	public void populateLists(MethodCloneInstance MCCInstance)
	{		
		// Match Method in the MCCInstance with the corresponding SCCInstance! //
		// Assuming MCCInstance has some information regarding SCCInstance //
		
		
	}

	public void generateTemplate()
	{
		String path = Activator.getAbsolutePath(OUTPUT_PATH);
		
		curFileID = curMCCID;
		try {
			String SPC_PATH = curFileID + "_MCC_SPC.vcl";
			String VCL_PATH = curFileID + "_MCC_Frame.vcl";

			refresh(path+SPC_PATH, path+VCL_PATH);	// Delete files if they already exist //

			FileWriter spc_file = new FileWriter(path + SPC_PATH, true);
			FileWriter vcl_file = new FileWriter(path + VCL_PATH, true);
			BufferedWriter spc_output = new BufferedWriter(spc_file);
			BufferedWriter output = new BufferedWriter(vcl_file);

			// ====== START OF SPC ===== //
			// Specify SPC details //
			
			spc_output.write("% SPC file for " + VCL_PATH + "\n\n");
			
			// The output filename //
			String fileName = "fileName";
			String fileNameValue = "MCCID_" + curMCCID + "_Inst";		// Can ask user to specify method name //
			spc_output.write("#set " + fileName + " = ");
			for(int _index = 0; _index < MCI_List.size(); _index++)
			{
				spc_output.write("\"" +  fileNameValue + "_" + _index + "\"");
				if(_index != MCI_List.size()-1)
					spc_output.write(", ");
				else
					spc_output.write("\n");
			}
			
			// Set Variables For Each SCC Instance //
			spc_output.write("% Here I will set all the place-holder variables\n\n");
			int counter = 0;
			for(MCI_Container Container: MCI_Container_List)
			{
				for(String SCCInfo : Container.SCC_Order)
				{
					String[] InfoArray = SCCInfo.split(",");
					int SCCID = Integer.parseInt(InfoArray[0]);
					int TokenIndex = Integer.parseInt(InfoArray[1]);
					SCC_Template Template = Container.getTemplate(SCCID);
					
					int index = 0;
					
					for(DynamicToken tk : Template.DynamicTokens)
					{
						if(tk.marked)
							continue;
						spc_output.write("#set " + tk.varName + " = "); 
						for(int _index = 0; _index < MCI_List.size(); _index++)
						{
							spc_output.write("\"" + tk.tokenValues.get(_index) + "\"");
							if(_index != MCI_List.size()-1)
								spc_output.write(", ");
							else
								spc_output.write("\n");
						}
					}
					
					
				}
			}
			
			// All Variables Are Set //
			
			// In While Loop //
			spc_output.write("\n#while ");
			
			for(SCC_Template Template : SCCTemplates)
			{
				for(DynamicToken tk : Template.DynamicTokens)
				{
					if(tk.marked)
						continue;
					spc_output.write(tk.varName + ", "); 
				}
				spc_output.write("fileName\n\n");				
			}

			spc_output.write("	#select fileName\n");
	
			// Insert First Gapped Region //
			for(int numInst = 0; numInst < MCI_List.size(); numInst++)
			{
				spc_output.write("		#option " +  fileNameValue + "_" + numInst + "\n");
				spc_output.write("			#insert-after break_0:\n");
				spc_output.write(MCI_List.get(numInst).getAnalysisDetails().get(0));
				spc_output.write("		#endinsert\n");
				spc_output.write("		#endoption\n");
			}
			spc_output.write("	#endselect\n");
			
			ArrayList<String> BaseFormat = MCI_List.get(0).getAnalysisDetails();
			int BaseFormatCounter = 0;
			
			// Adapt VCL File(s) //
			for(int index = 0; index < BaseFormat.size(); index++)
			{
				if(index % 2 != 0)	// Adapt XX_SCC_Frame
				{
					String FRAME_PATH = BaseFormat.get(index).split(",")[0] + "_SCC_Frame.vcl";
					spc_output.write("#adapt: " + "\"" + FRAME_PATH + "\"" + "\n");
					
					spc_output.write("	#select fileName\n");
					for(int numInst = 0; numInst < MCI_List.size(); numInst++)
					{
						spc_output.write("		#option " +  fileNameValue + "_" + numInst + "\n");
						spc_output.write("			#insert-after break_" + BaseFormat.get(index).split(",")[0] + ":\n");
						if(index+1 == BaseFormat.size()-1) // If Last Index 
						{
							spc_output.write(MCI_List.get(numInst).getAnalysisDetails().get(index+1));
						}
						else
						{
							String[] tempArray = MCI_List.get(numInst).getAnalysisDetails().get(index+1).split(",");
							int len = tempArray[0].length() + tempArray[1].length() + tempArray[2].length() + tempArray[3].length() + tempArray[4].length() + 5;							
							spc_output.write(MCI_List.get(numInst).getAnalysisDetails().get(index+1).substring(len));
						}
						spc_output.write("			#endinsert\n");
						spc_output.write("		#endoption\n");
					}
					spc_output.write("	#endselect\n");
					spc_output.write("#endadapt\n");
				}				
			}
			
			spc_output.write("#endwhile");
			spc_output.close();
			
			RunVCL(".\\MCC_Template\\" + SPC_PATH); // Run VCL Processor

		}
		catch(Exception e)
		{
			
		}
	}
	
	public void RunVCL(String filename)
	{
		Initializer.ExecuteVCL(filename);
	}

	private static void refresh(String SPC_path, String VCL_path) {
		File f1 = new File(SPC_path);
		File f2 = new File(VCL_path);
		f1.delete();
		f2.delete();
	}

}
