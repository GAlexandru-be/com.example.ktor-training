package com.example.data

import io.ktor.server.plugins.*
import java.util.*
import kotlin.collections.ArrayList

class DataManager {


    var orders =  ArrayList<Order>()

    fun getAllOrders() : ArrayList<Order> {
        return orders
    }

    fun addOrder(order : Order) : Order {
        orders.add(order)
        return order
    }

    fun updateOrder(order : Order) : Order? {
        val orderForUpdate = orders.find {
            it.id == order.id
        }
        orderForUpdate?.name = order.name
        orderForUpdate?.price = order.price
        return orderForUpdate
    }

    fun removeOrder(id : UUID) : Order {
        val orderForRemoving = orders.find {
            it.id == id
        } ?: throw NotFoundException()
        orders.remove(orderForRemoving)
        return orderForRemoving
    }
}