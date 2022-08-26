package nl.tudelft.labback

import nl.tudelft.labback.jobs.JobsApi

/**
 * The Labback client interface.
 *
 * All interactions with a Labback instance happen through this interface.
 * The actual Labback instance might directly implement this interface,
 * or be on a completely different system using some communication protocol
 * to transport requests and responses.
 *
 * Upon creating a Labback client, it is automatically active.
 * Call [close] when done with the Labback client, to let it release its resources and perform cleanup.
 * Closing a Labback client does not necessarily close the Labback instance it is connected to.
 * Instead, call [ApplicationApi.shutdown] to stop the actual Labback instance.
 */
interface LabbackClient: AutoCloseable {

//    /** The Application API. */
//    val application: ApplicationApi
//    /** The Runners API. */
//    val runners: RunnersApi
    /** The Jobs API. */
    val jobs: JobsApi
//    /** The Prototypes API. */
//    val prototypes: PrototypesApi
//    /** The Resources API. */
//    val resources: ResourcesApi
//    /** The Images API. */
//    val images: ImagesApi
//    /** The Registries API. */
//    val registries: RegistriesApi
//    /** The Metrics API. */
//    val metrics: MetricsApi

}