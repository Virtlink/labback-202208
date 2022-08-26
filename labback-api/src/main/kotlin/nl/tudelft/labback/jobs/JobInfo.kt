package nl.tudelft.labback.jobs

import nl.tudelft.labback.application.ApplicationID

/** Response data for the Jobs API, which gets info on an existing job. */
data class JobInfo(
    /** The job ID. */
    val id: JobID,
    /** The application ID of the application that is running this job. */
    val applicationID: ApplicationID,
    /** The job description. */
    val description: JobDescription,
    /** The job metrics. */
    val metrics: JobMetrics,
    /** The current state of the job. */
    val state: JobState,
    /** The error message resulting from the job, if any. */
    val errorMessage: String? = null,
    /** The kind of job result, if any. */
    val result: JobResultKind? = null,
)