package org.example.dao;

import org.example.models.Product;
import org.example.persistance.PersistenceConfig;

import java.io.IOException;

public class ProductDAO {

    public static void createProductInDB(Product product) throws IOException {

        var entityManagerFactory = PersistenceConfig.getEntityManagerFactory();
        //context that stores objects and after  tsx.commit(); it will sink to db (Level1 cache)
        var entityManager = entityManagerFactory.createEntityManager();

        var tsx = entityManager.getTransaction();

        tsx.begin();
        var p1 = new Product();
        p1.id = "33";
        p1.name = "GTX Geforce 1660 Super";

        entityManager.persist(p1);
        tsx.commit();

    }

    public static void updateProductDAO() throws IOException {

        var entityManagerFactory = PersistenceConfig.getEntityManagerFactory();
        var em = entityManagerFactory.createEntityManager();
        var tsx = em.getTransaction();

        var detail = em.find(Product.class, 1L);

    }
}