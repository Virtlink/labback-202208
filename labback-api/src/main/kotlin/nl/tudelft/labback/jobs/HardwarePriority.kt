package nl.tudelft.labback.jobs

/**
 * Specifies the priority of a job's allocated hardware resources.
 */
enum class HardwarePriority {
    /** The job has low priority on the system's allocated hardware resources. */
    Low,
    /** The job has normal priority on the system's allocated hardware resources. */
    Normal,
    /** The job has high priority on the system's allocated hardware resources. */
    High,
}