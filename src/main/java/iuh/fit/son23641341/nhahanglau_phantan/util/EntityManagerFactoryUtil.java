package iuh.fit.son23641341.nhahanglau_phantan.util; // Lưu ý tên package cho khớp với máy bạn

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryUtil {
    private static EntityManagerFactory factory;

    public static EntityManager getEntityManager() {
        if (factory == null) {
            synchronized (EntityManagerFactoryUtil.class) {
                if (factory == null) {
                    try {
                        factory = Persistence.createEntityManagerFactory("mariadb-pu");
                    } catch (Throwable ex) {
                        System.err.println("JPA Factory creation failed (Expected on Client side): " + ex.getMessage());
                        // Trả về null hoặc ném exception tùy ngữ cảnh, 
                        // nhưng ít nhất nó không làm crash ứng dụng ngay khi load class.
                    }
                }
            }
        }
        return factory != null ? factory.createEntityManager() : null;
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}