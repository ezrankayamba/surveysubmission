package tz.co.nezatech.dev.surveysubmission.model;

public class UserProject extends BaseData {
	private int id;
	private User user;
	private Project project;

	public UserProject(User user, Project project) {
		super();
		this.user = user;
		this.project = project;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
