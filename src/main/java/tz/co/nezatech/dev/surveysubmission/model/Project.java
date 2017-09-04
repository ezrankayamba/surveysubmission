package tz.co.nezatech.dev.surveysubmission.model;

import java.util.LinkedList;
import java.util.List;

public class Project extends BaseData {
	private int id;
	private String name, status;
	private List<FormRepos> repos;

	public Project() {
		super();
		repos = new LinkedList<>();
	}

	public Project(int id, String name, String status) {
		this();
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public List<FormRepos> getRepos() {
		return repos;
	}

	public void setRepos(List<FormRepos> repos) {
		this.repos = repos;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
