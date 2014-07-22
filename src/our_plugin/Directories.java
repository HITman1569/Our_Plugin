package our_plugin;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import our_plugin.handlers.SampleHandler;

public class Directories {
	public static final String CLONES_INPUT_FILES = "\\CloneMiner\\input\\InputFiles.txt";
	public static final String CLONES_INPUT_SUPPRESSED = "\\CloneMiner\\input\\Suppressed.txt";
	public static final String CLONES_INPUT_EQUALTOKEN = "\\CloneMiner\\input\\EqualTokens.txt";
	public static final String CLONES_INPUT_CLUSTER_PTRS = "\\CloneMiner\\input\\ClusterParameters.txt";
	public static final String CLONES_OUTPUT = "\\CloneMiner\\output\\Clones.txt";
	public static final String CLONESFILE= "\\CloneMiner\\output\\Clones.txt";
	public static final String COMBINEDTOKENSFILE="\\CloneMiner\\output\\CombinedTokens.txt";
	public static final String DIRSINFOFILE="\\CloneMiner\\output\\DirsInfo.txt";
	public static final String INPUTFILE="\\CloneMiner\\input\\InputFiles.txt";
	public static final String CHECKFILE="\\check\\check.txt";
	public static final String FILESINFOFILE="\\CloneMiner\\output\\FilesInfo.txt";
	public static final String CLONE_MINER = "\\CloneMiner\\clones.exe";
	public static final String CLONE_MINER_DIR = "\\CloneMiner";
	public static final String VCL_PATH = "\\VCL_Output\\VCL_1.1.4.bat";
	public static final String VCL_PATH_DIR = "\\VCL_Output";
	public static final String METHODSINFOFILE="\\CloneMiner\\output\\MethodsInfo.txt";
	public static final String CLONESBYMETHODFILE = "\\CloneMiner\\output\\ClonesByMethod.txt";
	public static final String METHODCLUSTERSXX = "\\CloneMiner\\output\\MethodClustersXX.txt";
	public static final String TOKEN_JAVA = "\\Resources\\JavaTokens.txt";
	public static final String TOKEN_CPP = "\\Resources\\CppTokens.txt";
	public static final String TOKEN_CSHARP= "\\Resources\\CSharpTokenTypes.txt";
	public static final String TOKEN_RUBBY= "\\Resources\\RubyTokenTypes.txt";
	public static final String TOKEN_PHP= "\\Resources\\PHPTokenTypes.txt";
	public static final String TOKEN_TXT= "\\Resources\\TXTLexerTokenTypes.txt";
	private static final Bundle myBundle = Activator.getDefault().getBundle();

	
	
	public static String getFilePath(String fp){
		String pathStr = null;
		try{
			IPath path = new Path(FileLocator.toFileURL(myBundle.getEntry(fp)).getPath());
			pathStr = path.toString();
			//pathStr = Activator.getAbsolutePath(Directories.CLONE_MINER);		
			}catch(Exception e){
				e.printStackTrace();
				System.err.println(e.getMessage());
		}

		return pathStr;
	}
}
