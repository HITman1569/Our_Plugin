package wizard;

import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import java.io.File;
import java.sql.Connection;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IWorkbenchPage;

import our_plugin.Activator;
import our_plugin.Initializer;


public class Our_Wizard extends Wizard implements INewWizard {
	private final static Initializer init = Activator.getInitializer();
	private Process miner;
	private MyExternalThread errStream, outputStream;
	private boolean isDelim = false; 
	private int stc = 30;
	private int langIndex = 0;
	private int groupIndex = 0;
	private Vector<String[]> fileList = null;
	private boolean savePage1 = false, savePage2 = false;
	CloneDetectionSettingsWizardPage1 page1;
	CloneDetectionSettingsWizardPage2 page21, page22,page24,page25,page26,page27;
	CloneDetectionSettingsWizardPage3 page31, page32,page34,page35,page36, page37;
	
	public Our_Wizard() {
		super();
	}
	
	public void setDelim(boolean isDelim) {
		this.isDelim = isDelim;
	}

	public void setStc(int stc) {
		this.stc = stc;
	}
	
	public void setLangIndex(int langIndex){
		this.langIndex = langIndex;
	}

	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	public void setFileList(Vector<String[]> fileList){
		this.fileList = fileList;
	}
	
	public void setSavePage1(boolean savePage1) {
		this.savePage1 = savePage1;
	}

	public void setSavePage2(boolean savePage2) {
		this.savePage2 = savePage2;
	}

	@Override
	public void addPages() {
		page1 = new CloneDetectionSettingsWizardPage1(this);
		page21 = new CloneDetectionSettingsWizardPage2(0, this);
		page22 = new CloneDetectionSettingsWizardPage2(1, this);
		page24 = new CloneDetectionSettingsWizardPage2(3, this);
		page25 = new CloneDetectionSettingsWizardPage2(4, this);
		page26 = new CloneDetectionSettingsWizardPage2(5, this);
		page27 = new CloneDetectionSettingsWizardPage2(6, this);
		
		page31 = new CloneDetectionSettingsWizardPage3(0);
		page32 = new CloneDetectionSettingsWizardPage3(1);
		page34 = new CloneDetectionSettingsWizardPage3(3);
		page35 = new CloneDetectionSettingsWizardPage3(4);
		page36 = new CloneDetectionSettingsWizardPage3(5);
		page37 = new CloneDetectionSettingsWizardPage3(6);
		
		addPage(page1);
		addPage(page21);
		addPage(page22);
		addPage(page24);
		addPage(page25);
		addPage(page26);
		addPage(page27);
		
		addPage(page31);
		addPage(page32);
		addPage(page34);
		addPage(page35);
		addPage(page36);
		addPage(page37);
	}
	
	public CloneDetectionSettingsWizardPage2 getPage21() {
		return page21;
	}

	public CloneDetectionSettingsWizardPage2 getPage22() {
		return page22;
	}
	
	public CloneDetectionSettingsWizardPage2 getPage24() {
		return page24;
	}

	public CloneDetectionSettingsWizardPage2 getPage25() {
		return page25;
	}
	public CloneDetectionSettingsWizardPage2 getPage26() {
		return page26;
	}

	public CloneDetectionSettingsWizardPage2 getPage27() {
		return page27;
	}
	
	public CloneDetectionSettingsWizardPage3 getPage31() {
		return page31;
	}

	public CloneDetectionSettingsWizardPage3 getPage32() {
		return page32;
	}
	
	public CloneDetectionSettingsWizardPage3 getPage34() {
		return page34;
	}

	public CloneDetectionSettingsWizardPage3 getPage35() {
		return page35;
	}
	public CloneDetectionSettingsWizardPage3 getPage36() {
		return page36;
	}

	public CloneDetectionSettingsWizardPage3 getPage37() {
		return page37;
	}

	@Override
	public boolean performFinish() {
		return false;
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.setWindowTitle("Clone Detection Settings");
		this.setNeedsProgressMonitor(true);
		this.setHelpAvailable(false);
		this.setForcePreviousAndNextButtons(true);
	}

	public void createPageControls(Composite pageContainer) {
	}

	public boolean performCancel() {
		if(miner != null){
			miner.destroy();
		}
		
		return super.performCancel();
	}

}
