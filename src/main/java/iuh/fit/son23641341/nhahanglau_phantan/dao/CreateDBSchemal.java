package iuh.fit.son23641341.nhahanglau_phantan.dao;

import jakarta.persistence.Persistence;

public class CreateDBSchemal {
    public static void main(String[] args) {
        Persistence.createEntityManagerFactory("mariadb-pu")
                .createEntityManager();
    }
}
