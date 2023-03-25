package nl.tudelft.labback.jobs

import nl.tudelft.labback.api.MyJackson.auto
import nl.tudelft.labback.application.ApplicationID
import org.http4k.asString
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.lens.BiDiBodyLens
import java.time.Instant
import kotlin.test.Test

class LensTests {

    data class TestData(
        val id: ApplicationID,
        val value: String,
    )

    @Test
    fun testJobInfo() {
        val testDataLens = Body.auto<TestData>().toLens()
        val jobInfoLens = Body.auto<JobInfo>().toLens()

//        val s = jobInfoLens.asString(JobInfo(
//            JobID("job1"),
//            ApplicationID("app1"),
//            OneShotTestJobDescription(image = "image1"),
//            JobMetrics(Instant.now(), readyAt = Instant.now()),
//            JobState.Ready,
//        ))
//        println(s)

        val s = testDataLens.asString(TestData(ApplicationID("my-id"), "my-test"))

        val handler = DebuggingFilters.PrintRequest()
            .then(ServerFilters.CatchLensFailure {
                Response(Status.BAD_REQUEST).body(it.message + "\n" + it.cause.toString())
            })
            .then { req: Request ->
                println(testDataLens.extract(req))
//                println(jobInfoLens.extract(req))
                Response(Status.OK)
            }


//        val input = """
//            {
//              "id" : "job1",
//              "applicationID" : "app1",
//              "description" : {
//                "image" : "image1",
//                "description" : "One-shot test job"
//              },
//              "metrics" : {
//                "newAt" : "2022-08-26T13:56:54.730Z",
//                "readyAt" : "2022-08-26T13:56:54.730007Z"
//              },
//              "state" : "Ready"
//            }
//        """.trimIndent()
        println(handler(Request(Method.GET, "").body(s)))
//        val result = jobInfoLens.extract(Response(Status.OK).body(Body(input)))
//        val result = jobInfoLens.extract(Response(Status.OK).body(Body(s)))

//        println(result)

//        val s = jobInfoLens to JobInfo(
//            JobID("job1"),
//            ApplicationID("app1"),
//            OneShotTestJobDescription(image = "image1"),
//            JobMetrics(Instant.now(), readyAt = Instant.now()),
//            JobState.Ready,
//        )

    }

    /**
     * Returns a string representation of the specified object using this lens.
     *
     * @param obj the object to be converted to a string
     * @return the string representation
     */
    private fun <T> BiDiBodyLens<T>.asString(obj: T): String {
        // I'm not sure how to do this other than creating a request,
        // using the lens to build the body,
        // and then getting the string representation of said body.
        return Request(Method.GET, "")
            .with(this of obj)
            .body.payload.asString()
    }





}