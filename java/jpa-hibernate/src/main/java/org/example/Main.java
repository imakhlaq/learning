package org.example;

import org.example.models.Product;
import org.example.persistance.PersistenceConfig;

import java.io.IOException;

public class Main {

    public static void createProduct(Product product) {

    }

    public static void main(String[] args) throws IOException {

        var entityManagerFactory = PersistenceConfig.getEntityManagerFactory();

        var entityManager = entityManagerFactory.createEntityManager();//context that stores objects and after  tsx.commit(); it will sink to db (Level1 cache)

        var tsx = entityManager.getTransaction();

        tsx.begin();
        var p1 = new Product();
        p1.id = "33";
        p1.name = "GTX Geforce 1660 Super";

        entityManager.persist(p1);
        tsx.commit();
    }
}