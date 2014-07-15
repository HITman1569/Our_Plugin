package our_plugin;

import java.io.IOException;
import java.util.ArrayList;

import our_plugin.handlers.SampleHandler;

public class MethodInstance {
	private String name;
	private int MethodID;
	private int file_number;
	private int start_line;
	private int end_line;
	private int token_count;
	private ArrayList<Integer> SCCs_contained;
	private String code;
	private String fileName;
	
	public MethodInstance(String n, int mid, int fid, int sl, int el, int tc) throws IOException
	{
		name =n;
		MethodID = mid;
		file_number = fid;
		start_line = sl;
		end_line = el;
		token_count = tc;
		SCCs_contained = new ArrayList<>();
		code = "";
		fileName=Parser.getFile(fid);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMethodID() {
		return MethodID;
	}
	public void setMethodID(int methodID) {
		MethodID = methodID;
	}
	public int getFile_number() {
		return file_number;
	}
	public void setFile_number(int file_number) {
		this.file_number = file_number;
	}
	public int getStart_line() {
		return start_line;
	}
	public void setStart_line(int start_line) {
		this.start_line = start_line;
	}
	public int getEnd_line() {
		return end_line;
	}
	public void setEnd_line(int end_line) {
		this.end_line = end_line;
	}
	public int getToken_count() {
		return token_count;
	}
	public void setToken_count(int token_count) {
		this.token_count = token_count;
	}
	public ArrayList<Integer> getSCCs() {
		return SCCs_contained;
	}
	public void setSCCs_contained(ArrayList<Integer> sCCs_contained) {
		SCCs_contained = sCCs_contained;
	}
	public void addSCCID(int x)
	{
		this.SCCs_contained.add(x);
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void displaySCCContained() {
		// TODO Auto-generated method stub
		for(int i=0;i<this.SCCs_contained.size();i++)
		{
			System.out.print(this.SCCs_contained.get(i) + " ");
		}
		System.out.println();
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
