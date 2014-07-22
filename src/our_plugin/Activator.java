package our_plugin;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "Our_Plugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private static Initializer init;

	/**
	 * The constructor
	 */
	public Activator() {
		super();
		if (plugin != null)
			throw new IllegalStateException("Plug-in class already exists");
		plugin = this;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		init = new Initializer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		init.stop();
		init = null;
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static String getAbsolutePath(final String filePath) {
		String result = null;
		URL confUrl = getDefault().getBundle().getEntry(filePath);
		try {
			result = FileLocator.toFileURL(confUrl).getFile();
		} catch (IOException e) {
			System.out.println("Problem " + filePath);
		}
		return result;
	}
	
	public static Initializer getInitializer()
	{
		return init;
	}
	
}
