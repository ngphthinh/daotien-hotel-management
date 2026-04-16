package iuh.fit.se.group1.config;

import iuh.fit.se.group1.infrastructure.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class CreateSchemaDB {
    public static void main(String[] args) {
        JPAUtil.getEntityManager();
        InitData.initAllData();
    }
}
