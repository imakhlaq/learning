package com.learn

import com.learn.entities.Product
import persistance.createEntityManagerFactory
import java.util.*

fun main() {

    val entityMangerFactory = createEntityManagerFactory();

    val entityManager = entityMangerFactory.createEntityManager()

    entityManager.transaction.begin()

    val p1 = Product()
    p1.id = UUID.randomUUID()
    p1.name = "Product name"

    println("id ${p1.id} name ${p1.name}")

}