package tz.co.nezatech.dev.surveysubmission.model;

import java.util.LinkedList;
import java.util.List;

public class User extends BaseData {
	private int id;
	private String username;
	private String password;
	private String email;
	private Role role;
	private boolean enabled;
	private List<Project> projects;

	public User() {
		super();
		projects = new LinkedList<Project>();
	}

	public User(String username, String password, String email, Role role) {
		this();
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
