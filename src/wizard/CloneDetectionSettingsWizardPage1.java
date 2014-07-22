package wizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IStatus;

import our_plugin.Activator;
import our_plugin.Directories;
import our_plugin.Initializer;
import our_plugin.handlers.SampleHandler;

public class CloneDetectionSettingsWizardPage1 extends WizardPage implements Listener{
	public static final String CLONES_INPUT_FILES = "\\CloneMiner\\input\\InputFiles.txt";
	private List selectionList;
	private IStatus selectionListStatus;
	private Combo languageChoice;
	private Text fpc;
	private IStatus fpcStatus;
	private Text ftc;
	private IStatus ftcStatus;
	private Text mpc;
	private IStatus mpcStatus;
	private Text mtc;
	private IStatus mtcStatus;
	private Text scc;
	private IStatus sccStatus;
	private Button methodAnalysisButton ;
	private Button removeButton;
	private Button addDirButton;
	private Button addFileButton;
	private Button preGroupButton;
	private Button nextGroupButton;
	private Combo groupChoice;
	private int langIndex = 0;
	private int groupIndex = 0;
	private int currentGroup = 0;
	private Vector<String[]> fileGroups;
	private Our_Wizard wizard;
	private CloneDetectionSettingsWizardPage2 page2;
	private final Initializer init = Activator.getInitializer();


	public CloneDetectionSettingsWizardPage1(Our_Wizard wizard) {
		super("Minimum Token and Input Files Settings");
		setTitle("Settings for Minimum Tokens and Input Files");
		setDescription("Set the minimum tokens of different types of clone classes;\n" +
				"Set whether method boundaries should be detected during clone detection " +
				"(applicable to Java Language only);\nAdd input source files of " +
		"your selected language.");
		this.wizard = wizard;
		selectionListStatus = new Status(IStatus.ERROR, "not_used", 0, "Input File List Cannot Be Empty", null);
		sccStatus = new Status(IStatus.OK, "not_used", 0, "", null);
		ftcStatus = new Status(IStatus.OK, "not_used", 0, "", null);
		fpcStatus = new Status(IStatus.OK, "not_used", 0, "", null);
		mtcStatus = new Status(IStatus.OK, "not_used", 0, "", null);
		mpcStatus = new Status(IStatus.OK, "not_used", 0, "", null);
		fileGroups = new Vector<String[]>();
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());
		selectionList = new List(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER);

		final Label simpleCloneClassLabel = new Label(container, SWT.NONE);
		final FormData formData = new FormData();
		formData.right = new FormAttachment(0, 190);
		formData.top = new FormAttachment(0, 10);
		formData.bottom = new FormAttachment(0, 23);
		formData.left = new FormAttachment(0, 5);
		simpleCloneClassLabel.setLayoutData(formData);
		simpleCloneClassLabel.setText("Min similarities for Simple Clone Class");

		scc = new Text(container, SWT.BORDER);
		scc.setText("30");
		final FormData formData_1 = new FormData();
		formData_1.top = new FormAttachment(simpleCloneClassLabel, -19, SWT.BOTTOM);
		formData_1.bottom = new FormAttachment(simpleCloneClassLabel, 0, SWT.BOTTOM);
		formData_1.left = new FormAttachment(0, 215);
		formData_1.right = new FormAttachment(0, 270);
		scc.setLayoutData(formData_1);
		scc.addListener(SWT.Modify, this);
			
		methodAnalysisButton = new Button(container, SWT.CHECK);
		methodAnalysisButton.addListener(SWT.Selection, this);
		final FormData formData_10 = new FormData();
		formData_10.bottom = new FormAttachment(simpleCloneClassLabel, 0, SWT.BOTTOM);
		formData_10.left = new FormAttachment(0, 346);
		methodAnalysisButton.setLayoutData(formData_10);
		methodAnalysisButton.setText("Method Analysis");
		
		
		final Label simpleCloneClassLabel_1 = new Label(container, SWT.NONE);
		final FormData formData_2 = new FormData();
		formData_2.bottom = new FormAttachment(simpleCloneClassLabel, 13, SWT.TOP);
		formData_2.top = new FormAttachment(simpleCloneClassLabel, 0, SWT.TOP);
		simpleCloneClassLabel_1.setLayoutData(formData_2);
		simpleCloneClassLabel_1.setText("tokens");

