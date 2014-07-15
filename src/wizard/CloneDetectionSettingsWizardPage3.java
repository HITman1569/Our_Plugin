package wizard;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.eclipse.jface.resource.ImageDescriptor;
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

public class CloneDetectionSettingsWizardPage3 extends WizardPage {
	private Text tokenSelected;
	private Button addTokenButton;
	private Button removeTokenButton;
	private List tokenList;
	private String lang;
	private String langTitle;
	//private final Controller con = CloneAnalyzerPlugin.getController();

	public CloneDetectionSettingsWizardPage3(int langIndex) {
		super("Equal Tokens Settings");
		setTitle("Settings for Equal Tokens");
		setDescription("Select multiple tokens from the token list below to make " +
				"them equivalent for Clone Detection (Example: \"3=18, 7=9=12\").");
		
		if(langIndex == 0){
			//lang = Controller.TOKEN_JAVA;
			langTitle = "Java ";
		}
		else if(langIndex == 1){
			//lang = Controller.TOKEN_CPP;
			langTitle = "C++ ";
		}
		else if(langIndex == 3){
			//lang = Controller.TOKEN_CSHARP;
			langTitle = "CSHARP ";
		}
		else if(langIndex == 4){
			//lang = Controller.TOKEN_RUBBY;
			langTitle = "RUBBY ";
		}
		else if(langIndex == 5){
			//lang = Controller.TOKEN_PHP;
			langTitle = "PHP ";
		}
		else if(langIndex == 6){
			//lang = Controller.TOKEN_TXT;
			langTitle = "TXT ";
		}
		else{
			lang = null;
			langTitle = "";
		}
	}

