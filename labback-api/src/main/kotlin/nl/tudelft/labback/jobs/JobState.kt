package nl.tudelft.labback.jobs

/**
 * The state of a job.
 */
enum class JobState {
    // NOTE: The order is important.
    // A job can go through these states from top to bottom,
    // but a job can never go to an earlier state from a later one.

    /** The job is being created. */
    New,
    /** The job is waiting to be executed. */
    Ready,
    /** The job is running on a runner. */
    Running,
    /** The job has finished, either successfully or failed. */
    Finished,
    /** The job has been disposed. */
    Disposed,
}
