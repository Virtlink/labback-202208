package nl.tudelft.labback.jobs

import nl.tudelft.labback.utils.isValidId
import nl.tudelft.labback.utils.toId

/**
 * A job identifier.
 */
@JvmInline
value class JobID(val value: String) {

    companion object {
        /**
         * Creates a task ID from the specified string.
         *
         * @param str the string
         * @return the task ID
         */
        fun fromString(str: String): JobID {
            require(str.isNotBlank()) { "Job ID cannot be empty" }

            return JobID(str.toId())
        }
    }

    init {
        require(value.isValidId()) { "The job ID is invalid." }
    }

    @Deprecated("Use the `value` field instead of toString(), to avoid boxing.", ReplaceWith("value"))
    override fun toString(): String = value

}