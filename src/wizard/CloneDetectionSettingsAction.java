package wizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.action.Action;

public class CloneDetectionSettingsAction extends Action implements
		IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow workbenchWindow;
	private Our_Wizard cdsWizard;

	public CloneDetectionSettingsAction() {
		super();
	}

	public void dispose() {
		
	}

	public void init(IWorkbenchWindow window) {
		workbenchWindow = window;
	}

	public void run(IAction action) {
		cdsWizard = new Our_Wizard();
		ISelection selection = workbenchWindow.getSelectionService().getSelection();
        IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
        if (selection instanceof IStructuredSelection){
            selectionToPass = (IStructuredSelection) selection;
        }
		cdsWizard.init(workbenchWindow.getWorkbench(), selectionToPass);
		Shell shell= workbenchWindow.getWorkbench().getActiveWorkbenchWindow().getShell();
		WizardDialog dialog= new WizardDialog(shell, cdsWizard);
		try{
			dialog.create();
			int res = dialog.open();
			notifyResult(res == Window.OK);
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		
	}

}
