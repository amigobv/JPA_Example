package swt6.orm.jpa;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import swt6.orm.domain.annotated.Employee;
import swt6.orm.domain.annotated.LogbookEntry;
import swt6.orm.domain.annotated.Module;
import swt6.orm.domain.annotated.PermanentEmployee;
import swt6.orm.domain.annotated.Phase;
import swt6.orm.domain.annotated.Project;

public class Dao {

	public static <T> void saveEntity(T entity) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		em.persist(entity);

		JPAUtil.commit();
	}

	public static Employee addLogbookEntry(LogbookEntry entry, Long emplId, Long phaseId, Long modId) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		Employee empl = null;
		
		try {
			empl = em.find(Employee.class, emplId);
			Phase ph = em.find(Phase.class, phaseId);
			Module mod = em.find(Module.class, modId);

			entry.setPhase(ph);
			entry.setModuleId(mod);
			entry.attachEmployee(empl);
			em.persist(entry);

			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
		
		return empl;
	}

	public static void saveModule(Module module, Project project) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			project = (Project) em.merge(project);
			module.attachProject(project);
			em.persist(module);
			JPAUtil.commit(); // session is closed automatically
		} catch (Exception e) {
			e.printStackTrace();
			JPAUtil.rollback();
		}
	}
	
	
	public static Project saveProject(Project project, Employee leader) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			project.attachLeader(leader);
			project = (Project) em.merge(project);
			em.persist(project);
			JPAUtil.commit(); // session is closed automatically
		} catch (Exception e) {
			e.printStackTrace();
			JPAUtil.rollback();
		}
		
		return project;
	}
	
	
	public static Project updateProject(Project project) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			project = em.merge(project);
			JPAUtil.commit(); // session is closed automatically
		} catch (Exception e) {
			e.printStackTrace();
			JPAUtil.rollback();
		}
		
		return project;
	}
	



	public static <T> T getEntity(Class<T> entity, Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		T item = null;

		try {
			item = em.find(entity, id);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
		return item;
	}

	public static <T> void removeEntity(Class c, Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		T item = null;

		try {
			item = (T) em.find(c, id);
			em.remove(item);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JPAUtil.rollback();
		}
	}

	public static Employee getEmployee(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		Employee empl = null;

		try {
			empl = em.find(Employee.class, id);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();
			JPAUtil.rollback();
		}

		return empl;
	}

	@SuppressWarnings("unchecked")
	public static void removeEmployee(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		Employee empl = null;

		try {
			empl = em.find(Employee.class, id);

			em.remove(empl);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
	}
	
	public static void removePermanentEmployee(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		PermanentEmployee empl = null;

		try {
			empl = em.find(PermanentEmployee.class, id);
			
			em.remove(empl);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<LogbookEntry> getAllLogbooks() {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		List<LogbookEntry> list = null;

		try {
			list = em.createQuery("select e from LogbookEntry e",
					LogbookEntry.class).getResultList();

			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public static List<Project> getAllProjects() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<Project> list = null;
		try {
			list = em.createQuery("select p from Project p", Project.class)
					.getResultList();

			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return list;
	}

	public static void updateEmployee(Employee empl) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			empl = (Employee) em.merge(empl);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
	}
	
	public static Employee updateEmployee(Employee empl, Project proj) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			empl = em.merge(empl);
			proj = em.merge(proj);
			empl.attachProject(proj);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
		
		return empl;
	}
	
	public static void updateLeader(Employee empl, Project proj) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			empl = em.merge(empl);
			proj = em.merge(proj);
			proj.attachLeader(empl);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
	}

	public static List<PermanentEmployee> getAllPermanentEmployees() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<PermanentEmployee> list = null;

		try {
			list = em.createQuery("select e from PermanentEmployee e", PermanentEmployee.class)
					.getResultList();

			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return list;
	}
	
	public static List<Phase> getAllPhases() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<Phase> list = null;

		try {
			list = em.createQuery("select e from Phase e", Phase.class)
					.getResultList();

			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return list;
	}
	
	public static List<Module> getAllModules() {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<Module> list = null;

		try {
			list = em.createQuery("select e from Module e", Module.class)
					.getResultList();

			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return list;
	}

	public static List<LogbookEntry> getLogbooksFromEmployee(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<LogbookEntry> list = null;

		try {
			list = em.createQuery(
					"select e from LogbookEntry e where employee_id = " + id,
					LogbookEntry.class).getResultList();

			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return list;
	}

	public static List<Employee> getAllEmployeesFromProject(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		List<Employee> list = null;

		try {
			List<Long> ids = em.createQuery(
					"select employee_id from ProjectEmployee where projetc_id = "
							+ id)
					.getResultList();

			for (Long i : ids) {
				Employee empl = Dao.getEmployee(i);
				list.add(empl);
			}

			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return list;
	}
	
	public static void removeLogbookEntry(LogbookEntry entry) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {		
			entry = em.find(LogbookEntry.class, entry.getId());
			entry.detachEmployee();
			entry.detachModule();
			entry.detachPhase();

			em.remove(entry);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
	}

	
	public static void removePhase(Phase phase) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			phase = em.find(Phase.class, phase.getId());
			em.remove(phase);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
	}
	
	public static void removeModule(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			Module module = em.find(Module.class, id);
			module.detachProject();
			em.remove(module);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
	}

	public static void removeProject(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		try {
			Project project = (Project) em.find(Project.class, id);
			em.remove(project);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}
	}

	public static <T> T getItemById(Class c, Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();

		T item = null;

		try {
			item = (T) em.find(c, id);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return item;
	}

	public static Module getModuleById(Long id) {
		EntityManager em = JPAUtil.getTransactedEntityManager();
		Module item = null;

		try {
			item = (Module) em.find(Module.class, id);
			JPAUtil.commit();
		} catch (Exception e) {
			e.printStackTrace();

			JPAUtil.rollback();
		}

		return item;
	}
}
