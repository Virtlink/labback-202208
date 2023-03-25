package nl.tudelft.labback

import io.javalin.Javalin
import nl.tudelft.labback.jobs.JobsApiJavalinServer
import org.http4k.contract.Tag
import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.ApiServer
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.sse
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer
import kotlin.concurrent.thread

/**
 * A server that handles API requests, implemented using Javalin.
 *
 * Usage: instantiate this class and call [start] to start the server. Call [stop] to stop the server, or wrap the
 * using code in a `server.use { }` block to automatically stop the server when the block is exited.
 */
class LabbackJavalinServer(
    /** The port to listen on; or 0 to pick a random port. */
    port: Int = 3001,
    /** Whether to autostart the server. */
    autoStart: Boolean = false,
    /** The Labback client to call. */
    private val labback: LabbackClient,
): LabbackServer {

    private val engine = Javalin.create().apply {
        JobsApiJavalinServer.installRoutes(this, labback.jobs)
    }

//    private val thread: Thread = thread(start = false, isDaemon = true, name = "labback-server") {
//        engine.start(port)
//    }

    private val startPort = port

    init {
        if (autoStart) {
            // Starting the server.
            start(false)
        }
    }

    override fun start(wait: Boolean) {
        if (wait) TODO("Not yet supported")
        engine.start(startPort)
    }

    override fun stop() {
        engine.close()
    }

    override val port: Int
        get() = engine.port()

}