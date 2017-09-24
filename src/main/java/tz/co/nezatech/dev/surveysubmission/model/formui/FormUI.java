package tz.co.nezatech.dev.surveysubmission.model.formui;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tz.co.nezatech.dev.surveysubmission.model.BaseData;
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormUI extends BaseData {
	String name;
	String label;
	String version;
	List<Group> groups;
	Submit submit;

	public FormUI() {
		super();
		this.groups = new LinkedList<FormUI.Group>();
	}
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Submit {
		String buttonText;
		String successMessage;
		String failureMessage;

		public Submit() {
			super();
		}

		public String getButtonText() {
			return buttonText;
		}

		public void setButtonText(String buttonText) {
			this.buttonText = buttonText;
		}

		public String getSuccessMessage() {
			return successMessage;
		}

		public void setSuccessMessage(String successMessage) {
			this.successMessage = successMessage;
		}

		public String getFailureMessage() {
			return failureMessage;
		}

		public void setFailureMessage(String failureMessage) {
			this.failureMessage = failureMessage;
		}

	}
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Group {
		String name;
		String label;
		String type;
		boolean otherSpecify;
		String itemLabel;
		String quantityLabel;
		String noStockReasonText;
		int idStart;
		int idEnd;
		String productTag;
		String quantityTag;
		String recomPriceTag;
		String actualPriceTag;
		String shortInstruction;
		List<FormUI.Input> inputs;
		String labelYesNo;
		String formGroupReference;

		public Group() {
			super();
			this.inputs = new LinkedList<FormUI.Input>();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<Input> getInputs() {
			return inputs;
		}

		public void setInputs(List<Input> inputs) {
			this.inputs = inputs;
		}

	}
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Input {
		String name;
		String label;
		String type;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Submit getSubmit() {
		return submit;
	}

	public void setSubmit(Submit submit) {
		this.submit = submit;
	}

}
