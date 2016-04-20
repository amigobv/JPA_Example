package swt6.orm.domain.annotated;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Module implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	
	@OneToMany(mappedBy="moduleId", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<LogbookEntry> logbooks = new HashSet<>();
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER, optional = false)
	private Project projectId;
	
	public Module() {		
	}
	
	public Module(String name) {
		this.name = name; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<LogbookEntry> getLogbooks() {
		return logbooks;
	}

	public void setLogbooks(Set<LogbookEntry> logbooks) {
		this.logbooks = logbooks;
	}
	
	public void addLogbook(LogbookEntry logbook) {
		if (logbook == null) {
			throw new IllegalArgumentException("Cannot add NULL logbook");
		}
		
		logbook.attachModule(this);
		logbooks.add(logbook);
	}
	
	public void removeLogbook(LogbookEntry logbook) {
		if (logbook == null) {
			throw new IllegalArgumentException("Cannot remove NULL logbook");
		}
		
		logbook.detachModule();
		logbooks.remove(logbook);
	}

	public Project getProjectId() {
		return projectId;
	}

	public void setProjectId(Project projectId) {
		this.projectId = projectId;
	}
	
	public void attachProject(Project project) {
		if (project == null)
			throw new IllegalArgumentException("Cannot attach NULL project!");
		
		if(projectId != null) {
			projectId.getModules().remove(this);
		}
		
		project.getModules().add(this);
		projectId = project;
	}
	
	public void detachProject() {
		if (this.projectId != null) {
			projectId.getModules().remove(this);
		}
		
		projectId = null;
	}
}
