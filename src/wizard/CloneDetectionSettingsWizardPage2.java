package wizard;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import our_plugin.Activator;
import our_plugin.Directories;
import our_plugin.Initializer;

public class CloneDetectionSettingsWizardPage2 extends WizardPage {
	private Text tokenSelected;
	private Button addTokenButton;
	private Button removeTokenButton;
	private List tokenList;
	private int langIndex;
	private String lang;
	private String langTitle;
	private Our_Wizard wizard;
	private CloneDetectionSettingsWizardPage3 page3;
	private final Initializer init = Activator.getInitializer();

	public CloneDetectionSettingsWizardPage2(int langIndex, Our_Wizard wizard) {
		super("Suppressed Tokens Settings");
		setTitle("Settings for Suppressed Tokens");
		setDescription("Select the suppressed tokens from the token list below " +
				"for Clone Detection (Example: \"3, 18\").");
		this.langIndex = langIndex;
		if(langIndex == 0){
			lang = Directories.TOKEN_JAVA;
			langTitle = "Java ";
		}
		else if(langIndex == 1){
			lang = Directories.TOKEN_CPP;
			langTitle = "C++ ";
		}
		else if(langIndex == 3){
			lang = Directories.TOKEN_CSHARP;
			langTitle = "CSHARP ";
		}
		else if(langIndex == 4){
			lang = Directories.TOKEN_RUBBY;
			langTitle = "RUBY ";
		}
		else if(langIndex == 5){
			lang = Directories.TOKEN_PHP;
			langTitle = "PHP ";
		}
		else if(langIndex == 6){
			lang = Directories.TOKEN_TXT;
			langTitle = "TXT ";
		}
		else{
			lang = null;
			langTitle = "";
		}
		this.wizard = wizard;
	}

	public CloneDetectionSettingsWizardPage2(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FormLayout());
		
		final Label suppressedTokensLabel = new Label(container, SWT.NONE);
		final FormData formData_5 = new FormData();
		formData_5.right = new FormAttachment(100, -10);
		formData_5.top = new FormAttachment(0, 10);
		formData_5.bottom = new FormAttachment(0, 25);
		formData_5.left = new FormAttachment(0, 10);
		suppressedTokensLabel.setLayoutData(formData_5);
		suppressedTokensLabel.setText("Suppressed Tokens:");

