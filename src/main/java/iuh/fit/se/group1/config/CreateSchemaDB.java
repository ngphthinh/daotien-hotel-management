package iuh.fit.se.group1.config;

import jakarta.persistence.Persistence;

public class CreateSchemaDB {
    public static void main(String[] args) {
        Persistence.createEntityManagerFactory("daotien").createEntityManager();
    }
}
