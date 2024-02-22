package com.example.data

import io.ktor.server.plugins.*
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.full.declaredMemberProperties

object DataManager {
    val log = LoggerFactory.getLogger(DataManager::class.java)

    var orders = ArrayList<Order>()

    init {
        addOrder(Order(UUID.fromString("e25a8e42-d090-12ee-a507-0242ac120006"),"PC" , 1500.0))
        addOrder(Order(UUID.fromString("e25a8e42-d090-12ee-a507-0242ac120007"),"XBOX" , 1000.0))
        addOrder(Order(UUID.fromString("e25a8e42-d090-12ee-a507-0242ac120008"),"PS" , 100.0))
    }

    fun sortOrders(sortBy: String, asc: Boolean): List<Order> {

        val member = Order::class.declaredMemberProperties.find { it.name == sortBy }
        if (member == null) {
            log.info("The field to sort by does not exist")
            return orders
        }

        return if (asc) {
            orders.sortedBy {
                member.get(it).toString()
            }
        } else {
            return orders.sortedByDescending {
                member.get(it).toString()
            }
        }
    }

    fun getAllOrders(): ArrayList<Order> {
        return orders
    }

    fun addOrder(order: Order): Order {
        orders.add(order)
        return order
    }

    fun updateOrder(order: Order): Order? {
        val orderForUpdate = orders.find {
            it.id == order.id
        }
        orderForUpdate?.name = order.name
        orderForUpdate?.price = order.price
        return orderForUpdate
    }

    fun removeOrder(id: UUID): Order {
        val orderForRemoving = orders.find {
            it.id == id
        } ?: throw NotFoundException()
        orders.remove(orderForRemoving)
        return orderForRemoving
    }
}