		final Label simpleCloneClassLabel_2 = new Label(container, SWT.NONE);
		final FormData formData_3 = new FormData();
		formData_3.bottom = new FormAttachment(0, 58);
		formData_3.top = new FormAttachment(0, 45);
		formData_3.right = new FormAttachment(simpleCloneClassLabel, 185, SWT.LEFT);
		formData_3.left = new FormAttachment(simpleCloneClassLabel, 0, SWT.LEFT);
		simpleCloneClassLabel_2.setLayoutData(formData_3);
		simpleCloneClassLabel_2.setText("Min similarities for Method Clone Class");

		mtc = new Text(container, SWT.BORDER);
		mtc.setEnabled(false);
		mtc.setText("50");
		final FormData formData_4 = new FormData();
		formData_4.right = new FormAttachment(0, 270);
		formData_4.top = new FormAttachment(simpleCloneClassLabel_2, -19, SWT.BOTTOM);
		formData_4.bottom = new FormAttachment(simpleCloneClassLabel_2, 0, SWT.BOTTOM);
		formData_4.left = new FormAttachment(0, 215);
		mtc.setLayoutData(formData_4);
		mtc.addListener(SWT.Modify, this);

		Label label;
		label = new Label(container, SWT.NONE);
		formData_2.right = new FormAttachment(label, 35, SWT.LEFT);
		formData_2.left = new FormAttachment(label, 0, SWT.LEFT);
		final FormData formData_5 = new FormData();
		formData_5.top = new FormAttachment(mtc, -13, SWT.BOTTOM);
		formData_5.bottom = new FormAttachment(mtc, 0, SWT.BOTTOM);
		formData_5.right = new FormAttachment(0, 325);
		formData_5.left = new FormAttachment(0, 290);
		label.setLayoutData(formData_5);
		label.setText("tokens");

		mpc = new Text(container, SWT.BORDER);
		mpc.setEnabled(false);
		mpc.setText("50");
		final FormData formData_4_1 = new FormData();
		formData_4_1.top = new FormAttachment(label, -19, SWT.BOTTOM);
		formData_4_1.bottom = new FormAttachment(label, 0, SWT.BOTTOM);
		formData_4_1.left = new FormAttachment(0, 346);
		formData_4_1.right = new FormAttachment(0, 400);
		mpc.setLayoutData(formData_4_1);
		mpc.addListener(SWT.Modify, this);

		final Label percentLabel = new Label(container, SWT.NONE);
		final FormData formData_6 = new FormData();
		formData_6.right = new FormAttachment(0, 460);
		formData_6.left = new FormAttachment(0, 420);
		formData_6.top = new FormAttachment(label, 0, SWT.TOP);
		percentLabel.setLayoutData(formData_6);
		percentLabel.setText("percent");

		final Label simpleCloneClassLabel_2_1 = new Label(container, SWT.NONE);
		final FormData formData_3_1 = new FormData();
		formData_3_1.top = new FormAttachment(0, 80);
		formData_3_1.bottom = new FormAttachment(0, 93);
		formData_3_1.right = new FormAttachment(simpleCloneClassLabel_2, 185, SWT.LEFT);
		formData_3_1.left = new FormAttachment(simpleCloneClassLabel_2, 0, SWT.LEFT);
		simpleCloneClassLabel_2_1.setLayoutData(formData_3_1);
		simpleCloneClassLabel_2_1.setText("Min similarities for File Clone Class");

		ftc = new Text(container, SWT.BORDER);
		ftc.setText("50");
		final FormData formData_4_2 = new FormData();
		formData_4_2.top = new FormAttachment(simpleCloneClassLabel_2_1, -19, SWT.BOTTOM);
		formData_4_2.bottom = new FormAttachment(simpleCloneClassLabel_2_1, 0, SWT.BOTTOM);
		formData_4_2.left = new FormAttachment(mtc, -55, SWT.RIGHT);
		formData_4_2.right = new FormAttachment(mtc, 0, SWT.RIGHT);
		ftc.setLayoutData(formData_4_2);
		ftc.addListener(SWT.Modify, this);

