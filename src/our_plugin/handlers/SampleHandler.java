package our_plugin.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import our_plugin.File_Reader;
import our_plugin.Initializer;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public static File_Reader f;
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new Initializer();
		f = new File_Reader();
		try {
			f.run();
			//for(int i = 1; i < 5; i++)
			//	f.genSCCTemplate(i);
			//for(int i = 3; i < 9; i++)
				//f.genMCCTemplate(f.MCCCLONES.get(4).getMCCID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
