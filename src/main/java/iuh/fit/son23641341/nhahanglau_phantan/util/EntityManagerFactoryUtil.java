package iuh.fit.son23641341.nhahanglau_phantan.util; // Lưu ý tên package cho khớp với máy bạn

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryUtil {
    private static EntityManagerFactory factory;

    static {
        try {
            // Tên "mariadb-pu" phải khớp chính xác với name trong file persistence.xml
            factory = Persistence.createEntityManagerFactory("mariadb-pu");
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}