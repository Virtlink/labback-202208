package nl.tudelft.labback.jobs

import nl.tudelft.labback.utils.Event
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.time.Duration

class JobsApiOkhttpClient(
    private val client: OkHttpClient,
): JobsApi {
    override fun getAll(): List<JobInfo> {
        val request = Request.Builder()
            .url("https://publicobject.com/helloworld.txt")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                println("$name: $value")
            }

            response.body!!.
            println(response.body!!.string())
        }

    }

    override fun create(id: JobID, prototypeName: String?, description: JobDescription): JobInfo {
        TODO("Not yet implemented")
    }

    override fun get(id: JobID): JobInfo? {
        TODO("Not yet implemented")
    }

    override fun schedule(id: JobID): JobInfo {
        TODO("Not yet implemented")
    }

    override fun cancel(id: JobID): JobInfo {
        TODO("Not yet implemented")
    }

    override fun dispose(id: JobID): Boolean {
        TODO("Not yet implemented")
    }

    override fun await(id: JobID, state: JobState, timeout: Duration): JobInfo? {
        TODO("Not yet implemented")
    }

    override val onJobStateChanged: Event<JobsApi.JobStateChangedEventArgs>
        get() = TODO("Not yet implemented")
}