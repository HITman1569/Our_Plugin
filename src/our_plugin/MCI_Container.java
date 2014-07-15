package our_plugin;

import java.util.ArrayList;

public class MCI_Container {
	public ArrayList<SCC_Template> SCCTemplates;
	public ArrayList<SCCInstanceCustom> SCCInstanceList;
	public ArrayList<String> SCC_Order;		// Holds [SCCID,TOKENINDEX]
	public String FRAME_PATH;
	
	
	public MCI_Container(ArrayList<SCC_Template> TList)
	{
		SCC_Order = new ArrayList();
		SCCTemplates = TList;
	}
	
	public void mapIndexes(String SCInst_CodeSegment)
	{	
		// Get Relevant SCCTemplate //
		String[] array = SCInst_CodeSegment.split(",");
		System.out.println("The required SCCID: " + array[0]);
		int SCCID = Integer.parseInt(array[0]);
		boolean matchFound = false;
		SCC_Template Template = getTemplate(SCCID);
		
		// Analyze Code Segment To Determine SCC_Instance_Index //
		String CodeSegment = SCInst_CodeSegment.substring(array[0].length()+1);
		System.out.println("The obtained Code Segment: ");
		System.out.print(CodeSegment);
		int numValues = Template.DynamicTokens.get(0).getValues().size();
		
		for(int tokenIndex = 0; tokenIndex < numValues; tokenIndex++) // 
		{
			for(int i = 0; i < Template.DynamicTokens.size(); i++)
			{
				DynamicToken Token = Template.DynamicTokens.get(i);
				String TokenValue = Token.tokenValues.get(tokenIndex);
				System.out.println("TokenValue: " + TokenValue);
				System.out.println(Token.tokenIndex);
				System.out.println("#######");
				
				if(CodeSegment.indexOf(TokenValue) != -1)
				{
					System.out.println("Match Found!"); // I should move to next token 
					if(i == Template.DynamicTokens.size()-1)	// If Last!
						matchFound = true;
				}
				else	// Didn't found a match for this tokenIndex, I should continue with the next tokenIndex
					break;
			}
			if(matchFound)
			{
				System.out.println("Token Index For This SCC_Instance: " + tokenIndex);
				SCC_Order.add(SCCID + "," + tokenIndex);
				break;
			}
		}		
	}
	
	public SCC_Template getTemplate(int SCCID)
	{
		for(SCC_Template Template : SCCTemplates)
		{
			if(Template.curSCCID == SCCID)
			{
				return Template;
			}
		}
		return null;
	}
	
}
