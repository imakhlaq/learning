package org.example.persistance;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.io.IOException;
import java.util.HashMap;

/*
EntityManagerFactory is only one for one db.
Because creating instance EntityManagerFactory is resource intensive so this class make sure EntityManagerFactory have only one instance (Singleton).
 */
public class PersistenceConfig {

    private static EntityManagerFactory entityManagerFactory = null;

    public static EntityManagerFactory getEntityManagerFactory() throws IOException {

        if (entityManagerFactory == null) {
            synchronized (PersistenceConfig.class) {
                if (entityManagerFactory == null) {

                    var hibernateProps = new HashMap<String, String>();
                    hibernateProps.put("hibernate.show_sql", "true");
                    hibernateProps.put("hibernate.hbm2ddl", "update");//create, none, update

                    entityManagerFactory = new HibernatePersistenceProvider()
                        .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo(), hibernateProps);
                }
            }
        }
        return entityManagerFactory;
    }
}