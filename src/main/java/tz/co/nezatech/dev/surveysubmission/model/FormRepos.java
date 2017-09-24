package tz.co.nezatech.dev.surveysubmission.model;

public class FormRepos extends BaseData {
	private int id;
	private String name, description, filepath;
	private Project project;
	private String version;

	public FormRepos() {
		super();
	}

	public FormRepos(String version, String name, String description, String filepath, Project project) {
		super();
		this.name = name;
		this.description = description;
		this.filepath = filepath;
		this.project = project;
		this.version=version; 
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "FormRepos [id=" + id + ", name=" + name + ", description=" + description + ", filepath=" + filepath
				+ ", project=" + project + "]";
	}

}
