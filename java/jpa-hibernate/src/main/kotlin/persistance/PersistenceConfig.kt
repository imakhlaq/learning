package persistance

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import org.hibernate.cfg.Environment

//hikariCp Datasource for connection pooling
private fun createHikariDataSource(): HikariDataSource {
    // Create HikariCP DataSource
    val config = HikariConfig();
    config.jdbcUrl = "jdbc:mysql://localhost:3306/my_database"; // Database URL
    config.username = "root"; // Database username
    config.password = "password"; // Database password
    config.driverClassName = "com.mysql.cj.jdbc.Driver"; // JDBC driver

    // Optional HikariCP settings for optimization
    config.maximumPoolSize = 10; // Max number of connections in the pool
    config.minimumIdle = 2; // Minimum number of idle connections
    config.idleTimeout = 30000; // Idle timeout in milliseconds
    config.maxLifetime = 1800000; // Max lifetime of a connection in milliseconds
    config.connectionTimeout = 30000; // Connection timeout in milliseconds

    return HikariDataSource(config);
}

// Create EntityManagerFactory
public fun createEntityManagerFactory(): EntityManagerFactory {
    val properties = HashMap<String, Any>();

    // Set the data source
    properties[Environment.DATASOURCE] = createHikariDataSource();

    // Hibernate properties
    properties[Environment.DIALECT] = "org.hibernate.dialect.MySQLDialect";
    properties[Environment.HBM2DDL_AUTO] = "update";
    properties[Environment.SHOW_SQL] = "true";
    properties[Environment.FORMAT_SQL] = "true";

    // JPA-specific properties
    properties["jakarta.persistence.schema-generation.database.action"] = "update";

    // Create EntityManagerFactory
    return Persistence.createEntityManagerFactory("my-persistence-unit", properties);
}