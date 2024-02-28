package com.example.routes

import com.example.data.DataManager
import com.example.data.Order
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.util.UUID

@Location("/order/list")
data class OrderListLocation(val sortBy : String , val asc :Boolean)

fun Route.orders() {
    val orderManager = DataManager

    get<OrderListLocation>() {
        call.respond(orderManager.sortOrders(it.sortBy,it.asc))
    }

    route("/order") {
        authenticate("myauth1") {
            get("/") {
                call.respond(orderManager.getAllOrders())
            }
        }
        post("/") {
            val order = call.receive(Order::class)
            orderManager.addOrder(order)
            call.respond(order)
        }

        put("/") {
            val receive = call.receive(Order::class)
            val updateOrder = orderManager.updateOrder(receive)
                ?: return@put call.respond(HttpStatusCode.BadRequest)
            call.respond(updateOrder)
        }

        delete("/{id}") {
            val id : String = call.parameters["id"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val removeOrder : Order = orderManager.removeOrder(UUID.fromString(id))
            call.respond(removeOrder)
        }
    }
}