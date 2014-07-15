package our_plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import our_plugin.handlers.SampleHandler;

public class File_Reader {

	public ArrayList<SCC> SCCCLONES;
	public ArrayList<MethodInstance> METHODS;
	public ArrayList<MethodClone> MCCCLONES;

	public File_Reader() {
		SCCCLONES = new ArrayList<>();
		METHODS = new ArrayList<>();
		MCCCLONES = new ArrayList<>();
	}

	public void run() throws Exception {
		System.out.println("Refreshing files...");
		Parser.refresh();
		System.out.println("Parsing clones.txt...");
		Parser.parseClones(Directories.CLONESFILE);
		System.out.println("Parsing combinedtokens.txt...");
		Parser.parseCombinedTokens(Directories.COMBINEDTOKENSFILE);
		System.out.println("Parsing methodsinfo.txt...");
		Parser.parseMethodsInfo(Directories.METHODSINFOFILE);
		System.out.println("Parsing clonesbymethod.txt...");
		Parser.parseClonesByMethod(Directories.CLONESBYMETHODFILE);
		System.out.println("Updating methods with code....");
		Parser.updateMETHODS(METHODS);
		System.out.println("Parsing Method Clusters...");
		Parser.parseMethodClustersXX(Directories.METHODCLUSTERSXX);
		// System.out.println("Parsing finished....Displaying data...");
		populateSCCs();
	 	new MethodsAnalyser();
	 	System.out.println("***** TESTING ANALYZER NOW , CHECK OUTPUT FOLDER ********...");
		testAnalyzer();
		System.out.println("***** ANALYZER TESTING FINISHED ******");
		 display(SCCCLONES);
//		 DisplayMethods();
//		 DisplayMethodClones();
		System.out.println("All Done....Starting GUI");
		//UI_Start.run();
		//wizard.Our_Wizard;
	}

	private void testAnalyzer() throws IOException {
		// TODO Auto-generated method stub
		String path = "\\My Output\\";
		path = Activator.getAbsolutePath(path);
		File f1 = new File(path+"fahad2.txt");
		File f2 = new File(path+"fahad1.txt");
		File f3 = new File(path+"fahad3.txt");
		f1.delete();
		f3.delete();
		f2.delete();
		int count=0;
		for (MethodClone mc : MCCCLONES) {
			if(count == 4)
			{
				for (MethodCloneInstance mci : mc.getMethod_Clones()) {
					
					mci.DisplayAnalysisDetails();
				}				
			}
			count++;
		}
	}

	void populateSCCs() {
		// TODO Auto-generated method stub
		for (int i = 0; i < SampleHandler.f.MCCCLONES.size(); i++) {

			for (int j = 0; j < SampleHandler.f.MCCCLONES.get(i)
					.getMethod_Clones().size(); j++) {
				for (int k = 0; k < SampleHandler.f.MCCCLONES.get(i)
						.getSCCIDs().size(); k++) {
					int SCID = SampleHandler.f.MCCCLONES.get(i).getSCCIDs()
							.get(k);
					int fnum = SampleHandler.f.MCCCLONES.get(i)
							.getMethod_Clones().get(j).getMethod()
							.getFile_number();
					String name = SampleHandler.f.MCCCLONES.get(i)
							.getMethod_Clones().get(j).getMethod().getName();
					int sl = SampleHandler.f.MCCCLONES.get(i)
							.getMethod_Clones().get(j).getMethod()
							.getStart_line();
					int el = SampleHandler.f.MCCCLONES.get(i)
							.getMethod_Clones().get(j).getMethod()
							.getEnd_line();
					// System.out.println("SCID: " + SCID + " fnum: " + fnum +
					// " name: " + name);
					SCCInstanceCustom add = getSCCInst(SCID, fnum, sl, el);
					SampleHandler.f.MCCCLONES.get(i).getMethod_Clones().get(j)
							.addSCCs_Contained(add);
					// System.out.println(add.getFile_number());
				}
			}
		}
	}

	private SCCInstanceCustom getSCCInst(int sCCID, int fid, int sl, int el) {
		for (int i = 0; i < SampleHandler.f.SCCCLONES.size(); i++) {
			for (int j = 0; j < SampleHandler.f.SCCCLONES.get(i)
					.getClone_instances().size(); j++) {
				int SCCIDin = SampleHandler.f.SCCCLONES.get(i).getSCCID();
				int fnum = SampleHandler.f.SCCCLONES.get(i)
						.getClone_instances().get(j).getFile_number();
				int start = SampleHandler.f.SCCCLONES.get(i)
						.getClone_instances().get(j).getStart_line();
				int end = SampleHandler.f.SCCCLONES.get(i).getClone_instances()
						.get(j).getEnd_line();
				// System.out.println("SCCIDin: " + SCCIDin + " fnum: " + fnum);
				// System.out.println("sCCID: " + sCCID + " fid: " + fid);
				if (sCCID == SCCIDin && fnum == fid && sl <= start && el >= end) {
					// System.out.println("Match found. Details are: " + SCCIDin
					// + " fnum: " + fnum + " sl: " + sl + " el: " + el);
					return SampleHandler.f.SCCCLONES.get(i)
							.getClone_instances().get(j);
				}
			}
		}
		// System.out.println("NOT FOUND FOR " + sCCID + " fnum: " + fid +
		// " sl: " + sl + " el: " + el);
		return null;
	}

