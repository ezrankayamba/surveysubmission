package tz.co.nezatech.dev.surveysubmission.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseData implements IData {
	@JsonIgnore
	private Schema schema;

	public BaseData() {
		super();
		setSchema();
	}

	public void setSchema() {
		try {
			this.schema = new Schema(this.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Schema getSchema() {
		return schema;
	}

}
