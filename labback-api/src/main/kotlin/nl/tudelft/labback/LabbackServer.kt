package nl.tudelft.labback

/**
 * A server that handles API requests.
 *
 * The server decodes and handles the requests that are received on the specified port,
 * and encodes and sends the response. The routes are declared as objects in the respective
 * routes files of an API, such as `JobsApiJavalinServer` for the Jobs API, but the actual handling
 * logic is in this class.
 *
 * Usage: instantiate this class and call [start] to start the server. Call [stop] to stop the server, or wrap the
 * using code in a `server.use { }` block to automatically stop the server when the block is exited.
 */
interface LabbackServer: AutoCloseable {

    /**
     * Starts the server.
     *
     * @param wait whether to block this thread until the engine is closed from another thread
     */
    fun start(wait: Boolean = false)

    /**
     * Stops the server.
     */
    fun stop()

    override fun close() {
        stop()
    }

    /**
     * Gets the port the server is listening on.
     *
     * This will return the chosen port when the server was started with port 0.
     * Note: this might throw an exception or block if the embedded server has not yet been started.
     *
     * @return the port on which the server is listening
     */
    val port: Int

}