		fpc = new Text(container, SWT.BORDER);
		fpc.setText("50");
		final FormData formData_4_3 = new FormData();
		formData_4_3.top = new FormAttachment(ftc, -19, SWT.BOTTOM);
		formData_4_3.bottom = new FormAttachment(ftc, 0, SWT.BOTTOM);
		formData_4_3.right = new FormAttachment(mpc, 55, SWT.LEFT);
		formData_4_3.left = new FormAttachment(mpc, 0, SWT.LEFT);
		fpc.setLayoutData(formData_4_3);
		fpc.addListener(SWT.Modify, this);

		final Label tokensLabel = new Label(container, SWT.NONE);
		final FormData formData_5_1 = new FormData();
		formData_5_1.top = new FormAttachment(fpc, -13, SWT.BOTTOM);
		formData_5_1.bottom = new FormAttachment(fpc, 0, SWT.BOTTOM);
		formData_5_1.left = new FormAttachment(label, -35, SWT.RIGHT);
		formData_5_1.right = new FormAttachment(label, 0, SWT.RIGHT);
		tokensLabel.setLayoutData(formData_5_1);
		tokensLabel.setText("tokens");

		final Label percentLabel_1 = new Label(container, SWT.NONE);
		final FormData formData_6_1 = new FormData();
		formData_6_1.bottom = new FormAttachment(0, 93);
		formData_6_1.top = new FormAttachment(0, 80);
		formData_6_1.right = new FormAttachment(percentLabel, 40, SWT.LEFT);
		formData_6_1.left = new FormAttachment(percentLabel, 0, SWT.LEFT);
		percentLabel_1.setLayoutData(formData_6_1);
		percentLabel_1.setText("percent");

		final FormData formData_9 = new FormData();
		formData_9.top = new FormAttachment(0, 150);
		formData_9.bottom = new FormAttachment(0, 351);
		formData_9.left = new FormAttachment(0, 5);
		formData_9.right = new FormAttachment(percentLabel_1, 0, SWT.RIGHT);
		selectionList.setLayoutData(formData_9);
		selectionList.addListener(SWT.Selection, this);
		
		addDirButton = new Button(container, SWT.NONE);
		final FormData formData_8 = new FormData();
		formData_8.top = new FormAttachment(0, 150);
		formData_8.bottom = new FormAttachment(0, 173);
		formData_8.right = new FormAttachment(0, 517);
		formData_8.left = new FormAttachment(0, 470);
		addDirButton.setLayoutData(formData_8);
		addDirButton.setText("Add Dir");
		addDirButton.addListener(SWT.Selection, this);

		addFileButton = new Button(container, SWT.NONE);
		addFileButton.addListener(SWT.Selection, this);
		final FormData formData_8_1 = new FormData();
		formData_8_1.top = new FormAttachment(0, 239);
		formData_8_1.bottom = new FormAttachment(0, 262);
		formData_8_1.right = new FormAttachment(addDirButton, 47, SWT.LEFT);
		formData_8_1.left = new FormAttachment(addDirButton, 0, SWT.LEFT);
		addFileButton.setLayoutData(formData_8_1);
		addFileButton.setText("Add File");

		removeButton = new Button(container, SWT.NONE);
		removeButton.setEnabled(false);
		removeButton.addListener(SWT.Selection, this);
		final FormData formData_8_1_1 = new FormData();
		formData_8_1_1.bottom = new FormAttachment(0, 351);
		formData_8_1_1.top = new FormAttachment(0, 328);
		formData_8_1_1.right = new FormAttachment(addFileButton, 47, SWT.LEFT);
		formData_8_1_1.left = new FormAttachment(addFileButton, 0, SWT.LEFT);
		removeButton.setLayoutData(formData_8_1_1);
		removeButton.setText("Remove");

