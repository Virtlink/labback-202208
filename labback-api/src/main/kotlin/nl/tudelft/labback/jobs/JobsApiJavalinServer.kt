package nl.tudelft.labback.jobs

import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import io.javalin.http.pathParamAsClass
import nl.tudelft.labback.api.JobNotFoundException

/**
 * Installs the Javalin server routes.
 */
object JobsApiJavalinServer {

    fun installRoutes(app: Javalin, api: JobsApi) {
        app.get("/api/v1/jobs") { ctx ->
            // Action
            val jobs = api.getAll()
            // Response
            ctx.status(HttpStatus.OK).json(jobs)
        }

        app.post("/api/v1/jobs") { ctx ->
            // Request
            val (id, prototype, description) = ctx.bodyAsClass<JobCreateRequest>()
            // Action
            val job = api.create(id, prototype, description)
            // Response
            ctx.status(HttpStatus.CREATED).json(job)
        }

        app.get("/api/v1/jobs/{id}") { ctx ->
            // Request
            val id = ctx.pathParamAsClass<JobID>("id").get()
            // Action
            val job = api.get(id) ?: throw JobNotFoundException(id)
            // Response
            ctx.status(HttpStatus.OK).json(job)
            // TODO: Handle exceptions
        }

        app.put("/api/v1/jobs/{id}/schedule") { ctx ->
            // Request
            val id = ctx.pathParamAsClass<JobID>("id").get()
            // Action
            val job = api.schedule(id)
            // Response
            ctx.status(HttpStatus.OK).json(job)
        }

        app.put("/api/v1/jobs/{id}/cancel") { ctx ->
            // Request
            val id = ctx.pathParamAsClass<JobID>("id").get()
            // Action
            val job = api.cancel(id)
            // Response
            ctx.status(HttpStatus.OK).json(job)
        }

        app.delete("/api/v1/jobs/{id}") { ctx ->
            // Request
            val id = ctx.pathParamAsClass<JobID>("id").get()
            // Action
            val disposed = api.dispose(id)
            // Response
            if (disposed) ctx.status(HttpStatus.NO_CONTENT)
            else ctx.status(HttpStatus.NOT_FOUND)
        }

        app.sse("/api/v1/jobs") { client ->
            val handler: (JobsApi.JobStateChangedEventArgs) -> Unit = { args ->
                client.keepAlive()
                client.sendEvent("job-state-changed", args)
            }
            api.onJobStateChanged += handler
            client.onClose { api.onJobStateChanged -= handler }
        }
    }

}