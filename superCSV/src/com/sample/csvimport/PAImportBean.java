package com.sample.csvimport;

public class PAImportBean {

	String DEName;
	String DEInstance;
	String Operation;
	String RDN;
	String MOName;
	String MOInstance;
	String Parameter;
	String Value_RefType;
	String Annotation;
	String result;
	String comments;

	public String getDEName() {
		return DEName;
	}

	public void setDEName(String dEName) {
		DEName = dEName;
	}

	public String getDEInstance() {
		return DEInstance;
	}

	public void setDEInstance(String dEInstance) {
		DEInstance = dEInstance;
	}

	public String getOperation() {
		return Operation;
	}

	public void setOperation(String operation) {
		Operation = operation;
	}

	public String getRDN() {
		return RDN;
	}

	public void setRDN(String rDN) {
		RDN = rDN;
	}

	public String getMOName() {
		return MOName;
	}

	public void setMOName(String mOName) {
		MOName = mOName;
	}

	public String getMOInstance() {
		return MOInstance;
	}

	public void setMOInstance(String mOInstance) {
		MOInstance = mOInstance;
	}

	public String getParameter() {
		return Parameter;
	}

	public void setParameter(String parameter) {
		Parameter = parameter;
	}

	public String getValue_RefType() {
		return Value_RefType;
	}

	public void setValue_RefType(String value_RefType) {
		Value_RefType = value_RefType;
	}

	public String getAnnotation() {
		return Annotation;
	}

	public void setAnnotation(String annotation) {
		Annotation = annotation;
	}
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "[DEName=" + DEName + ", DEInstance=" + DEInstance
				+ ", Operation=" + Operation + ", RDN=" + RDN + ", MOName="
				+ MOName + ", MOInstance=" + MOInstance + ", Parameter="
				+ Parameter + ", Value_RefType=" + Value_RefType
				+ ", Annotation=" + Annotation + "]"+"\n";
	}

	
	
}
