package tz.co.nezatech.dev.surveysubmission.model;

public class FormData extends BaseData {
	private int id;
	private String metadata, rawvalue, datatype;
	private Form form;

	public FormData(int id, String metadata, String rawdata, String datatype, Form form) {
		super();
		this.metadata = metadata;
		this.rawvalue = rawdata;
		this.datatype = datatype;
		this.form = form;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getRawvalue() {
		return rawvalue;
	}

	public void setRawvalue(String rawdata) {
		this.rawvalue = rawdata;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

}
