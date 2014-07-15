package our_plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SCC_Template {

	String OUTPUT_PATH = "\\VCL_Output\\";
	int curSCCID = -1;
	int curFileID = 0;
	int curClassID = 0;
	int curMethodID = 0;
	int curVariableID = 0;
	int MCC_Type = 0;
	int toOutput = 1;
	ArrayList<ArrayList<String>> InstanceList;
	ArrayList<ArrayList<String>> FormatInstances;
	ArrayList<String> GenericTokens;
	ArrayList<DynamicToken> DynamicTokens;
	
	public SCC_Template(int SCCID, SCC SimpleClone, ArrayList<Integer> MCC_Settings)
	{
		InstanceList = new ArrayList();
		FormatInstances = new ArrayList();
		GenericTokens = new ArrayList();
		DynamicTokens = new ArrayList();
		curSCCID = SCCID;
		
		// Populate MCC_Settings Parameters //
		if(MCC_Settings.size() > 0)
		{
			MCC_Type = MCC_Settings.get(0);
			if(MCC_Type == 1)
			{
				OUTPUT_PATH += "MCC_Template\\";
			}
			toOutput = MCC_Settings.get(1);
		}
		
		populateInstanceList(SimpleClone);
		matchTokens();
		for(int index = 0; index < InstanceList.size(); index++)
			formatTokens(SimpleClone.getClone_instances(), index);
		generateTemplate();
	}
	
	public void formatTokens(ArrayList<SCCInstanceCustom> _InstanceList, int instanceIndex)
	{
		System.out.println("About to format code segment for Instance #" + instanceIndex);
		SCCInstanceCustom Inst = _InstanceList.get(instanceIndex);
		String codeSegment = Inst.getCode();
		ArrayList<String> tokenList = InstanceList.get(instanceIndex);
		ArrayList<String> FormattedList = new ArrayList();
		int startIndex, endIndex, tokenLen, posFrom = 0;
		int indexOf = -1;
		System.out.print(codeSegment);
		
		// Match intermediate tokens //
		for(int i = 0; i < tokenList.size(); i++)
		{
			// Initialize tokens //
			if(i+1 >= tokenList.size())	// Last Index 
				break;

			String token1 = tokenList.get(i);
			String token2 = tokenList.get(i+1);
			startIndex = 0;
			endIndex = 0;
			indexOf = -1;
			tokenLen = 0;
			
			// Find ending index of first token //
			tokenLen = token1.length();
			indexOf = codeSegment.indexOf(token1, posFrom);
			//System.out.println("First index(trying to find: '" + token1 + "') " + indexOf);
			
			if(indexOf != -1)
			{	
				endIndex = indexOf + tokenLen;
				//posFrom = endIndex - 1;
				posFrom = endIndex;
			}
			else
				System.out.println("I could not find the index of token: " + token1);
			
			// Find starting index of second token //
			tokenLen = token2.length();
			indexOf = codeSegment.indexOf(token2, posFrom);
			//System.out.println("Second index(trying to find: '" + token2 + "') " + indexOf);
			
			if(indexOf != -1)
			{
				startIndex = indexOf;
			}
			else
				System.out.println("I could not find the index of token: " + token2);
			
			// Store intermediary information //
			if(endIndex <= startIndex && endIndex >= 0 && startIndex >= 0)
			{
				String tempInfo = codeSegment.substring(endIndex, startIndex);
				FormattedList.add(tempInfo);
				//System.out.println("The intermediary information between '" + token1 + "' and '" + token2 + "' is: ");
				//System.out.println("==|'" + tempInfo + "'|==");
			}
			else
			{
				System.out.println("Out of bound errors in SCC_Template");
			}
		}
		FormattedList.add("");	// To handle some out-of-bounds exceptions
		
		// Formatting Saved in FormattedList //
		System.out.println("Formatting Saved!\n====================");
		FormatInstances.add(FormattedList);
		//for(String token : FormattedList)
		//	System.out.print(token);
		// ================================ //
		
	}

	public void matchTokens()
	{
		boolean firstPass = true;
		int tokenIndex = 0;

		// For all the instances of curSCCID //
		System.out.println("Size: " + InstanceList.size());
		for(int i = 0; i < InstanceList.size(); i++)
		{
			//System.out.println("Current pass is matching instance #:" + i);
			//System.out.println("Current size of dynamic tokens: " + DynamicTokens.size());
			// For the first pass, add all tokens to GenericTokens //
			if(firstPass)
			{
				for(String InstanceToken : InstanceList.get(i))
					GenericTokens.add(InstanceToken);
				firstPass = false;
			}
			else
			{
				tokenIndex = 0;
				for(String InstanceToken : InstanceList.get(i))
				{
					if(!GenericTokens.get(tokenIndex).equals(InstanceToken))	// If tokens differ
					{
						if(GenericTokens.get(tokenIndex).equals("9999")) // If you find 9999 at the index
						{
							// Ignore, All Instances already populated  //
						}
						else
						{
							// Once a dynamic token is detected //
							DynamicToken tk = new DynamicToken();
							tk.tokenIndex = tokenIndex;
							for(int instIndex = 0; instIndex < InstanceList.size(); instIndex++)
								tk.tokenValues.add(InstanceList.get(instIndex).get(tokenIndex));
							DynamicTokens.add(tk);
							
							GenericTokens.set(tokenIndex, "9999");
						}
					}
					tokenIndex++;
				}	
			}
		}
		
		// Set placeholders //
		int varCounter = 0;
		for(int index = 0; index < DynamicTokens.size(); index++)
		{
			DynamicToken tk = DynamicTokens.get(index);
			if(tk.marked)
			{
				GenericTokens.set(tk.tokenIndex, "?@" + tk.varName + "?");
				continue;
			}
			String token = tk.getValues().get(0);
			tk.varName = "SCCID_"+curSCCID+"_variable_" + varCounter;
			
			for(int j = 0; j < DynamicTokens.size(); j++)
			{
				if(DynamicTokens.get(j).getValues().get(0).equals(token) && index != j)
				{
					if(DynamicTokens.get(j).marked == false)
					{
						DynamicTokens.get(j).varName = tk.varName;
						DynamicTokens.get(j).marked = true;
					}
				}
			}
			GenericTokens.set(tk.tokenIndex,"?@" + tk.varName + "?");
			varCounter++;
		}
		
		System.out.println("About to print dynamic tokens:- ");
		for(DynamicToken tk : DynamicTokens)
			tk.printTokens();
	}
	
	public void checkIfExists(String InstanceToken, int tokenIndex)
	{
		for(DynamicToken tk : DynamicTokens)
		{
			if(tk.tokenIndex == tokenIndex)
			{
				tk.tokenValues.add(InstanceToken);
				return;
			}
		}
	}

	public void populateInstanceList(SCC SimpleClone)
	{		
		InstanceList.clear();
		ArrayList<SCCInstanceCustom> AllInstances = SimpleClone.getClone_instances();
		
		for(SCCInstanceCustom Instance : AllInstances)
		{
			InstanceList.add(Instance.getTokens());
		}
	}

	public void generateTemplate()
	{
		String path = Activator.getAbsolutePath(OUTPUT_PATH);
		ArrayList<String> BaseFormat = FormatInstances.get(0);
		try {
			String SPC_PATH = curSCCID + "_SCC_SPC.vcl";
			String VCL_PATH = curSCCID + "_SCC_Frame.vcl";

			refresh(path+SPC_PATH, path+VCL_PATH);	// Delete files if they already exist //

			FileWriter spc_file = new FileWriter(path + SPC_PATH, true);
			FileWriter vcl_file = new FileWriter(path + VCL_PATH, true);
			BufferedWriter spc_output = new BufferedWriter(spc_file);
			BufferedWriter output = new BufferedWriter(vcl_file);

			// Read VariablesList //

			curVariableID = 0;
			ArrayList<String> VariableList = new ArrayList();
			for(int i = 0 ; i < DynamicTokens.size(); i++)
			{
				VariableList.add("variable_" + curVariableID);
				curVariableID++;
			}

			// ====== START OF SPC ===== //

			// Specify SPC details //
			spc_output.write("% SPC file for " + VCL_PATH + "\n\n");

			// The output filename //
			String fileName = "fileName";
			String fileNameValue = "SCCID_" + curSCCID + "_Inst";		// Can ask user to specify method name //
			spc_output.write("#set " + fileName + " = ");
			for(int _index = 0; _index < InstanceList.size(); _index++)
			{
				spc_output.write("\"" +  fileNameValue + "_" + _index + "\"");
				if(_index != InstanceList.size()-1)
					spc_output.write(", ");
				else
					spc_output.write("\n");
			}			

			// Set Variables //
			spc_output.write("% Here I will set the rest of the place-holder variables\n\n");
			int index = 0;
			
			for(DynamicToken tk : DynamicTokens)
			{
				if(tk.marked)
					continue;
				spc_output.write("#set " + tk.varName + " = "); 
				for(int _index = 0; _index < InstanceList.size(); _index++)
				{
					spc_output.write("\"" + tk.tokenValues.get(_index) + "\"");
					if(_index != InstanceList.size()-1)
						spc_output.write(", ");
					else
						spc_output.write("\n");
					
				}
			}
			
			// In While Loop //
			spc_output.write("\n#while ");
			for(DynamicToken tk : DynamicTokens)
			{
				if(tk.marked)
					continue;
				spc_output.write(tk.varName + ", "); 
			}
			spc_output.write("fileName\n");
			
				// Adapt VCL File //
				spc_output.write("#adapt: " + "\"" + VCL_PATH + "\"" + "\n\n");
				// Details about SPC here //
				spc_output.write("% You can specify options for this SPC File here\n\n");
				spc_output.write("#endadapt\n");
			spc_output.write("#endwhile");

			// ====== END OF SPC ======= // 
			// ========================= //
			// ====== START OF VCL ===== //

			// Specify VCL output //
			
			if(toOutput == 1)
			{				
				output.write("#output " + "?@" + fileName + "?" + "\".java\"" + "\n\n"); // Outputs: #output _filename_.java
				if(MCC_Type == 1)
					output.write("#break: break_0\n\n");
			}

			
			// Traversing VCLInputContainer Generic + Variable Lists //
			int varCounter = 0;
			int formatCounter = 0;
			for(String token: GenericTokens)
			{
				output.write(token);
				if(formatCounter < BaseFormat.size()-1)
					output.write(BaseFormat.get(formatCounter));
				else
					System.out.println("Formatting does not exist (out of bounds)");
				formatCounter++;
			}
			
			if(MCC_Type == 1)
				output.write("\n#break: break_" + curSCCID + "\n");

			output.close();
			spc_output.close();
			curClassID++;
			curMethodID++;
			if(MCC_Type != 1)
				RunVCL(SPC_PATH); // Run VCL Processor
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ====== END OF VCL ===== //
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
