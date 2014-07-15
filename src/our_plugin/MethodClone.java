package our_plugin;

import java.util.ArrayList;

import our_plugin.handlers.SampleHandler;

public class MethodClone {
	private int instances;
	private int MCCID;
	private ArrayList<Integer> SCCIDs;
	private ArrayList<MethodCloneInstance> Method_Clones;

	public MethodClone(int MCCIDint, int instance_count,
			ArrayList<Integer> sCCIDs_int) {
		// TODO Auto-generated constructor stub
		instances = instance_count;
		MCCID = MCCIDint;
		SCCIDs = sCCIDs_int;
		Method_Clones = new ArrayList<>();
	}

	public int getInstances() {
		return instances;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}

	public int getMCCID() {
		return MCCID;
	}

	public void setMCCID(int mCCID) {
		MCCID = mCCID;
	}

	public ArrayList<Integer> getSCCIDs() {
		return SCCIDs;
	}

	public void setSCCIDs(ArrayList<Integer> sCCIDs) {
		SCCIDs = sCCIDs;
	}

	public ArrayList<MethodCloneInstance> getMethod_Clones() {
		return Method_Clones;
	}

	public void setMethod_Clones(ArrayList<MethodCloneInstance> method_Clones) {
		Method_Clones = method_Clones;
	}

	public void addMethodCloneInstance(MethodCloneInstance e) {
		this.Method_Clones.add(e);
	}

	public void addSCCIDs(int x) {
		this.SCCIDs.add(x);
	}
}
