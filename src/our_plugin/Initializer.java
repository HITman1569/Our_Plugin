package our_plugin;

import java.io.File;

public class Initializer {
	private static Process miner;
	private static Process vcl;
	private static int mintokensize = 50;
	private static int methodunique=1;
	private static int groupindex=0;
	private static ExternalThread errStream;
	private static ExternalThread outputStream;
	
	public Initializer()
	{
		ExecuteCloneMiner();
		CloneDB.Initiate();
	}
	
	public static void ExecuteCloneMiner()
	{
		try
		{
			String cloneminerexecpath=Activator.getAbsolutePath(Directories.CLONE_MINER);
			String cloneminerpath=Activator.getAbsolutePath(Directories.CLONE_MINER_DIR);
			final String[] strArray = new String[4];
			strArray[0] = cloneminerexecpath;
			strArray[1] = "" + mintokensize;
			strArray[2] = "" + methodunique;
			strArray[3] = "" + groupindex;
			File dir = new File(cloneminerpath);
			miner = Runtime.getRuntime().exec(strArray, null, dir);
			errStream = new ExternalThread(miner.getErrorStream());
			outputStream = new ExternalThread(miner.getInputStream());				
			errStream.start();
			outputStream.start();
			int result = miner.waitFor();
			if(result != 0){
				System.err.println("Clone Miner terminates with problems...");
			}
			else{
				errStream.join();
				outputStream.join();
			}
		}catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			miner.destroy();
		}
	}
	
	public static void ExecuteVCL(String filename) // RUNS VCL PROCESSOR FOR A SPECIFIC SPC FILE
	{
		//String path = Activator.getAbsolutePath(Directories.VCL_PATH_DIR+filename);
		try {
			String vclexecpath=Activator.getAbsolutePath(Directories.VCL_PATH);
			String vclpath=Activator.getAbsolutePath(Directories.VCL_PATH_DIR);
			final String[] strArray = new String[2];
			strArray[0] = vclexecpath;
			strArray[1] = "" + filename;
			File dir = new File(vclpath);
			vcl = Runtime.getRuntime().exec(strArray, null, dir);
			errStream = new ExternalThread(vcl.getErrorStream());
			outputStream = new ExternalThread(vcl.getInputStream());				
			errStream.start();
			outputStream.start();
			int result = vcl.waitFor();
			if(result != 0){
				System.err.println("VCL PROCESSOR terminates with problems...");
			}
			else{
				errStream.join();
				outputStream.join();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
			vcl.destroy();
		}
	}
}
