package our_plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

import our_plugin.handlers.SampleHandler;

public class Parser {

	public Parser() {

	}

	public static void parseCombinedTokens(String temp_path) {
		temp_path = Activator.getAbsolutePath(temp_path);
		FileReader frdr = null;
		BufferedReader reader = null;
		try {
			frdr = new FileReader(temp_path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		try {
			String line = null;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				if(line.charAt(0)!=' ')
				{
					line = " " + line;
				}
				String[] parts = line.split("\\s+");
				if(Integer.parseInt(parts[1])==9 || Integer.parseInt(parts[1])==99999)
				{
					int x=0;
				}
				if (isNumber(parts[2]) && isNumber(parts[3])
						&& isNumber(parts[4])) {
					SCCInstanceCustom instance = getSCCInstance(
							Integer.parseInt(parts[2]),
							Integer.parseInt(parts[3]),
							Integer.parseInt(parts[4]));
					if (instance != null) {
						if (parts[9].equals("STARTMETHOD")
								|| parts[9].equals("ENDMETHOD")
								|| parts[9].equals("ENDFILE")) {
							continue;
						}
						instance.addToken(parts[9]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * private static void show(ArrayList<ArrayList<String>> tokens) { for (int
	 * i = 0; i < tokens.size(); i++) { System.out.println(tokens.size());
	 * System.out.println(tokens.get(i).size()); }
	 * 
	 * }
	 */
	private static SCCInstanceCustom getSCCInstance(int filenumber,
			int linenumber, int colnumber) throws IOException {
		for (int i = 0; i < SampleHandler.f.getClones().size(); i++) {
			for (int j = 0; j < SampleHandler.f.getClones().get(i)
					.getClone_instances().size(); j++) {
				if (SampleHandler.f.getClones().get(i).getClone_instances()
						.get(j).getFile_number() == filenumber
						&& SampleHandler.f.getClones().get(i)
								.getClone_instances().get(j).getStart_line() <= linenumber
						&& SampleHandler.f.getClones().get(i)
								.getClone_instances().get(j).getEnd_line() >= linenumber) {
					if (SampleHandler.f.getClones().get(i).getClone_instances()
							.get(j).getStart_line() == linenumber) {
						if (SampleHandler.f.getClones().get(i)
								.getClone_instances().get(j).getStart_col() <= colnumber) {
							return SampleHandler.f.getClones().get(i)
									.getClone_instances().get(j);
						} else {
							return null;
						}
					} else if (SampleHandler.f.getClones().get(i)
							.getClone_instances().get(j).getEnd_line() == linenumber) {
						if (SampleHandler.f.getClones().get(i)
								.getClone_instances().get(j).getEnd_col() >= colnumber) {
							return SampleHandler.f.getClones().get(i)
									.getClone_instances().get(j);
						} else {
							return null;
						}
					} else {
						return SampleHandler.f.getClones().get(i)
								.getClone_instances().get(j);
					}
				}
			}
		}
		return null;
	}

	private static boolean isNumber(String string) {
		try {
			Double.parseDouble(string);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true && string.length() > 0;
	}

	public static void parseClones(String path) {
		path = Activator.getAbsolutePath(path);
		FileReader frdr = null;
		BufferedReader reader = null;
		refresh();
		int count = 0;
		try {
			frdr = new FileReader(path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
		}
		try {
			String line = null;
			SCC tempscc = null;
			SCCInstanceCustom tempclone = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("[; : . -]");
				switch (parts.length) {
				case 0:
					break;
				case 3:
					tempscc = new SCC();
					tempscc.setSCCID(Integer.parseInt(parts[0]));
					tempscc.setLength_in_tokens(Integer.parseInt(parts[1]));
					tempscc.setClone_count(Integer.parseInt(parts[2]));
					count = 0;
					break;
				case 5:
					count++;
					tempclone = new SCCInstanceCustom();
					tempclone.setSCCID(tempscc.getSCCID());
					tempclone.setFile_number(Integer.parseInt(parts[0]));
					tempclone.setStart_line(Integer.parseInt(parts[1]));
					tempclone.setStart_col(Integer.parseInt(parts[2]));
					tempclone.setEnd_line(Integer.parseInt(parts[3]));
					tempclone.setEnd_col(Integer.parseInt(parts[4]));
					tempclone.setCode(getCodeSegment(
							getFile(tempclone.getFile_number(), tempclone),
							tempclone.getStart_line(),
							tempclone.getStart_col(), tempclone.getEnd_line(),
							tempclone.getEnd_col(),tempclone));
					tempclone.setDir_id(getDir(tempclone.getFile_number()));
					tempclone
							.setFilename(getDirName(tempclone.getFile_number()));
//					Writer.writer(tempclone);
					tempscc.addInstance(tempclone);
					if (count == tempscc.getClone_count()) {
						SampleHandler.f.addSCC(tempscc);
						CloneDB.insertSCC(tempscc);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void refresh() {
		for (int i = 0; i < 115; i++) {
			String path = Activator.getAbsolutePath(".\\My Output\\");
			File f = new File(path + i + ".txt");
			f.delete();
		}

	}

	public static String getDirName(int file_number2) {
		int dirnum = getDir(file_number2);
		FileReader frdr = null;
		BufferedReader reader = null;
		try {
			String path = Directories.DIRSINFOFILE;
			path = Activator.getAbsolutePath(path);
			frdr = new FileReader(path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
		}
		try {
			String line = reader.readLine();
			while (line != null) {
				String[] parts = line.split(",");
				if (Integer.parseInt(parts[0]) == dirnum) {
					return parts[1];
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
		}
		try {
			reader.close();
		} catch (IOException e) {
		}
		return "Directory name not found";
	}

	public static int getDir(int file_number2) {
		FileReader frdr = null;
		BufferedReader reader = null;
		try {
			String path = Directories.FILESINFOFILE;
			path = Activator.getAbsolutePath(path);
			frdr = new FileReader(path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
		}
		try {
			String line = reader.readLine();
			while (line != null) {
				String[] parts = line.split(",");
				if (Integer.parseInt(parts[0]) == file_number2) {
					return Integer.parseInt(parts[1]);
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
		}
		try {
			reader.close();
		} catch (IOException e) {
		}
		return 0;
	}

	public static String getFile(int file_number2, SCCInstanceCustom temp) {
		String name = null;
		temp.setFile_number(0);
		FileReader frdr = null;
		BufferedReader reader = null;
		try {
			String path = Directories.INPUTFILE;
			path = Activator.getAbsolutePath(path);
			frdr = new FileReader(path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
		}
		try {
			String line = reader.readLine();
			while (line != null) {
				if (temp.getFile_number() == file_number2) {
					name = line;
					// System.out.println(name);
					reader.close();
					return name;
				}
				temp.setFile_number(temp.getFile_number() + 1);
				line = reader.readLine();
			}
		} catch (Exception e) {
		}
		try {
			reader.close();
		} catch (IOException e) {
		}
		System.out.println("File not found");
		return "File not found";
	}

	/*static String getCodeSegment(String path, int start_line,
			int start_col, int end_line, int end_col) throws IOException {
		File check = new File(path);
		FileReader frdr = null;
		BufferedReader buff = null;
		String data = "";
		if (check.exists()) {
			frdr = new FileReader(path);
			buff = new BufferedReader(frdr);
		} else if (!check.exists()) {
			String path2 = Directories.CHECKFILE;
			path2 = Activator.getAbsolutePath(path2);
			frdr = new FileReader(path2);
			buff = new BufferedReader(frdr);
			System.out.println("File not found: " + path2);
		}
		int line_number = 0;
		try {
			String line = null;
			while ((line = buff.readLine()) != null) {
				line_number++;
				if (line_number >= start_line && line_number <= end_line) {
					if (line_number == start_line) {
						line = line.substring(start_col - 1);
					} else if (line_number == end_line) {
						//line = line.substring(0, end_col);
						String temp = line.substring(0, end_col);
						String temp2 = line.substring(end_col);
						String [] patrs = temp2.split("[ ; , [ ] { } . ! = < > ( )]");
						temp += patrs[0];
						end_col=end_col+patrs[0].length();
						line=temp;
//						data +=line + "\n";
//						continue;
					}
					data += (line + "\n");
				}
			}
		} catch (Exception e) {
		} finally {
			frdr.close();
			buff.close();

		}
		return data;
	}*/
	
	static String getCodeSegment(String path, int start_line,
			int start_col, int end_line, int end_col,SCCInstanceCustom inst) throws IOException {
		File check = new File(path);
		FileReader frdr = null;
		BufferedReader buff = null;
		String data = "";
		if (check.exists()) {
			frdr = new FileReader(path);
			buff = new BufferedReader(frdr);
		} else if (!check.exists()) {
			String path2 = Directories.CHECKFILE;
			path2 = Activator.getAbsolutePath(path2);
			frdr = new FileReader(path2);
			buff = new BufferedReader(frdr);
			System.out.println("File not found: " + path2);
		}
		int line_number = 0;
		try {
			String line = null;
			while ((line = buff.readLine()) != null) {
				line_number++;
				if(start_line == end_line)
				{
					if(line_number == start_line)
					{
						line=line.substring(start_col - 1, end_col);
						data += (line + "\n");
						frdr.close();
						buff.close();
						return data;
					}
				}
				else if (line_number >= start_line && line_number <= end_line) {
					if (line_number == start_line) {
						line = line.substring(start_col - 1);
					} else if (line_number == end_line) {
						if(inst != null)
						{
							String temp = line.substring(0, end_col);
							//String temp2 = line.substring(end_col);
							//String [] patrs = temp2.split("[ ; , [ ] { } . ! = < > ( )]");
							//temp += patrs[0];
							//inst.setEnd_col(end_col+patrs[0].length());
							line=temp;
							
						}
						else
						{
							line=line.substring(0,end_col);
						}
						//line = line.substring(0, end_col);
//						data +=line + "\n";
//						continue;
					}
					data += (line + "\n");
				}
			}
		} catch (Exception e) {
		} finally {
			frdr.close();
			buff.close();

		}
		return data;
	}

	public static void parseMethodsInfo(String methodsinfofile)
			throws IOException {
		// TODO Auto-generated method stub
		String path = Activator.getAbsolutePath(methodsinfofile);
		FileReader frdr = null;
		BufferedReader reader = null;
		try {
			frdr = new FileReader(path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				MethodInstance m = new MethodInstance(parts[2],
						Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
						Integer.parseInt(parts[5]));
				SampleHandler.f.addMethod(m);
			}
		} catch (Exception e) {
			System.out.println("Error2");
		} finally {
			reader.close();
		}
	}

	public static void parseClonesByMethod(String clonesbymethodfile)
			throws IOException {
		// TODO Auto-generated method stub
		String temp_path = Activator.getAbsolutePath(clonesbymethodfile);
		FileReader frdr = null;
		BufferedReader reader = null;
		try {
			frdr = new FileReader(temp_path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		try {
			String line = null;
			int line_number = 0;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] parts = line.split(",");
					for (int i = 0; i < parts.length; i++) {
						SampleHandler.f.getMethods().get(line_number)
								.addSCCID(Integer.parseInt(parts[i]));
					}
				}
				line_number++;
			}
		} catch (Exception e) {
			System.out.println("Error1");
		} finally {
			reader.close();
		}
	}

	public static void updateMETHODS(ArrayList<MethodInstance> mETHODS)
			throws Exception {
		// TODO Auto-generated method stub
		for (int i = 0; i < mETHODS.size(); i++) {
			int fnum = mETHODS.get(i).getFile_number();
			String fname = getFile(fnum);
//			System.out.println("Name of the file is: " + fname);
			int start_line = mETHODS.get(i).getStart_line();
			int end_line = mETHODS.get(i).getEnd_line();
//			System.out.println("Start Line: " + start_line);
//			System.out.println("End Line: " + end_line);
			String code_set = getCodeSegment(fname, start_line, end_line);
//			System.out.println(code_set);
			mETHODS.get(i).setCode(code_set);
		}

	}

	private static String getCodeSegment(String path, int start_line,
			int end_line) throws Exception {
		// TODO Auto-generated method stub
		FileReader frdr = null;
		BufferedReader reader = null;
		String data="";
		frdr = new FileReader(path);
		reader = new BufferedReader(frdr);
		String line;
		int linenumber=0;
		while((line=reader.readLine())!=null)
		{
			if(linenumber>=start_line-1 && linenumber<=end_line)
			{
				data+=line + "\n";
			}
			linenumber++;
		}
		reader.close();
		return data;
	}

	public static String getFile(int fnum) throws IOException {
		FileReader frdr = null;
		BufferedReader reader = null;
		try {
//			System.out.println("File Number: " + fnum);
			String path = Directories.INPUTFILE;
			path = Activator.getAbsolutePath(path);
//			System.out.println("Path needed: " + path);
			frdr = new FileReader(path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
		}
		try {
			int f = 0;
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (f == fnum) {
					return line;
				}
//				System.out.println(f + " " + line);
				f++;
			}
		} catch (Exception e) {
			System.out.println("Error finding file Name");
		}
		reader.close();
		return "N/A";
	}

	public static void parseMethodClustersXX(String methodclustersxx) {
		// TODO Auto-generated method stub
		String temp_path = Activator.getAbsolutePath(methodclustersxx);
		FileReader frdr = null;
		BufferedReader reader = null;
		try {
			frdr = new FileReader(temp_path);
			reader = new BufferedReader(frdr);
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		try {
//			System.out.println("Parsing.....");
			String line = "";
			ArrayList<Integer> SCCIDs = null;
			int MCCID = -99;
			int instances = -99;
			int MID = -99;
			double coverage = -99.00;
			while((line = reader.readLine())!=null)
			{
//				System.out.println("Reading data...");
				String [] parts = line.split("[; ,]");
				switch(parts.length){
				case 2:
					MCCID = Integer.parseInt(parts[0]);
					instances = Integer.parseInt(parts[1]);
					line = reader.readLine();
					parts = line.split(",");
					SCCIDs = new ArrayList<>();
					for(int i=0;i<parts.length;i++)
					{
						SCCIDs.add(Integer.parseInt(parts[i]));
					}
					break;
				case 3:
					MID = Integer.parseInt(parts[0]);
					coverage = Double.parseDouble(parts[2]);
//					System.out.println("MCCID: " + MCCID);
//					System.out.println("MID: " + MID);
//					System.out.println("Instances: " + instances);
//					System.out.println("Coverage: " + coverage);
//					System.out.print("base SCCIDs: ");
//					for(int i=0;i<SCCIDs.size();i++)
//					{
//						System.out.print(SCCIDs.get(i) + " ");
//					}
//					System.out.println();
					MethodInstance add = null;
					for(int i=0;i<SampleHandler.f.METHODS.size();i++)
					{
						if(SampleHandler.f.METHODS.get(i).getMethodID()==MID)
						{
							add = SampleHandler.f.METHODS.get(i);
						}
					}
					MethodCloneInstance temp = new MethodCloneInstance(MID, coverage, add);
					MethodClone temp2 = new MethodClone(MCCID, instances, SCCIDs);
					temp2.addMethodCloneInstance(temp);
//					System.out.print("SCCCIDs: ");
//					for(int i=0;i<temp2.getSCCIDs().size();i++)
//					{
//						System.out.print(" " + temp2.getSCCIDs().get(i) + " ");
//					}
//					System.out.println();
					SampleHandler.f.MCCCLONES.add(temp2);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
