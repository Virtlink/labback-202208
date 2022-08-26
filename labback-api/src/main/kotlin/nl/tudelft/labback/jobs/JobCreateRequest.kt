package nl.tudelft.labback.jobs

/** Request data for the Jobs API, which creates a new job. */
data class JobCreateRequest(
    /** The job ID. */
    val id: JobID,
    /** The ID of the prototype from which to get the base job description values. */
    val prototype: String?,
    /** The job description of the job to create. */
    val description: JobDescription,
)