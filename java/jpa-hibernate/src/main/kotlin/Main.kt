package com.learn

import com.learn.entities.Product
import java.util.*

fun main() {

    val p1 = Product()
    p1.id = UUID.randomUUID()
    p1.name = "Product name"

    println("id ${p1.id} name ${p1.name}")

}