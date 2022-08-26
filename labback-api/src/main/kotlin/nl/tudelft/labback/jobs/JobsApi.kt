package nl.tudelft.labback.jobs

import kotlin.time.Duration


/**
 * Implements the Jobs API.
 */
interface JobsApi {

    /**
     * Gets a list of known jobs.
     *
     * @return a list of jobs
     */
    suspend fun getAll(): List<JobInfo>

    /**
     * Creates a new job and returns its data.
     *
     * The final job description is built from the prototype job description,
     * overridden with the specified job description.
     *
     * When this method returns, the job has been created.
     *
     * @param id the ID of the job to create
     * @param prototypeName the name of the job prototype to use; or `null` to use none
     * @param description the new job's description
     * @return the created job
     * @throws JobExistsException if a job with this ID already exists
     * @throws PrototypeNotFoundException if the specified prototype does not exist
     * @throws PrototypeInvalidException if the specified prototype is not valid for this job
     */
//    @Throws(JobExistsException::class, PrototypeNotFoundException::class, PrototypeInvalidException::class)
    suspend fun create(id: JobID, prototypeName: String?, description: JobDescription): JobInfo

    /**
     * Gets the job with the specified ID, or returns `null` if not found.
     *
     * @param id the ID of the job to find
     * @return the job with the specified ID, or `null` if not found
     */
    suspend fun get(id: JobID): JobInfo?

    /**
     * Schedules the job with the specified ID.
     *
     * This adds the job to the queue of jobs that are ready to be run (marking it ready).
     * Eventually, it will be picked up by an available runner.
     *
     * When this method returns, the job has been scheduled for execution.
     *
     * @param id the ID of the job to schedule
     * @return the job with the specified ID
     * @throws JobNotFoundException if the job with the specified ID is not found
     */
//    @Throws(JobNotFoundException::class)
    suspend fun schedule(id: JobID): JobInfo

    /**
     * Cancels or kills the job with the specified ID.
     *
     * This cancels the job if it is not yet running; otherwise, kills the job if it is running.
     * If the job has already finished, this method does nothing.
     *
     * When this method returns, the job has been cancelled.
     *
     * @param id the ID of the job to cancel
     * @return the job with the specified ID
     * @throws JobNotFoundException if the job with the specified ID is not found
     */
//    @Throws(JobNotFoundException::class)
    suspend fun cancel(id: JobID): JobInfo

    /**
     * Deletes and disposes the job with the specified ID.
     *
     * This cancels or kills the job, deletes the job's data and associated resources,
     * and removes the job from this Labback instance. Use this method to indicate
     * that the caller is no longer interested in the job's result.
     *
     * When this method returns, the job has been deleted and disposed and is no longer known.
     * @return `true` when the job was found, deleted, and disposed;
     * otherwise, `false` when the job is not found
     */
    suspend fun dispose(id: JobID): Boolean

    /**
     * Waits for the job with the specified ID to get to a specific state or a later state.
     *
     * @param id the ID of the job to wait for
     * @param state the minimum state to wait for
     * @param timeout the maximum time to wait for the job to reach the state
     * @return the job with the specified ID once it has reached at least the specified state;
     * or `null` when the timeout was triggered
     * @throws JobNotFoundException if the job with the specified ID is not found
     */
//    @Throws(JobNotFoundException::class)
    suspend fun await(id: JobID, state: JobState, timeout: Duration = Duration.INFINITE): JobInfo?

}