	public void DisplayMethodClones() {
		// TODO Auto-generated method stub
		// System.out.println(MCCCLONES.size());
		for (int i = 0; i < MCCCLONES.size(); i++) {
			if (MCCCLONES.get(i).getMCCID() == 1) {
				for (int j = 0; j < MCCCLONES.get(i).getMethod_Clones().size(); j++) {
					System.out.println();
					System.out.println(MCCCLONES.get(i).getMethod_Clones().get(j).getMethod().getFileName());
					System.out
							.println("*********************Method Code****************");
					System.out.println(MCCCLONES.get(i).getMethod_Clones()
							.get(j).getMethod().getName());
					System.out.println(MCCCLONES.get(i).getMethod_Clones()
							.get(j).getMethod().getCode());
					System.out
							.println("*********************END Code****************");
					System.out.println();
//					for (int k = 0; k < MCCCLONES.get(i).getMethod_Clones()
//							.get(j).getSCCs().size(); k++) {
//						System.out.println();
//						System.out
//								.println("*********************Clone Code****************");
//						System.out.println(MCCCLONES.get(i).getMethod_Clones()
//								.get(j).getSCCs().get(k).getCode());
//						System.out
//								.println("*********************END Clone Code****************");
//						System.out.println();
//					}
				}
			}
		}
		// for(int i=0;i<MCCCLONES.size();i++)
		// {
		// System.out.println("MCCID: " + MCCCLONES.get(i).getMCCID());
		// System.out.print("SCCIDs are: ");
		// for(int j=0;j<MCCCLONES.get(i).getSCCIDs().size();j++)
		// {
		// System.out.println("MID: " +
		// MCCCLONES.get(i).getMethod_Clones().get(j).getMethodCloneID());
		// System.out.print(MCCCLONES.get(i).getSCCIDs().get(j) + " ");
		// }
		// System.out.println();
		// }
	}

	private static void display(ArrayList<SCC> SCCinstances) {
		for (int i = 0; i < SCCinstances.size(); i++) {
			for (int j = 0; j < SCCinstances.get(i).getClone_instances().size(); j++) {
				if (SCCinstances.get(i).getClone_instances().get(j)
						.getSCCID() == 30) {
					System.out.println(SCCinstances.get(i).getClone_instances()
							.get(j).getFile_number()
							+ " "
							+ SCCinstances.get(i).getClone_instances().get(j)
									.getSCCID()
							+ " "
							+ SCCinstances.get(i).getClone_instances().get(j)
									.getCode()
							+ " "
							+ SCCinstances.get(i).getClone_instances().get(j)
									.getStart_line()
							+ " "
							+ SCCinstances.get(i).getClone_instances().get(j)
									.getEnd_line());
				}
			}
		}
	}

	public ArrayList<SCC> getClones() {
		return SCCCLONES;
	}

	public ArrayList<MethodInstance> getMethods() {
		return METHODS;
	}

	public void addSCC(SCC temp) {
		SCCCLONES.add(temp);
	}

	public void addMethod(MethodInstance m) {
		METHODS.add(m);
	}

	public void genSCCTemplate(int SCCID) {
		SCC_Template vcl = null;
		for (SCC SC : SCCCLONES) {
			if (SC.getSCCID() == SCCID) {
				System.out
						.println("About to generate VCL Templates for SCCID: "
								+ SCCID);
				vcl = new SCC_Template(SCCID, SC, new ArrayList<Integer>());
				return;
			}
		}
		System.out.println("Could not find specified SCCID!");
	}

	public void genMCCTemplate(int MCCID) {
		MCC_Template vcl = null;
		for (MethodClone MC : MCCCLONES) {
			if (MC.getMCCID() == MCCID) {
				System.out
						.println("About to generate VCL Templates for MCCID: "
								+ MCCID);
				vcl = new MCC_Template(MCCID, MC);
				return;
			}
		}
		System.out.println("Could not find specified MCCID!");
	}

	public void DisplayMethods() {
		System.out.println("size = " + METHODS.size());
		for (MethodInstance m : METHODS) {
			// System.out.println(m.getCode());
			m.displaySCCContained();
		}
		System.out.println();
	}
}
