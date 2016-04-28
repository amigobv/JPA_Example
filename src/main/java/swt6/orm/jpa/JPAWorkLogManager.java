package swt6.orm.jpa;

import java.text.DateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import swt6.orm.domain.annotated.Address;
import swt6.orm.domain.annotated.Employee;
import swt6.orm.domain.annotated.LogbookEntry;
import swt6.orm.domain.annotated.Module;
import swt6.orm.domain.annotated.PermanentEmployee;
import swt6.orm.domain.annotated.Phase;
import swt6.orm.domain.annotated.Project;
import swt6.orm.domain.annotated.TemporaryEmployee;
import swt6.util.DateUtil;

public class JPAWorkLogManager {
	private static final DateFormat fmt = DateFormat.getDateTimeInstance();

	private static Long saveEmployee(Employee empl) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		em.persist(empl);

		JPAUtil.commit();
		return empl.getId();
	}
	
	private static Long savePhase(Phase phase) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		
		em.persist(phase);
		
		JPAUtil.commit();
		return phase.getId();
	}
	
	private static Long saveModule(Module module, Project project) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		
		project = em.merge(project);
		
		module.setProjectId(project);
		em.persist(module);
		
		JPAUtil.commit();
		return module.getId();
	}
	
	private static Long saveProject(Project project, Employee leader) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		
		leader = em.merge(leader);
		project.attachLeader(leader);
		em.persist(project);
		
		JPAUtil.commit();
		return project.getId();
	}
	
	private static void listPhases() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		
		List<Phase> phaseList = em.createQuery("select p from Phase p", Phase.class).getResultList();
		
		for (Phase p : phaseList) {
			System.out.println(p.getName());
			
			if (p.getLogbooks().size() > 0) {
				for (LogbookEntry entry : p.getLogbooks()) {
					System.out.println(" " + entry.getActivity() + ": " + entry.getStartTime() + " - " + entry.getEndTime());
				}
			}
		}
	}

	private static void listEmployees() {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		List<Employee> empList = em.createQuery("select e from Employee e", Employee.class).getResultList();

		for (Employee e : empList) {
			System.out.println(e);

			if (e.getLogbookEntries().size() > 0) {
				System.out.println(" logbookEntries:");
				for (LogbookEntry lbe : e.getLogbookEntries()) {
					System.out.println(" " + lbe.getActivity() + ": " + lbe.getStartTime() + " - " + lbe.getEndTime());
				}
			}

		}

		JPAUtil.commit();
	}

	private static void addLogbookEntries(Employee empl, Phase phase, Module module) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		empl = em.merge(empl);
		phase = em.merge(phase);
		module = em.merge(module);

		// method 1
		LogbookEntry entry = new LogbookEntry("Analyse", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30));
		entry.attachEmployee(empl);
		entry.attachPhase(phase);
		entry.attachModule(module);

		// method 2
		empl.addLogbookEntry(new LogbookEntry("Implementierung", DateUtil.getTime(8, 45), DateUtil.getTime(17, 15), phase, module));
		empl.addLogbookEntry(new LogbookEntry("Testen", DateUtil.getTime(12, 30), DateUtil.getTime(17, 00), phase, module));

		JPAUtil.commit();

	}

	private static void getEmployee(long emplId) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		Employee empl = em.find(Employee.class, emplId);

		if (empl != null) {
			System.out.println(empl);

			for (LogbookEntry lbe : empl.getLogbookEntries()) {
				System.out.println(" " + lbe.getActivity() + ": " + fmt.format(lbe.getStartTime()) + " - "
						+ fmt.format(lbe.getEndTime()));
			}
		}

		JPAUtil.commit();
	}

	private static void getLogbookEntry(long entryId) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		LogbookEntry entry = em.find(LogbookEntry.class, entryId);

		if (entry != null) {
			System.out.println("Logbookentry: " + entry + " -->" + entry.getEmployee());
		}

		JPAUtil.commit();
	}
	
	private static void assignProjectToModule(Module module, Project project) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		
		project = em.merge(project);
		
		
	}

	private static void assignProjectsToEmployees(Employee empl1, Employee empl2) {

		EntityManager em = JPAUtil.getTransactedEntityManager();

		empl1 = em.merge(empl1);
		empl2 = em.merge(empl2);

		Project p1 = new Project("Office", empl1);
		Project p2 = new Project("Enterprise Server", empl1);

		empl1.attachProject(p1);
		empl1.attachProject(p2);

		p2.addMember(empl2);

		// empl1 = em.merge(empl1);
		// p2 = em.merge(p2);

		System.out.println(empl1);
		System.out.println(" projects:");
		for (Project p : empl1.getProjects()) {
			System.out.println(" " + p.toString());
		}
		System.out.println(empl2);
		System.out.println(" projects:");
		for (Project p : empl2.getProjects()) {
			System.out.println(" " + p.toString());
		}

		JPAUtil.commit();

	}

	private static void listLogbookEntriesOfEmployee(Employee empl1) {

		EntityManager em = JPAUtil.getTransactedEntityManager();

		System.out.println("logbook entries of employee: " + empl1.getLastName() + " (" + empl1.getId() + ")");

		// Keep in mind: JPA-QL queries refer to Java (domain) objects not to
		// database tables.

		// Version 1:
		// Query qry = em.createQuery("from LogbookEntry where
		// employee.id=:emplId")
		// .setParameter("emplId", empl1.getId());

		// Version 2:
		TypedQuery<LogbookEntry> qry = em.createQuery("from LogbookEntry where employee=:emplId", LogbookEntry.class)
				.setParameter("emplId", empl1);

		List<LogbookEntry> entries = qry.getResultList();
		for (LogbookEntry entry : entries) {
			System.out.println(entry);
		}
		JPAUtil.commit();
	}

	public static void listEmployeesOfProject(Long projectId) {

		EntityManager em = JPAUtil.getTransactedEntityManager();

		Project proj = em.find(Project.class, projectId);
		if (proj == null) {
			System.out.format("Project with id <%d> not found.%n", projectId);
			return;
		}

		// Query qry =
		// em.createQuery("select e from Employee e where :proj in elements
		// (e.projects)");
		// Query qry =
		// em.createQuery("select e from Employee e where :proj member of
		// e.projects");
		TypedQuery<Employee> qry = em.createQuery("select e from Employee e, in (e.projects) p where p = :proj",
				Employee.class);
		qry.setParameter("proj", proj);
		List<Employee> empls = qry.getResultList();

		System.out.format("employees of project <%s>%n", proj);
		for (Employee empl : empls) {
			System.out.println(" " + empl);
		}

		JPAUtil.commit();

	}

	public static void main(String[] args) {
		try {
			System.out.println("----- create schema -----");
			JPAUtil.getEntityManager();

			// Employee empl1 = new Employee("Franz", "Mayr",
			// DateUtil.getDate(1980, 12, 24));
			// Employee empl2 = new Employee("Bill", "Gates",
			// DateUtil.getDate(1970, 1, 21));
			// empl1.setAddress(new Address("4232", "Hagenberg", "Hauptstraße
			// 1"));
			// empl2.setAddress(new Address("77777", "Redmond", "Clinton
			// Street"));

			PermanentEmployee empl1 = new PermanentEmployee("Franz", "Mayr", DateUtil.getDate(1980, 12, 24));
			empl1.setAddress(new Address("4232", "Hagenberg", "Hauptstraße 1"));
			empl1.setSalary(5000.0);

			TemporaryEmployee empl2 = new TemporaryEmployee("Bill", "Gates", DateUtil.getDate(1970, 1, 21));
			empl2.setAddress(new Address("77777", "Redmond", "Clinton Street"));
			empl2.setHourlyRate(50.0);
			empl2.setRenter("Microsoft");
			empl2.setStartDate(DateUtil.getDate(2006, 3, 1));
			empl2.setEndDate(DateUtil.getDate(2006, 4, 1));
			
			PermanentEmployee empl3 = new PermanentEmployee("Heinz", "Dobler", DateUtil.getDate(1965, 10, 13));
			empl3.setAddress(new Address("4231", "Hagenberg","Hauptstraße 14"));
			empl3.setSalary(6000);
			
			Phase proto = new Phase("Prototype");
			Phase dev = new Phase("Development");
			Phase mantainance = new Phase("Maintainance");
			
			saveEmployee(empl3);
			Project zeus = new Project("Zeus");
			saveProject(zeus, empl3);
			
			Module mod1 = new Module("Module 1");
			Module mod2 = new Module("Module 2");
			Module mod3 = new Module("Module 3");
			System.out.println("----- savemodule -----");
			saveModule(mod1, zeus);
			saveModule(mod2, zeus);
			saveModule(mod3, zeus);

			System.out.println("----- saveEmployee -----");
			saveEmployee(empl1);

			System.out.println("----- saveEmployee -----");
			saveEmployee(empl2);
			
			System.out.println("----- savePhase -----");
			savePhase(proto);
			
			System.out.println("----- savePhase -----");
			savePhase(dev);
			
			System.out.println("----- savePhase -----");
			savePhase(mantainance);

			System.out.println("----- ListPhases -----");
			listPhases();
			
			System.out.println("----- getEmployee -----");
			// getEmployee(empl2.getId());

			System.out.println("----- addLogbookEntries -----");
			addLogbookEntries(empl1, dev, mod1);

			System.out.println("----- listEmployees -----");
			listEmployees();

			System.out.println("----- getLogbookEntry -----");
			getLogbookEntry(1L);

			System.out.println("----- assignProjectsToEmployees -----");
			assignProjectsToEmployees(empl1, empl2);

			System.out.println("----- listEmployeesOfProject -----");
			listEmployeesOfProject(32768L);
			listEmployeesOfProject(32769L);

			System.out.println("----- listEmployees -----");
			listEmployees();

			System.out.println("----- listLogbookEntriesOfEmployee -----");
			listLogbookEntriesOfEmployee(empl1);
		} finally {
			JPAUtil.closeEntityManagerFactory();
		}
	}

}
