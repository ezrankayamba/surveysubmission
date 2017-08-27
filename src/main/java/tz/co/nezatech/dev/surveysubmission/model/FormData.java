package tz.co.nezatech.dev.surveysubmission.model;

public class FormData extends BaseData {
	private int id;
	private String metadata, rawdata, datatype;
	private Form form;

	public FormData(String metadata, String rawdata, String datatype, Form form) {
		super();
		this.metadata = metadata;
		this.rawdata = rawdata;
		this.datatype = datatype;
		this.form = form;
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

	public String getRawdata() {
		return rawdata;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
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
