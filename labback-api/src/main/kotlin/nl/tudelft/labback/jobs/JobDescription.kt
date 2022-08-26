package nl.tudelft.labback.jobs

import nl.tudelft.labback.utils.ByteSize
import nl.tudelft.labback.utils.Cmd
import nl.tudelft.labback.config.HardwareConstraints
import kotlin.time.Duration

/**
 * A job description.
 */
sealed interface JobDescription {

    /** The Docker image; or `null` if not set. */
    val image: String?

    /** The name of a runner group that is allowed to run this job; or `null` if not set. */
    val runnerGroup: String?
    /** The maximum time the job may be scheduled before it is cancelled; or `null` if not set. */
    val expireAfter: Duration?
    /** The maximum time this job may be running before it is killed; or `null` if not set. */
    val timeoutAfter: Duration?
    /** The maximum time this job may be finished before it is disposed; or `null` if not set. */
    val disposeAfter: Duration?

    /**
     * The priority of the resources for this job; or `null` if not set.
     *
     * This is used to assign a job a greater or smaller part of available resources
     * on a Docker runner when there is contention.
     *
     * This is not used in scheduling the job.
     */
    val hardwarePriority: HardwarePriority?
    /**
     * Any resource limits that are specific to this job.
     *
     * If specified, any limits of the runner that are more strict take precedence.
     */
    val hardwareConstraints: HardwareConstraints

    /** Any environment variables to set in the container; or `null` if not set. */
    val environment: Map<String, String>?
    /** The command to execute; or `null` to use the Dockerfile command. */
    val command: Cmd?

    /** The path in the container to where the input files are copied; or `null` if not set. */
    val containerInputDirectory: String?
    /** The path in the container from where the output files are copied; or `null` if not set. */
    val containerOutputDirectory: String?

    /** The maximum file size of files extracted from the container; or `null` to impose no limit. */
    val outputFileSizeLimit: ByteSize?
    /** The maximum number of entries to extract from the container; or `null` to impose no limit. */
    val outputEntryCountLimit: Int?
    /** Whether to ignore the output streams for the final output (and display the output files instead); or `null` if not set. */
    val reportOutputFiles: Boolean?
    /** The maximum captured output stream size; or `null` to impose no limit. */
    val outputStreamLimit: ByteSize?
    /** Whether to filter escape codes from the captured output streams; or `null` if not set. */
    val outputFilterEscapes: Boolean?
    /** The user ID to assign to files copied to the container; or `null` to not change the user ID. */
    val userId: Long?
    /** The group ID to assign to files copied to the container; or `null` to not change the group ID. */
    val groupId: Long?
    /** Renamed files. This can also be used to move files to a different directory; or `null` if not set. */
    val renamedFiles: Map<String, String>?

    /** A short sentence describing the job, used in debugging and logging. */
    val description: String

}
