package swt6.orm.domain.annotated;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Project implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, optional = false )
	private Employee leader;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "ProjectEmployee", joinColumns = { @JoinColumn(name = "project_id") }, inverseJoinColumns = {
			@JoinColumn(name = "employee_id") })
	private Set<Employee> members = new HashSet<>();

	@OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<Module> modules = new HashSet<>();

	public Project() {
	}

	public Project(String name) {
		this.name = name;
	}
	
	public Project(String name, Employee leader) {
		this(name);
		attachLeader(leader);
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Employee getLeader() {
		return leader;
	}

	public void setLeader(Employee leader) {
		this.leader = leader;
	}
	
	public void attachLeader(Employee leader) {
		if (leader == null)
			throw new IllegalArgumentException("Cannot attach NULL leader!");
		
		if (this.leader != null) {
			this.leader.getProjects().remove(this);
		}

		leader.getProjects().add(this);
		this.leader = leader;
	}
	
	public void detachLeader() {
		if (leader != null) {
			leader.getProjects().remove(this);
		}
		
		leader = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Employee> getMembers() {
		return members;
	}

	public void setMembers(Set<Employee> members) {
		this.members = members;
	}

	public void addMember(Employee empl) {
		if (empl == null) {
			throw new IllegalArgumentException("Null Employee");
		}
		empl.getProjects().add(this);
		members.add(empl);
	}

	public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}
	
	public void addModule(Module module) {
		if (module == null) {
			throw new IllegalArgumentException("Null Module");
		}
		
		module.setProjectId(this);
		this.modules.add(module);
	}

	@Override
	public String toString() {
		return name;
	}
}
