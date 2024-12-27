package com.learn.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class Product {

    @get:Id
    var id: UUID? = null

    var name: String? = null
    /*
     To set getter and setter
    var name: String? = null
     get()=field
     set(value){
     field=value
 }
 */

    /*
 To set getter and setter
var name: String? = null
 get()=field
 set(value){
 field=value
}
*/
}