package nl.tudelft.labback.api

import nl.tudelft.labback.jobs.JobID
import java.time.Instant

/** An API exception. */
sealed class ApiException(
    /** The HTTP message. */
    message: String? = null,
    /** The cause of this exception, if any. */
    cause: Throwable? = null,
    /** The path where the error occurred (without the domain name), if known. */
    val path: String? = null,
    /** When the exception occurred. */
    val timestamp: Instant = Instant.now(),
) : Exception(message ?: "An API exception occurred.", cause)

/** The job with the specified ID was not found. */
class JobNotFoundException(
    /** The ID of the job that was not found. */
    val id: JobID,
    /** The message, if any. */
    message: String? = null,
    /** The cause of this exception, if any. */
    cause: Throwable? = null,
    /** The path where the error occurred (without the domain name), if known. */
    path: String? = null,
    /** When the exception occurred. */
    timestamp: Instant = Instant.now(),
): ApiException(message ?: "Job not found: $id", cause, path, timestamp)

/** The job with the specified ID already exists. */
class JobExistsException(
    /** The ID of the job that already exists. */
    val id: JobID,
    /** The message, if any. */
    message: String? = null,
    /** The cause of this exception, if any. */
    cause: Throwable? = null,
    /** The path where the error occurred (without the domain name), if known. */
    path: String? = null,
    /** When the exception occurred. */
    timestamp: Instant = Instant.now(),
): ApiException(message ?: "Job already exists: $id", cause, path, timestamp)
