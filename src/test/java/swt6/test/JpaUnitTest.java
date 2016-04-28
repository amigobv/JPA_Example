package swt6.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.model.EachTestNotifier;

import swt6.orm.domain.annotated.Address;
import swt6.orm.domain.annotated.Employee;
import swt6.orm.domain.annotated.LogbookEntry;
import swt6.orm.domain.annotated.Module;
import swt6.orm.domain.annotated.PermanentEmployee;
import swt6.orm.domain.annotated.Phase;
import swt6.orm.domain.annotated.Project;
import swt6.orm.jpa.Dao;
import swt6.util.DateUtil;

public class JpaUnitTest {

	@Before
	public void setUp() throws Exception {
		Mock.GenerateEntities();
	}

	@After
	public void tearDown() throws Exception {
		Mock.Clean();
	}

	@Test
	public void insertNewEmployeeTest() {
		Employee empl = createTestEmployee();

		Employee dbEmpl = Dao.getEmployee(empl.getId());
		assertNotNull(dbEmpl);

		if (empl.getId() != dbEmpl.getId())
			fail("Id are not matching");

		Dao.removeEmployee(dbEmpl.getId());
	}

//	@Test
//	public void insertNewLogbookEntry() {
//	}
	@Test
	public void insertNewLogbookEntry() {
		Employee empl = Dao.getEmployee(createTestEmployee().getId());

		Module mod = Dao.getItemById(Module.class, 1L);
		assertNotNull(mod);

		Phase ph = Dao.getItemById(Phase.class, 1L);
		assertNotNull(ph);
		
		LogbookEntry entry = new LogbookEntry("Testing", DateUtil.getTime(12, 0), DateUtil.getTime(13, 30));
		empl = Dao.addLogbookEntry(entry, empl.getId(), ph.getId(), mod.getId());
		assertNotNull(empl);

		LogbookEntry dbEntry = Dao.getItemById(LogbookEntry.class,
				entry.getId());
		assertNotNull(dbEntry);
		assertEquals(entry.getId(), dbEntry.getId());
		assertEquals(entry.getActivity(), dbEntry.getActivity());

		Dao.removeLogbookEntry(entry);
		Dao.removeEmployee(empl.getId());
	}

	@Test
	public void assignEmployeeToProject() {
		Employee empl = Dao.getItemById(Employee.class,
				createTestEmployee().getId());

		List<Project> projects = Dao.getAllProjects();
		assertNotNull(projects);

		Project proj = projects.get(0);
		assertNotNull(proj);

		empl = Dao.updateEmployee(empl, proj);
		assertTrue(empl.getProjects().contains(proj));

		Dao.removeEmployee(empl.getId());
	}

	@Test
	public void assignEmployeeAsProjectLeader() {
		Employee empl = Dao.getItemById(Employee.class,
				createTestEmployee().getId());

		List<Project> projects = Dao.getAllProjects();
		assertNotNull(projects);

		Project proj = projects.get(0);
		assertNotNull(proj);

		Dao.updateLeader(empl, proj);

		empl = Dao.getEmployee(empl.getId());
		assertTrue(empl.getLeaders().contains(proj));

		Dao.removeEmployee(empl.getId());
	}

	@Test
	public void assignEntryWithNewPhase() {
		Phase ph = new Phase("Release");
		Dao.saveEntity(ph);

		List<PermanentEmployee> employees = Dao.getAllPermanentEmployees();
		assertNotNull(employees);

		Employee empl = employees.get(0);
		assertNotNull(empl);
		
		List<Module> modules = Dao.getAllModules();
		assertNotNull(modules);
		
		Module mod = modules.get(0);
		assertNotNull(mod);

		LogbookEntry entry = new LogbookEntry("Release", DateUtil.getTime(14, 0), DateUtil.getTime(16, 30));
		empl.addLogbookEntry(entry);
		Dao.addLogbookEntry(entry, empl.getId(), ph.getId(), mod.getId());

		Dao.removeLogbookEntry(entry);
		Dao.removeEntity(Phase.class, ph.getId());
	}