		final Label simpleCloneClassLabel_2_1_1 = new Label(container, SWT.NONE);
		final FormData formData_3_1_1 = new FormData();
		formData_3_1_1.bottom = new FormAttachment(0, 128);
		formData_3_1_1.top = new FormAttachment(0, 115);
		formData_3_1_1.right = new FormAttachment(simpleCloneClassLabel_2_1, 100, SWT.LEFT);
		formData_3_1_1.left = new FormAttachment(simpleCloneClassLabel_2_1, 0, SWT.LEFT);
		simpleCloneClassLabel_2_1_1.setLayoutData(formData_3_1_1);
		simpleCloneClassLabel_2_1_1.setText("Language Choice");

		languageChoice = new Combo(container, SWT.DROP_DOWN);
		languageChoice.addListener(SWT.Selection, this);
		languageChoice.add("Java");
		languageChoice.add("C++");
		languageChoice.add("VB.NET");
		languageChoice.add("CSharp");
		languageChoice.add("Ruby");
		languageChoice.add("PHP");
		languageChoice.add("Plain Text");
		
		languageChoice.select(0);
		final FormData formData_7 = new FormData();
		formData_7.top = new FormAttachment(simpleCloneClassLabel_2_1_1, -19, SWT.BOTTOM);
		formData_7.bottom = new FormAttachment(simpleCloneClassLabel_2_1_1, 0, SWT.BOTTOM);
		formData_7.right = new FormAttachment(simpleCloneClassLabel_2_1_1, 100, SWT.RIGHT);
		formData_7.left = new FormAttachment(simpleCloneClassLabel_2_1_1, 20, SWT.RIGHT);
		languageChoice.setLayoutData(formData_7);
		
		final Label groupLabel = new Label(container, SWT.NONE);
		final FormData formData_13 = new FormData();
		formData_13.top = new FormAttachment(simpleCloneClassLabel_2_1_1, -13, SWT.BOTTOM);
		formData_13.bottom = new FormAttachment(simpleCloneClassLabel_2_1_1, 0, SWT.BOTTOM);
		formData_13.left = new FormAttachment(label, 0, SWT.LEFT);
		formData_13.right = new FormAttachment(label, 100, SWT.LEFT);
		groupLabel.setLayoutData(formData_13);
		groupLabel.setText("Grouping Choice");
		
		groupChoice = new Combo(container, SWT.DROP_DOWN);
		groupChoice.addListener(SWT.Selection, this);
		groupChoice.add("Mixed Mode");
		groupChoice.add("Across Groups");
		groupChoice.select(0);
		final FormData formData_14 = new FormData();
		formData_14.top = new FormAttachment(groupLabel, -19, SWT.BOTTOM);
		formData_14.bottom = new FormAttachment(groupLabel, 0, SWT.BOTTOM);
		formData_14.left = new FormAttachment(groupLabel, 20, SWT.RIGHT);
		formData_14.right = new FormAttachment(groupLabel, 120, SWT.RIGHT);
		groupChoice.setLayoutData(formData_14);
		
		nextGroupButton = new Button(container, SWT.PUSH);
		nextGroupButton.setEnabled(false);
		nextGroupButton.addListener(SWT.Selection, this);
		final FormData formData_12 = new FormData();
		formData_12.top = new FormAttachment(selectionList, 12, SWT.BOTTOM);
		formData_12.bottom = new FormAttachment(selectionList, 35, SWT.BOTTOM);
		formData_12.left = new FormAttachment(selectionList, -90, SWT.RIGHT);
		formData_12.right = new FormAttachment(selectionList, 0, SWT.RIGHT);
		nextGroupButton.setLayoutData(formData_12);
		nextGroupButton.setText("Next Group");
		
		preGroupButton = new Button(container, SWT.PUSH);
		preGroupButton.setEnabled(false);
		preGroupButton.addListener(SWT.Selection, this);
		final FormData formData_11 = new FormData();
		formData_11.top = new FormAttachment(nextGroupButton, 0, SWT.TOP);
		formData_11.bottom = new FormAttachment(nextGroupButton, 0, SWT.BOTTOM);
		formData_11.right = new FormAttachment(nextGroupButton, -5, SWT.LEFT);
		formData_11.left = new FormAttachment(nextGroupButton, -95, SWT.LEFT);
		preGroupButton.setLayoutData(formData_11);
		preGroupButton.setText("Previous Group");

