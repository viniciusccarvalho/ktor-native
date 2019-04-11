package io.igx.kotlin

import io.igx.kotlin.model.Driver
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI

/**
 * @author vinicius
 *
 */

@UseExperimental(KtorExperimentalAPI::class)
fun main(args: Array<String>) {
    val server = embeddedServer(CIO, 8080, module = Application::module)
    server.start(wait = true)
}

fun Application.module() {
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
       get("/drivers"){
           call.respond(Driver(102, "Ayrton", "Senna", "Brazilian"))
       }
    }
}