	@Test
	public void assignEntryWithNewModule() {
		List<Project> projects = Dao.getAllProjects();
		assertNotNull(projects);
		assertNotEquals(0, projects.size());
		
		Project proj = projects.get(0);
		
		Module mod = new Module("Delivery");
		Dao.saveModule(mod, proj);

		List<PermanentEmployee> employees = Dao.getAllPermanentEmployees();
		assertNotNull(employees);

		Employee empl = employees.get(0);
		assertNotNull(empl);
		
		List<Phase> phases = Dao.getAllPhases();
		assertNotNull(phases);
		
		Phase ph = phases.get(0);
		assertNotNull(ph);

		LogbookEntry entry = new LogbookEntry("Release", DateUtil.getTime(14, 0), DateUtil.getTime(16, 30));
		empl.addLogbookEntry(entry);
		Dao.addLogbookEntry(entry, empl.getId(), ph.getId(), mod.getId());

		Dao.removeLogbookEntry(entry);
		Dao.removeEntity(Module.class, mod.getId());
	}

	@Test
	public void removeLogbookEntryFromEmployee() {
		List<PermanentEmployee> employees = Dao.getAllPermanentEmployees();
		assertNotNull(employees);

		Employee empl = employees.get(employees.size()-2);
		assertNotNull(empl);

		List<LogbookEntry> entries = Dao.getLogbooksFromEmployee(empl.getId());
		assertNotNull(entries);

		LogbookEntry entry = entries.get(0);
		assertNotNull(entry);

		Dao.removeLogbookEntry(entry);
		
		List<LogbookEntry> entriesAfterDelete = Dao.getLogbooksFromEmployee(empl.getId());
		assertNotNull(entriesAfterDelete);

		assertEquals(1, entries.size() - entriesAfterDelete.size());
	}

	@Test
	public void insertNewProject() {
		Employee empl = Dao.getItemById(Employee.class,createTestEmployee().getId());

		Project proj = new Project("Unit Test", empl);
		proj = Dao.updateProject(proj);
		
		List<Project> projects = Dao.getAllProjects();
		assertNotNull(projects);
		assertTrue(projects.contains(proj));

		Dao.removeEmployee(empl.getId());
	}

	@Test
	public void removeEmployee() {
		List<PermanentEmployee> employees = Dao.getAllPermanentEmployees();
		assertNotNull(employees);
		
		Employee empl = employees.get(1);
		assertNotNull(empl);
		
		List<LogbookEntry> entries = Dao.getLogbooksFromEmployee(empl.getId());
		assertNotNull(entries);
		
		for(LogbookEntry entry : entries) {
			entry.detachEmployee();
			//Dao.removeItem(LogbookEntry.class, entry.getId());
		}
		
		Dao.removeEmployee(empl.getId());
		
		List<LogbookEntry> logbooks = Dao.getLogbooksFromEmployee(empl.getId());
		assertNotNull(logbooks);
		assertEquals(0, logbooks.size());
	}

	@Test
	public void changeEmployeeAddress() {
		List<PermanentEmployee> employees = Dao.getAllPermanentEmployees();
		assertNotNull(employees);
		
		Employee empl = employees.get(0);
		assertNotNull(empl);
		
		empl.setAddress(new Address("6969", "London", "Bakers Street 4"));
		Dao.updateEmployee(empl);
		
		Employee emplLoad = Dao.getEmployee(empl.getId());
		assertEquals(empl.getAddress(), emplLoad.getAddress());
	}

	private Employee createTestEmployee() {
		PermanentEmployee empl = new PermanentEmployee("Daniel", "Rotaru",
				DateUtil.getDate(1984, 7, 27));
		empl.setAddress(new Address("4020", "Linz", "Hauptstraße 1"));
		empl.setSalary(3000.0);

		Dao.saveEntity(empl);

		return empl;
	}
}
