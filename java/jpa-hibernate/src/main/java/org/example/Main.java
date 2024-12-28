package org.example;

import org.example.models.Product;
import org.example.persistance.PersistenceConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");

        var entityManager = PersistenceConfig.getEntityManager();

        var tsx = entityManager.getTransaction();

        tsx.begin();
        var p1 = new Product();
        p1.id = 1L;
        p1.name = "GTX Geforce 1660 Super";

        entityManager.persist(p1);
        tsx.commit();
    }
}