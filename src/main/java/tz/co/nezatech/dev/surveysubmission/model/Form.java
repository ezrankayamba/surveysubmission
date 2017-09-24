package tz.co.nezatech.dev.surveysubmission.model;

import java.util.LinkedList;
import java.util.List;

public class Form extends BaseData {
	private int id;
	private String name;
	private FormRepos formRepos;
	private User capturedBy;
	private List<FormData> dataList;
	private String reposVersion;
	private String reposPath;

	public Form(int id, String reposPath, String reposVersion, String name, FormRepos formRepos, User capturedBy) {
		super();
		this.name = name;
		this.formRepos = formRepos;
		this.capturedBy = capturedBy;
		this.dataList = new LinkedList<>();
		this.id = id;
		this.reposVersion = reposVersion;
		this.reposPath = reposPath;
	}

	public String getReposPath() {
		return reposPath;
	}

	public void setReposPath(String reposPath) {
		this.reposPath = reposPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReposVersion() {
		return reposVersion;
	}

	public void setReposVersion(String reposVersion) {
		this.reposVersion = reposVersion;
	}

	public void setFormRepos(FormRepos formRepos) {
		this.formRepos = formRepos;
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

	public List<FormData> getDataList() {
		return dataList;
	}

	public void setDataList(List<FormData> dataList) {
		this.dataList = dataList;
	}

}
