package tz.co.nezatech.dev.surveysubmission.model;

public class FormData extends BaseData {
	private int id;
	private String metadata, rawvalue;
	private String category, type, name;
	private Form form;

	public FormData(int id, String category, String type, String name, String metadata, String rawdata,
			Form form) {
		super();
		this.metadata = metadata;
		this.rawvalue = rawdata;
		this.form = form;
		this.id = id;
		this.category = category;
		this.type = type;
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

}
