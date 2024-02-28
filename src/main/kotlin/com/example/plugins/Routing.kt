package com.example.plugins

import com.example.data.Person
import com.example.data.dto.PersonDto
import com.example.extensions.toDto
import com.example.extensions.toPerson
import com.example.routes.orders
import com.example.service.PersonService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val service = PersonService()
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        orders()
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }

        route("/person") {
            get("/{id}") {
                val id = call.parameters["id"].toString()
                service.findById(id)
                    ?.let { foundPerson -> call.respond(foundPerson.toDto()) }
                    ?: call.respond(HttpStatusCode.NotFound)
            }
            get {
                val peopleList =
                    service.findAll()
                        .map(Person::toDto)
                call.respond(peopleList)
            }
            post {
                val request = call.receive<PersonDto>()
                val person = request.toPerson()
                service.create(person)
                    ?.let { userId ->
                        call.response.headers.append("My-User-Id-Header", userId.toString())
                        call.respond(HttpStatusCode.Created)
                    } ?: call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
