package nl.tudelft.labback.jobs

import java.time.Instant

/** Metrics on a job. */
data class JobMetrics(
    /** When the job was created. */
    val newAt: Instant,
    /** When the job was scheduled; or `null` when the job is not yet ready. */
    val readyAt: Instant? = null,
    /** When the job was started; or `null` when the job is not yet running. */
    val runningAt: Instant? = null,
    /** When the job was finished (completed or failed); or `null` when the job is not yet finished. */
    val finishedAt: Instant? = null,
    /** When the job was disposed; or `null` when the job is not yet disposed. */
    val disposedAt: Instant? = null,

    /** When the job will expire; or [Instant.MAX] when it will never expire; or `null` when expiration is not yet known. */
    val willExpireAt: Instant? = null,
    /** When the job will time-out; or [Instant.MAX] when it will never time-out; or `null` when the time-out is not yet known. */
    val willTimeoutAt: Instant? = null,
    /** When the job will be disposed; or [Instant.MAX] when ti will never be disposed; or `null` when the disposal time is not yet known. */
    val willDisposeAt: Instant? = null,
)