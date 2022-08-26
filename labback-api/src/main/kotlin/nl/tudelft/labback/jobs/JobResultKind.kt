package nl.tudelft.labback.jobs

/**
 * The kind of job result.
 */
enum class JobResultKind {
    /** The job completed. */
    Completed,
    /** The job completed but detected tampering. */
    Tampered,
    /** The job was cancelled. */
    Cancelled,
    /** The job was killed. */
    Killed,
    /** The job died (e.g., due to out-of-memory). */
    Died,
    /** The job did not run before its expiration deadline. */
    Expired,
    /** The job did not finish before its time-out deadline. */
    TimedOut,
    /** The job failed due to a Labback error. */
    Errored;
}