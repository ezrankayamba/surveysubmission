package tz.co.nezatech.dev.surveysubmission.model;

public class Form extends BaseData {
	private int id;
	private String name;
	private FormRepos formRepos;
	private User capturedBy;

	public Form(String name, FormRepos formRepos, User capturedBy) {
		super();
		this.name = name;
		this.formRepos = formRepos;
		this.capturedBy = capturedBy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FormRepos getFormRepos() {
		return formRepos;
	}

	public void setFormRepository(FormRepos formRepos) {
		this.formRepos = formRepos;
	}

	public User getCapturedBy() {
		return capturedBy;
	}

	public void setCapturedBy(User capturedBy) {
		this.capturedBy = capturedBy;
	}

}