	public CloneDetectionSettingsWizardPage3(String pageName, String title,
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
		suppressedTokensLabel.setText("Equal Tokens:");
		
		tokenSelected = new Text(container, SWT.V_SCROLL | SWT.BORDER | SWT.MULTI);
		tokenSelected.setEditable(true);
		tokenSelected.setText("");
		try{
			/*String filePath = con.getFilePath(Controller.CLONES_INPUT_EQUALTOKEN);
			File file = new File(filePath);
			FileInputStream filein = new FileInputStream(file);
			BufferedReader stdin = new BufferedReader(new InputStreamReader(filein));
			String line = "";
			while((line = stdin.readLine()) != null){
				tokenSelected.append(line);
			}*/	
			final FormData formData_1 = new FormData();
			formData_1.bottom = new FormAttachment(0, 170);
			formData_1.top = new FormAttachment(0, 30);
			formData_1.left = new FormAttachment(0, 10);
			formData_1.right = new FormAttachment(100, -10);
			tokenSelected.setLayoutData(formData_1);
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
				if(tokenList.getSelectionCount() > 1){
					StringTokenizer st;
					int count;
					String[] tokenIndices = null;
					String[] selections = tokenList.getSelection();
					String tokens = tokenSelected.getText();
					String newTokens = "";
					
					if(tokens != null && tokens.length() > 0){
						st = new StringTokenizer(tokens, "\n\r");
						count = st.countTokens();
						tokenIndices = new String[count];
						for(int i = 0; i < count; i++){
							tokenIndices[i] = st.nextToken().trim();
						}
						count = selections.length;
						for(int i = 0; i < count; i++){
							st = new StringTokenizer(selections[i], ",");
							newTokens += (st.nextToken() + "=");
						}
						newTokens = newTokens.substring(0, newTokens.length() - 1);
						boolean contains = false;
						for(int i = 0; i < tokenIndices.length; i++){
							if(newTokens.equalsIgnoreCase(tokenIndices[i])){
								contains = true;
								removeTokenButton.setEnabled(true);
								addTokenButton.setEnabled(false);
							}
						}
						if(!contains){
							removeTokenButton.setEnabled(false);
							addTokenButton.setEnabled(true);
						}
					}
					else{
						removeTokenButton.setEnabled(false);
						addTokenButton.setEnabled(true);
					}
				}
				else{
					addTokenButton.setEnabled(false);
					removeTokenButton.setEnabled(false);
				}
			}
		});
		try{
			/*String filePath = con.getFilePath(lang);
			File file = new File(filePath);
			FileInputStream filein = new FileInputStream(file);
			BufferedReader stdin = new BufferedReader(new InputStreamReader(filein));
			String line = "";
			while((line = stdin.readLine()) != null){
				tokenList.add(line);
			}*/	
			final FormData formData_2 = new FormData();
			formData_2.bottom = new FormAttachment(0, 400);
			formData_2.top = new FormAttachment(0, 210);
			formData_2.left = new FormAttachment(0, 10);
			formData_2.right = new FormAttachment(100, -70);
			tokenList.setLayoutData(formData_2);
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
				String[] tokenIndices = null;
				String[] selections = tokenList.getSelection();
				String tokens = tokenSelected.getText();
				String newTokens = "";
				
				if(tokens != null && tokens.length() > 0){
					tokenSelected.setText("");
					st = new StringTokenizer(tokens, "\n\r");
					count = st.countTokens();
					tokenIndices = new String[count];
					for(int i = 0; i < count; i++){
						tokenIndices[i] = st.nextToken().trim();
					}
					if(selections != null && selections.length > 1){
						count = selections.length;
						for(int i = 0; i < count; i++){
							st = new StringTokenizer(selections[i], ",");
							newTokens += (st.nextToken() + "=");
						}
						newTokens = newTokens.substring(0, newTokens.length() - 1);
						boolean contains = false;
						for(int i = 0; i < tokenIndices.length; i++){
							tokenSelected.append(tokenIndices[i] + "\n");
							if(newTokens.equalsIgnoreCase(tokenIndices[i])){
								contains = true;
							}
						}
						if(!contains){
							tokenSelected.append(newTokens + "\n");
							removeTokenButton.setEnabled(true);
							addTokenButton.setEnabled(false);
						}
					}
					else{
						for(int i = 0; i < tokenIndices.length; i++){
							tokenSelected.append(tokenIndices[i] + "\n");
						}
					}
				}
				else{
					if(selections != null && selections.length > 1){
						count = selections.length;
						for(int i = 0; i < count; i++){
							st = new StringTokenizer(selections[i], ",");
							newTokens += (st.nextToken() + "=");
						}
						newTokens = newTokens.substring(0, newTokens.length() - 1);
						tokenSelected.append(newTokens);
						removeTokenButton.setEnabled(true);
						addTokenButton.setEnabled(false);
					}
				}
				tokens = tokenSelected.getText();
				tokenSelected.setText(tokens);
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
				String[] tokenIndices = null;
				String[] selections = tokenList.getSelection();
				String tokens = tokenSelected.getText();
				String newTokens = "";
				
				if(tokens != null && tokens.length() > 0){
					tokenSelected.setText("");
					st = new StringTokenizer(tokens, "\n\r");
					count = st.countTokens();
					tokenIndices = new String[count];
					for(int i = 0; i < count; i++){
						tokenIndices[i] = st.nextToken();
					}
					if(selections != null && selections.length > 1){
						count = selections.length;
						for(int i = 0; i < count; i++){
							st = new StringTokenizer(selections[i], ",");
							newTokens += (st.nextToken() + "=");
						}
						newTokens = newTokens.substring(0, newTokens.length() - 1);
						for(int j = 0; j < tokenIndices.length; j++){
							if(!newTokens.equalsIgnoreCase(tokenIndices[j])){
								tokenSelected.append(tokenIndices[j] + "\n");
							}
						}
						removeTokenButton.setEnabled(false);
						addTokenButton.setEnabled(true);
					}
					else{
						for(int j = 0; j < tokenIndices.length; j++){
							tokenSelected.append(tokenIndices[j] + "\n");
						}
					}
				}
				tokens = tokenSelected.getText();
				tokenSelected.setText(tokens);
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
	
	public void saveDataToWizard() {
		try{
			/*String filePath = con.getFilePath(Controller.CLONES_INPUT_EQUALTOKEN);
			File file = new File(filePath);
			file.createNewFile();
			FileOutputStream fileout = new FileOutputStream(file);
			PrintWriter stdout = new PrintWriter(fileout);
			if(tokenSelected != null && tokenSelected.getText() != null){
				stdout.print(tokenSelected.getText());
			}
			stdout.flush();
			fileout.close();
			stdout.close();*/
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public boolean canFlipToNextPage() {
		return false;
	}
}
