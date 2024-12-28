package org.example.persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import org.example.models.Product;
import org.hibernate.cfg.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PersistenceConfig {

    private static EntityManager entityManager = null;

    public static EntityManager getEntityManager() throws IOException {

        if (entityManager == null) {
            synchronized (PersistenceConfig.class) {
                if (entityManager == null)
                    entityManager = new PersistenceConfig().createEntityManager();
            }
        }
        return entityManager;
    }

    private DataSource createHikariDataSource() throws IOException {

        var filePath = "F:\\dev\\learning\\java\\jpa-hibernate\\src\\main\\resources\\META-INF\\hibernate.properties";
        var propertiesFile = new Properties();
        propertiesFile.load(Files.newInputStream(Path.of(filePath)));

        // Create HikariCP DataSource
        var config = new HikariConfig();
        config.setJdbcUrl(propertiesFile.getProperty("hibernate.connection.url"));// Database URL
        config.setUsername(propertiesFile.getProperty("hibernate.username"));// Database username
        config.setPassword(propertiesFile.getProperty("hibernate.password"));// Database password
        config.setDriverClassName(propertiesFile.getProperty("hibernate.driverClassName"));// JDBC driver

        config.setMaximumPoolSize(10);// Max number of connections in the pool
        config.setMinimumIdle(2);// Minimum number of idle connections
        config.setIdleTimeout(30000);// Idle timeout in milliseconds
        config.setMaxLifetime(1800000);// Max lifetime of a connection in milliseconds
        config.setConnectionTimeout(30000);// Connection timeout in milliseconds

        return new HikariDataSource(config);
    }

    private EntityManager createEntityManager() throws IOException {

        var filePath = "F:\\dev\\learning\\java\\jpa-hibernate\\src\\main\\resources\\META-INF\\hibernate.properties";
        var propertiesFile = new Properties();
        propertiesFile.load(Files.newInputStream(Path.of(filePath)));

        var dataSource = this.createHikariDataSource();

        var config = new Configuration()
            .setProperty(
                "hibernate.connection.provider_class",
                "org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl"
            )
            .setProperty(
                "hibernate.hikari.dataSource",
                dataSource.toString()
            ) // Important: Setting the HikariCP DataSource
            .addAnnotatedClass(Product.class)
            .setDatasource("jdbc/jpa-db")
            .buildSessionFactory();

        return config.createEntityManager();
    }
}