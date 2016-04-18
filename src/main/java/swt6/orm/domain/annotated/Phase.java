package swt6.orm.domain.annotated;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Phase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;

	private String name;
	
	@OneToMany(mappedBy="phaseId", cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<LogbookEntry> logbooks = new HashSet<>();
	
	public Phase() {
	}
	
	public Phase(String name) {
		this.name = name;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		
		logbook.attachPhase(this);
		logbooks.add(logbook);
	}
	
	public void removeLogbook(LogbookEntry logbook) {
		if (logbook == null) {
			throw new IllegalArgumentException("Cannot remove NULL logbook");
		}
		
		logbook.detachPhase();
		logbooks.remove(logbook);
	}
}
