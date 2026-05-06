package iuh.fit.son23641341.nhahanglau_phantan.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// NOTE: Hibernate bootstrap added for Maven/Jakarta EE setup; mock data still used in app.
public final class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
