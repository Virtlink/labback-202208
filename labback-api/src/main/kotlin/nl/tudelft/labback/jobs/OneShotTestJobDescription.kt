package nl.tudelft.labback.jobs

import nl.tudelft.labback.*
import nl.tudelft.labback.utils.ByteSize.Companion.mebibyte
import nl.tudelft.labback.config.HardwareConstraints
import nl.tudelft.labback.utils.ByteSize
import nl.tudelft.labback.utils.Cmd
import nl.tudelft.labback.utils.or
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Job description for the [OneShotTestJob].
 */
data class OneShotTestJobDescription(
    override val image: String? = null,

    override val runnerGroup: String? = null,
    override val expireAfter: Duration? = null,
    override val timeoutAfter: Duration? = null,
    override val disposeAfter: Duration? = null,

    override val hardwarePriority: HardwarePriority? = null,
    override val hardwareConstraints: HardwareConstraints = HardwareConstraints(),

    override val environment: Map<String, String> = emptyMap(),
    override val command: Cmd? = null,

    override val containerInputDirectory: String? = null,
    override val containerOutputDirectory: String? = null,

    override val outputFileSizeLimit: ByteSize? = null,
    override val outputEntryCountLimit: Int? = null,
    override val reportOutputFiles: Boolean? = null,
    override val outputStreamLimit: ByteSize? = null,
    override val outputFilterEscapes: Boolean? = null,
    override val userId: Long? = null,
    override val groupId: Long? = null,
    override val renamedFiles: Map<String, String> = emptyMap(),

    /** The deploy base directory; or `null` if not specified. */
    val deployBaseDirectory: Path? = null,
    /** The deploy base URL; or `null` if not specified. */
    val deployBaseUrl: String? = null,
) : JobDescription {

    init {
        require(image == null || image.isNotBlank()) { "The image name may not be blank." }
    }

    companion object {
        /** The default values for this type of job description. */
        val default = OneShotTestJobDescription(
            image = null,

            runnerGroup = null,
            expireAfter = 30.seconds,
            timeoutAfter = 60.seconds,
            disposeAfter = 120.seconds,

            hardwarePriority = HardwarePriority.Normal,
            hardwareConstraints = HardwareConstraints(),

            environment = emptyMap(),
            command = null,

            containerInputDirectory = "/home/student/",
            containerOutputDirectory = "/home/student/output/",

            outputFileSizeLimit = null,
            outputEntryCountLimit = null,
            reportOutputFiles = false,
            outputStreamLimit = 1.mebibyte(),
            userId = 1000,
            groupId = 1000,
            renamedFiles = emptyMap(),
        )
    }

    /**
     * Takes values from this job description, or from the specified job description
     * if the value is not set in this job description.
     *
     * @param base the base job description
     * @return the resulting job description
     */
    infix fun or(base: OneShotTestJobDescription): OneShotTestJobDescription = copy(
        image = this.image ?: base.image,

        runnerGroup = this.runnerGroup ?: base.runnerGroup,
        expireAfter = this.expireAfter ?: base.expireAfter,
        timeoutAfter = this.timeoutAfter ?: base.timeoutAfter,

        hardwarePriority = this.hardwarePriority ?: base.hardwarePriority,
        hardwareConstraints = this.hardwareConstraints or base.hardwareConstraints,

        environment = this.environment or base.environment,
        command = this.command ?: base.command,

        containerInputDirectory = this.containerInputDirectory ?: base.containerInputDirectory,
        containerOutputDirectory = this.containerOutputDirectory ?: base.containerOutputDirectory,

        outputFileSizeLimit = this.outputFileSizeLimit ?: base.outputFileSizeLimit,
        outputEntryCountLimit = this.outputEntryCountLimit ?: base.outputEntryCountLimit,
        reportOutputFiles = this.reportOutputFiles ?: base.reportOutputFiles,
        outputStreamLimit = this.outputStreamLimit ?: base.outputStreamLimit,
        userId = this.userId ?: base.userId,
        groupId = this.groupId ?: base.groupId,
        renamedFiles = this.renamedFiles or base.renamedFiles,
    )

    override val description: String
        get() = "One-shot test job"

}
