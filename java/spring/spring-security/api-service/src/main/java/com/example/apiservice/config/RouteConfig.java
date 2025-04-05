package com.example.apiservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder
            .routes()
            .route(
                path ->
                    path.path("/books/**")
                        //   .filters(p -> p.rewritePath("/resource", "/"))
                        .uri("lb://books")//url of book service
            )

            .route(
                path ->
                    path.path("/data/**")
                        //   .filters(p -> p.rewritePath("/resource", "/"))
                        .uri("lb://data")//url of data service
            ).build();
    }
}