		tokenSelected = new Text(container, SWT.V_SCROLL | SWT.BORDER | SWT.MULTI);
		tokenSelected.setEditable(true);
		tokenSelected.setText("");
		try{
			String filePath = Directories.getFilePath(Directories.CLONES_INPUT_SUPPRESSED);
			File file = new File(filePath);
			FileInputStream filein = new FileInputStream(file);
			BufferedReader stdin = new BufferedReader(new InputStreamReader(filein));
			String line = "";
			while((line = stdin.readLine()) != null){
				tokenSelected.append(line);
			}	
			final FormData formData_1 = new FormData();
			formData_1.bottom = new FormAttachment(0, 170);
			formData_1.top = new FormAttachment(0, 30);
			formData_1.left = new FormAttachment(0, 10);
			formData_1.right = new FormAttachment(100, -10);
			tokenSelected.setLayoutData(formData_1);
			stdin.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		final Label tokenListLabel = new Label(container, SWT.NONE);
		final FormData formData_6 = new FormData();
		formData_6.right = new FormAttachment(0, 300);
		formData_6.top = new FormAttachment(0, 190);
		formData_6.bottom = new FormAttachment(0, 105);
		formData_6.left = new FormAttachment(0, 10);
		tokenListLabel.setLayoutData(formData_6);
		tokenListLabel.setText(langTitle + "Token List: ");

		tokenList = new List(container, SWT.V_SCROLL | SWT.BORDER | SWT.MULTI);
		tokenList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(tokenList.getSelectionCount() > 0){
					addTokenButton.setEnabled(true);
					removeTokenButton.setEnabled(true);
				}
				else{
					addTokenButton.setEnabled(false);
					removeTokenButton.setEnabled(false);
				}
			}
		});
		try{
			String filePath = Directories.getFilePath(lang);
			File file = new File(filePath);
			FileInputStream filein = new FileInputStream(file);
			BufferedReader stdin = new BufferedReader(new InputStreamReader(filein));
			String line = "";
			while((line = stdin.readLine()) != null){
				tokenList.add(line);
			}
			final FormData formData_2 = new FormData();
			formData_2.bottom = new FormAttachment(0, 400);
			formData_2.top = new FormAttachment(0, 210);
			formData_2.left = new FormAttachment(0, 10);
			formData_2.right = new FormAttachment(100, -70);
			tokenList.setLayoutData(formData_2);
			stdin.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		addTokenButton = new Button(container, SWT.NONE);
		addTokenButton.setEnabled(false);
		addTokenButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				StringTokenizer st;
				int count;
				int[] tokenIndices = null, newTokenIndices = null;
				String[] selections = tokenList.getSelection();
				String tokens = tokenSelected.getText();
				if(tokens != null && tokens.length() > 0){
					tokenSelected.setText("");
					st = new StringTokenizer(tokens, ",");
					count = st.countTokens();
					tokenIndices = new int[count];
					for(int i = 0; i < count; i++){
						tokenIndices[i] = Integer.parseInt(st.nextToken().trim());
					}

					if(selections != null && selections.length != 0){
						count = selections.length;
						newTokenIndices = new int[count];
						for(int i = 0; i < count; i++){
							st = new StringTokenizer(selections[i], ",");
							newTokenIndices[i] = Integer.parseInt(st.nextToken().trim());
						}

						int m = 0;

						for(int i = 0; i < tokenIndices.length; ++i){
							if(m < newTokenIndices.length){
								boolean check = false;
								while(m < newTokenIndices.length){
									if(tokenIndices[i] < newTokenIndices[m]){
										tokenSelected.append(tokenIndices[i] + ",");
										check = true;
										break;
									}
									else if(tokenIndices[i] == newTokenIndices[m]){
										tokenSelected.append(tokenIndices[i]+ ",");
										++m;
										check = true;
										break;
									}
									else{
										tokenSelected.append(newTokenIndices[m] + ",");
										++m;
									}
								}
								if(check == false){
									tokenSelected.append(tokenIndices[i]+ ",");
								}
							}
							else{
								tokenSelected.append(tokenIndices[i]+ ",");
							}
						}
						while(m < newTokenIndices.length){
							tokenSelected.append(newTokenIndices[m] + ",");
							++m;
						}
					}
					else{
						for(int i = 0; i < tokenIndices.length; i++){
							tokenSelected.append(tokenIndices[i] + ",");
						}
					}
				}
				else{
					if(selections != null && selections.length != 0){
						count = selections.length;
						newTokenIndices = new int[count];
						for(int i = 0; i < count; i++){
							st = new StringTokenizer(selections[i], ",");
							newTokenIndices[i] = Integer.parseInt(st.nextToken().trim());
							tokenSelected.append(newTokenIndices[i] + ",");
						}
					}
				}
				tokens = tokenSelected.getText();
				tokenSelected.setText(tokens.substring(0, tokens.length() - 1));
				wizard.setSavePage2(false);
			}
		});
		final FormData formData_3 = new FormData();
		formData_3.top = new FormAttachment(tokenList, 0, SWT.TOP);
		formData_3.bottom = new FormAttachment(tokenList, 25, SWT.TOP);
		formData_3.right = new FormAttachment(tokenSelected, 0, SWT.RIGHT);
		formData_3.left = new FormAttachment(tokenList, 10, SWT.RIGHT);
		addTokenButton.setLayoutData(formData_3);
		addTokenButton.setText("Add");

		removeTokenButton = new Button(container, SWT.NONE);
		removeTokenButton.setEnabled(false);
		removeTokenButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				StringTokenizer st;
				int count;
				int[] tokenIndices = null;
				String[] selections = tokenList.getSelection();
				int[] newTokenIndices = null;
				String tokens = tokenSelected.getText();
				if(tokens != null && tokens.length() > 0){
					tokenSelected.setText("");
					st = new StringTokenizer(tokens, ",");
					count = st.countTokens();
					tokenIndices = new int[count];
					for(int i = 0; i < count; i++){
						tokenIndices[i] = Integer.parseInt(st.nextToken().trim());
					}

					if(selections != null && selections.length != 0){
						count = selections.length;
						newTokenIndices = new int[count];
						for(int i = 0; i < count; i++){
							st = new StringTokenizer(selections[i], ",");
							newTokenIndices[i] = Integer.parseInt(st.nextToken().trim());
						}

						boolean contains = false;
						for(int i = 0; i < tokenIndices.length; i++){
							contains = false;
							for(int j = 0; j < newTokenIndices.length; j++){
								if(newTokenIndices[j] == tokenIndices[i]){
									contains = true;
									break;
								}
							}
							if(!contains){
								tokenSelected.append(tokenIndices[i] + ",");
							}
						}
					}
				}
				tokens = tokenSelected.getText();
				tokenSelected.setText(tokens.substring(0, tokens.length() - 1));
				wizard.setSavePage2(false);
			}
		});

		final FormData formData_4 = new FormData();
		formData_4.bottom = new FormAttachment(tokenList, 0, SWT.BOTTOM);
		formData_4.top = new FormAttachment(tokenList, -25, SWT.BOTTOM);
		formData_4.right = new FormAttachment(tokenSelected, 0, SWT.RIGHT);
		formData_4.left = new FormAttachment(tokenList, 10, SWT.RIGHT);
		removeTokenButton.setLayoutData(formData_4);
		removeTokenButton.setText("Remove");
		
		setControl(container);
	}
	
	public IWizardPage getNextPage() {
		saveDataToWizard();
		if(langIndex == 0){
			page3 = wizard.getPage31();
		}
		else if(langIndex == 1){
			page3 = wizard.getPage32();
		}
		else if(langIndex == 3){
			page3 = wizard.getPage34();
		}
		else if(langIndex == 4){
			page3 = wizard.getPage35();
		}
		else if(langIndex == 5){
				page3 = wizard.getPage36();
		}
		else if(langIndex == 6){
			page3 = wizard.getPage37();
		}	
		else{
			page3 = null;
		}
		
		return page3;
	}
	
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}
	
	public void saveDataToWizard() {
		try{
			String filePath = Directories.getFilePath(Directories.CLONES_INPUT_SUPPRESSED);
			File file = new File(filePath);
			file.createNewFile();
			FileOutputStream fileout = new FileOutputStream(file);
			PrintWriter stdout = new PrintWriter(fileout);
			if(tokenSelected != null && tokenSelected.getText() != null){
				stdout.print(tokenSelected.getText());
			}
			stdout.flush();
			stdout.close();
			fileout.close();
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
		wizard.setSavePage2(true);
	}
}
