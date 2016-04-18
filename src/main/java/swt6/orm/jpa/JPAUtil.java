package swt6.orm.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAUtil {

  private static EntityManagerFactory emFactory;
  private static ThreadLocal<EntityManager> emThread = new ThreadLocal<EntityManager>();

  public static EntityManagerFactory getEntityManagerFactory() {
    if (emFactory == null) {
      emFactory = Persistence.createEntityManagerFactory("WorklogPU");
    }
    return emFactory;
  }

  public static EntityManager getEntityManager() {
    if (emThread.get() == null) {
      emThread.set(getEntityManagerFactory().createEntityManager());
    }

    return emThread.get();
  }

  public static void closeEntityManager() {
    if (emThread.get() != null) {
      emThread.get().close();
      emThread.set(null);
    }
  }

  public static EntityManager getTransactedEntityManager() {
    EntityManager em = getEntityManager();
    EntityTransaction tx = em.getTransaction();
    if (!tx.isActive()) {
      tx.begin();
    }
    return em;
  }

  public static void commit() {
    EntityManager em = getEntityManager();
    EntityTransaction tx = em.getTransaction();
    if (tx.isActive()) {
      tx.commit();
    }
    closeEntityManager();
  }

  public static void rollback() {
    EntityManager em = getEntityManager();
    EntityTransaction tx = em.getTransaction();
    if (tx.isActive()) {
      tx.rollback();
    }
    closeEntityManager();
  }

  public static void closeEntityManagerFactory() {
    if (emFactory != null) {
      emFactory.close();
      emFactory = null;
    }
  }
}