		setControl(container);
	}

	public void handleEvent(Event e){
		Status status = new Status(IStatus.OK, "not_used", 0, "", null);

		if(e.widget == scc){
			try{
				if(!scc.getText().equalsIgnoreCase("")){
					int sccInt = Integer.parseInt(scc.getText().trim());
					if(sccInt <= 0){
						status = new Status(IStatus.ERROR, "not_used", 0, 
								"Minimum Token Count Value Cannot Be Smaller Than or Smaller Than or Equal To 0", null);
					}
				}
				else{
					status = new Status(IStatus.ERROR, "not_used", 0, 
							"Minimum Token Count Value Cannot Be Empty", null);
				}
			}catch(Exception exception){
				status = new Status(IStatus.ERROR, "not_used", 0, 
						"Minimum Token Count Value Must Be A Integer Value", null);
			}

			sccStatus = status;
		}
		else if(e.widget == ftc){
			try{
				if(!ftc.getText().equalsIgnoreCase("")){
					int ftcInt = Integer.parseInt(ftc.getText().trim());
					if(ftcInt <= 0){
						status = new Status(IStatus.ERROR, "not_used", 0, 
								"Minimum Token Count Value Cannot Be Smaller Than or Smaller Than or Equal To 0", null);
					}
				}
				else{
					status = new Status(IStatus.ERROR, "not_used", 0, 
							"Minimum Token Count Value Cannot Be Empty", null);
				}
			}catch(Exception exception){
				status = new Status(IStatus.ERROR, "not_used", 0, 
						"Minimum Token Count Value Must Be An Integer Value", null);
			}

			ftcStatus = status;
		}
		else if(e.widget == fpc){
			try{
				if(!fpc.getText().equalsIgnoreCase("")){
					int fpcInt = Integer.parseInt(fpc.getText().trim());
					if(fpcInt > 100 || fpcInt <= 0){
						status = new Status(IStatus.ERROR, "not_used", 0, 
								"Minimum Token Percentage Value Cannot Be Larger Than 100 or Smaller Than or Equal To 0", null);
					}
				}
				else{
					status = new Status(IStatus.ERROR, "not_used", 0, 
							"Minimum Token Percentage Value Cannot Be Empty", null);
				}
			}catch(Exception exception){
				status = new Status(IStatus.ERROR, "not_used", 0, 
						"Minimum Token Percentage Must Be An Integer Value", null);
			}

			fpcStatus = status;
		}
		else if(e.widget == mtc){
			try{
				boolean isMethodAnalysis = methodAnalysisButton.getSelection(); 
				if(isMethodAnalysis){
					if(!mtc.getText().equalsIgnoreCase("")){
						int mtcInt = Integer.parseInt(mtc.getText().trim());
						if(mtcInt <= 0){
							status = new Status(IStatus.ERROR, "not_used", 0, 
									"Minimum Token Count Value Cannot Be Smaller Than or Equal To 0", null);
						}
					}
					else{
						status = new Status(IStatus.ERROR, "not_used", 0, 
								"Minimum Token Count Value Cannot Be Empty", null);
					}
				}
			}catch(Exception exception){
				status = new Status(IStatus.ERROR, "not_used", 0, 
						"Minimum Token Count Value Must Be An Integer Value", null);
			}

			mtcStatus = status;
		}
		else if(e.widget == mpc){
			try{
				boolean isMethodAnalysis = methodAnalysisButton.getSelection(); 
				if(isMethodAnalysis){
					if(!mpc.getText().equalsIgnoreCase("")){
						int mpcInt = Integer.parseInt(mpc.getText().trim());
						if(mpcInt > 100 || mpcInt <= 0){
							status = new Status(IStatus.ERROR, "not_used", 0, 
									"Minimum Token Percentage Value Cannot Be Larger Than 100 or Smaller Than or Equal To 0", null);
						}
					}
					else{
						status = new Status(IStatus.ERROR, "not_used", 0, 
								"Minimum Token Percentage Value Cannot Be Empty", null);
					}
				}
			}catch(Exception exception){
				status = new Status(IStatus.ERROR, "not_used", 0, 
						"Minimum Token Percentage Must Be An Integer Value", null);
			}

			mpcStatus = status;
		}
		
		else if(e.widget == methodAnalysisButton){
			if (methodAnalysisButton.getSelection()){
				mtc.setEnabled(true);
				mpc.setEnabled(true);
			}
			else{
				mtc.setEnabled(false);
				mpc.setEnabled(false);
			}
		}
		else if(e.widget == selectionList){
			if(selectionList.getSelectionCount() > 0){
				removeButton.setEnabled(true);
			}
			else{
				removeButton.setEnabled(false);
			}
			
			if(selectionList.getItemCount() > 0){
				nextGroupButton.setEnabled(true);
			}
			else{
				nextGroupButton.setEnabled(false);
			}
		}
		else if(e.widget == nextGroupButton){
			if(selectionList.getItemCount() > 0){
				String[] fileList = selectionList.getItems();
				if(currentGroup < fileGroups.size()){
					fileGroups.set(currentGroup, fileList);
				}
				else{
					fileGroups.add(currentGroup, fileList);
				}
				preGroupButton.setEnabled(true);
				currentGroup++;
				if(currentGroup < fileGroups.size()){
					String[] nextFileList = fileGroups.get(currentGroup);
					selectionList.setItems(nextFileList);
				}
				else{
					selectionList.removeAll();
				}
				
				if(selectionList.getItemCount() > 0){
					nextGroupButton.setEnabled(true);
				}
				else{
					nextGroupButton.setEnabled(false);
				}
			}
		}
		else if(e.widget == preGroupButton){
			if(selectionList.getItemCount() > 0){
				String[] fileList = selectionList.getItems();
				if(currentGroup < fileGroups.size()){
					fileGroups.set(currentGroup, fileList);
				}
				else{
					fileGroups.add(currentGroup, fileList);
				}
			}
			nextGroupButton.setEnabled(true);
			currentGroup--;
			String[] preFileList = fileGroups.get(currentGroup);
			selectionList.setItems(preFileList);
			
			if(currentGroup > 0){
				preGroupButton.setEnabled(true);
			}
			else{
				preGroupButton.setEnabled(false);
			}
		}
		else if(e.widget == languageChoice){
			if(languageChoice.getSelectionIndex() != langIndex){
				selectionList.removeAll();
			}
			langIndex = languageChoice.getSelectionIndex();
			
			/*if(languageChoice.getSelectionIndex() != 0){
				methodAnalysisButton.setEnabled(false);
			}
			else{
			 */
			methodAnalysisButton.setEnabled(true);
			
			//}
		}
		else if(e.widget == groupChoice){
			groupIndex = groupChoice.getSelectionIndex();
		}
		else if(e.widget == removeButton){
			if(selectionList.getSelectionCount() > 0){
				selectionList.remove(selectionList.getSelectionIndices());
			}
			
			if(selectionList.getItemCount() > 0){
				nextGroupButton.setEnabled(true);
			}
			else{
				nextGroupButton.setEnabled(false);
			}
		}
		else if(e.widget == addFileButton){
			FileDialog fileDialog = new FileDialog(getShell(), SWT.MULTI);
			fileDialog.setText("Open");
			fileDialog.setFilterPath("");
			String[] javaLang = {"*.java"};
			String[] cppLang = {"*.c","*.cpp","*.cxx","*.h","*.hh","*.hpp","*.hxx"};
			String[] vbnetLang = {"*.vb"};
			String[] csharpLang = {"*.cs"};
			String[] rubbyLang = {"*.rb"};
			String[] phpLang = {"*.php"};
			String[] plainTextLang = {"*.txt"};
			String[] lang = {"empty"};

			switch(languageChoice.getSelectionIndex()){
			case 0:
				lang = javaLang;
				break;
			case 1:
				lang = cppLang;
				break;
			case 2:
				lang = vbnetLang;
				break;
			case 3:
				lang = csharpLang;
				break;
			case 4:
				lang = rubbyLang;
				break;
			case 5:
				lang = phpLang;
				break;
			case 6:
				lang=plainTextLang;
				break;
			}

			fileDialog.setFilterExtensions(lang);
			selectionList.add(fileDialog.open());
			if(selectionList.getItemCount() > 0){
				nextGroupButton.setEnabled(true);
			}
			else{
				nextGroupButton.setEnabled(false);
			}
		}
		else if(e.widget == addDirButton){
			DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
			String[] javaLang = {"java"};
			String[] cppLang = {"c","cpp","cxx","h","hh","hpp","hxx"};
			String[] vbnetLang = {"vb"};
			String[] csharpLang = {"cs"};
			String[] rubbyLang = {"rb"};
			String[] phpLang = {"php"};
			String[] plainTextLang={"txt"};
			String[] lang = {"empty"};
			switch(languageChoice.getSelectionIndex()){
			case 0:
				lang = javaLang;
				break;
			case 1:
				lang = cppLang;
				break;
			case 2:
				lang = vbnetLang;
				break;
			case 3:
				lang = csharpLang;
				break;
			case 4:
				lang = rubbyLang;
				break;
			case 5:
				lang = phpLang;
				break;
			case 6:
				lang=plainTextLang;
				break;
			}
			directoryDialog.setMessage("Please select a directory and click OK");
			directoryDialog.setFilterPath("");
			try{
				File dir = new File(directoryDialog.open());
				setSelectionList(dir, lang);
			}catch(Exception exp){
			}
			if(selectionList.getItemCount() > 0){
				nextGroupButton.setEnabled(true);
			}
			else{
				nextGroupButton.setEnabled(false);
			}
		}
		else{}

		if(selectionList.getItemCount() <= 0){
			status = new Status(IStatus.ERROR, "not_used", 0, 
					"Input File List Cannot Be Empty", null);
		}
		selectionListStatus = status;
		applyErrorMessage();
		wizard.setSavePage1(false);
		getWizard().getContainer().updateButtons();
	}

	public void setSelectionList(File dir, String[] lang){
		File[] files = dir.listFiles();
		for (int i=0; i<files.length; i++){
			File file = files[i];
			String filename = file.getName();
			if (file.isFile()){
				String ext = getExtension(filename);
				boolean ok = false;
				for (int j=0; j<lang.length; j++){
					if (ext.equals(lang[j])){
						ok = true;
						break;
					}
				}
				if(ok) selectionList.add(file.getAbsolutePath());
			}
			else{
				setSelectionList(file, lang);
			}
		}
	}

	/**
	 * helper function for getting the file extension part to decide file type
	 */
	public String getExtension(String filename) {
		String ext;
		int dotPlace = filename.lastIndexOf('.');
		ext = filename.substring(dotPlace + 1);
		return ext;
	}

	public void applyErrorMessage() {
		boolean methodStatusOK = true;
		//
		if( methodAnalysisButton.getSelection()&& (!mtcStatus.isOK() || !mpcStatus.isOK())){
			methodStatusOK = false;
		}

		if(sccStatus.isOK() && ftcStatus.isOK() && fpcStatus.isOK() 
				&& methodStatusOK && selectionListStatus.isOK()){
			setErrorMessage(null);
		}
		else{
			String msg = "";
			if(!selectionListStatus.isOK()){
				msg += selectionListStatus.getMessage() + "\n";
			}
			if(!sccStatus.isOK()){
				msg += sccStatus.getMessage() + "\n";
			}
			if(!ftcStatus.isOK()){
				msg += ftcStatus.getMessage() + "\n";
			}
			if(!fpcStatus.isOK()){
				msg += fpcStatus.getMessage() + "\n";
			}
			if(methodAnalysisButton.getSelection() &&!mtcStatus.isOK()){ 
				msg += mtcStatus.getMessage() + "\n";
			}
			if(methodAnalysisButton.getSelection() && !mpcStatus.isOK()){// 
				msg += mpcStatus.getMessage() + "\n";
			}
			setErrorMessage(msg);
		}
	}

	public boolean isPageComplete() {
		if (getErrorMessage() != null){
			return false;
		}
		
		boolean methodStatusOK = true;
		if(methodAnalysisButton.getSelection() && (!mtcStatus.isOK() || !mpcStatus.isOK())){// 
			methodStatusOK = false;
		}

		if(sccStatus.isOK() && ftcStatus.isOK() && fpcStatus.isOK() 
				&& methodStatusOK && selectionListStatus.isOK()){
			return true;
		}

		return false;
	}
	
	public IWizardPage getNextPage() {
		saveDataToWizard();
		if(languageChoice.getSelectionIndex() == 0){
			page2 = wizard.getPage21();
		}
		else if(languageChoice.getSelectionIndex() == 1){
			page2 = wizard.getPage22();
		}
		else if(languageChoice.getSelectionIndex() == 3){
			page2 = wizard.getPage24();
		}
		else if(languageChoice.getSelectionIndex() == 4){
			page2 = wizard.getPage25();
		}
		else if(languageChoice.getSelectionIndex() == 5){
			page2 = wizard.getPage26();
		}
		else{
			page2 = null;
		}
		
		return page2;
	}
	
	public boolean canFlipToNextPage() {
		return isPageComplete() && languageChoice.getSelectionIndex() != 2;
	}

	public void saveDataToWizard() {
		int stcInt = Integer.parseInt(scc.getText().trim());
		wizard.setStc(stcInt);
		langIndex = languageChoice.getSelectionIndex();
		wizard.setLangIndex(langIndex);
		groupIndex = groupChoice.getSelectionIndex();
		if(groupIndex == 1){
			groupIndex++;
		}
		wizard.setGroupIndex(groupIndex);
		int ftcInt = Integer.parseInt(ftc.getText().trim());
		int fpcInt = Integer.parseInt(fpc.getText().trim());
		boolean delim= true;
		wizard.setDelim(delim);
		int mtcInt = 0;
		int mpcInt = 0;
		if(methodAnalysisButton.getSelection()){
		
		    mtcInt = Integer.parseInt(mtc.getText().trim());
			mpcInt = Integer.parseInt(mpc.getText().trim());
		}
		
		if(selectionList.getItemCount() > 0){
			String[] fileList = selectionList.getItems();
			if(currentGroup < fileGroups.size()){
				fileGroups.set(currentGroup, fileList);
			}
			else{
				fileGroups.add(currentGroup, fileList);
			}
		}
		
		wizard.setFileList(fileGroups);
		
		try{
			String pathStr = Directories.getFilePath(Directories.CLONES_INPUT_FILES);
			File file = new File(pathStr);
			file.createNewFile();
			FileOutputStream fileout = new FileOutputStream(file);
			PrintWriter stdout = new PrintWriter(fileout);
			
			for(int i = 0; i < fileGroups.size(); i++){
				for(int j = 0; j < fileGroups.get(i).length; j++){
					if(j == fileGroups.get(i).length - 1 
							&& i != fileGroups.size() - 1){
						stdout.println(fileGroups.get(i)[j] + ";");
					}
					else{
						stdout.println(fileGroups.get(i)[j]);
					}
				}
			}
			
			stdout.flush();
			stdout.close();
			fileout.close();

			pathStr = Directories.getFilePath(Directories.CLONES_INPUT_CLUSTER_PTRS);
			file = new File(pathStr);
			file.createNewFile();
			fileout = new FileOutputStream(file);
			stdout = new PrintWriter(fileout);
			stdout.print(fpcInt + "," + ftcInt + "," + mpcInt + "," + mtcInt);
			stdout.flush();
			stdout.close();
			fileout.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		//con.setMinerSettings(groupIndex, fileGroups.size(), delim, stcInt, fpcInt, ftcInt, mpcInt, mtcInt);	
		init.setMinerSettings(groupIndex, fileGroups.size(), delim, stcInt, fpcInt, ftcInt, mpcInt, mtcInt);
		wizard.setSavePage1(true);
	}
}
