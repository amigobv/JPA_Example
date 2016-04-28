package swt6.orm.domain.annotated;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class LogbookEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final DateFormat fmt = DateFormat.getDateTimeInstance();

	@Id
	@GeneratedValue
	private Long id;

	private String activity;

	@Temporal(TemporalType.TIME)
	private Date startTime;
	@Temporal(TemporalType.TIME)
	private Date endTime;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, optional = false)
	private Employee employee;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, optional = false)
	private Phase phaseId;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, optional = false )
	private Module moduleId;

	public LogbookEntry() {
	}

	public LogbookEntry(String activity, Date start, Date end) {
		this.activity = activity;
		startTime = start;
		endTime = end;
	}
	
	public LogbookEntry(String activity, Date start, Date end, Phase phase, Module module) {
		this(activity, start, end);
		attachPhase(phase); 
		attachModule(module);
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void attachEmployee(Employee employee) {
		// If this entry is already linked to some employee,
		// remove this link.
		if (this.employee != null) {
			this.employee.getLogbookEntries().remove(this);
		}

		// Add a bidirection link between this entry and employee.
		if (employee != null) {
			employee.getLogbookEntries().add(this);
		}
		this.employee = employee;
	}

	public void detachEmployee() {
		if (employee != null) {
			employee.getLogbookEntries().remove(this);
		}

		employee = null;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date start) {
		startTime = start;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date end) {
		endTime = end;
	}

	public Phase getPhase() {
		return phaseId;
	}

	public void setPhase(Phase phase) {
		this.phaseId = phase;
	}
	
	public void attachPhase(Phase phase) {
		if (phase == null)
			throw new IllegalArgumentException("Cannot attach NULL phase!");
		
		if (this.phaseId != null) {
			this.phaseId.getLogbooks().remove(this);
		}

		phase.getLogbooks().add(this);
		this.phaseId = phase;
	}
	
	public void detachPhase() {
		if (phaseId != null) {
			phaseId.getLogbooks().remove(this);
		}
		
		phaseId = null;
	}

	public Module getModuleId() {
		return moduleId;
	}

	public void setModuleId(Module moduleId) {
		this.moduleId = moduleId;
	}

	public void attachModule(Module module) {
		if (module == null)
			throw new IllegalArgumentException("Cannot attach NULL module!");
		
		if (this.moduleId != null) {
			this.moduleId.getLogbooks().remove(this);
		}
		
		module.getLogbooks().add(this);
		moduleId = module;
	}
	
	public void detachModule() {
		if (moduleId != null) {
			moduleId.getLogbooks().remove(this);
		}
		
		moduleId = null;
	}
	
	@Override
	public boolean equals(Object obj) {
		LogbookEntry entry = (LogbookEntry) obj;

		return startTime.equals(entry.startTime) &&
			   endTime.equals(entry.endTime) &&
			   activity.equals(entry.activity);
	}
	
	@Override
	public int hashCode() {
		if (startTime != null && endTime != null && activity != null)
			return startTime.hashCode() + endTime.hashCode() + activity.hashCode();
		
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		return activity + ": " + fmt.format(startTime) + " - " + fmt.format(endTime) + " ("
				+ getEmployee().getLastName() + ")";

	}
}
