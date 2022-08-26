package nl.tudelft.labback.application

import nl.tudelft.labback.utils.isValidId
import nl.tudelft.labback.utils.toId

/**
 * An application ID.
 *
 * The application ID is used to uniquely identify this instance of the application.
 * It is associated with the Docker containers that are created by this instance,
 * such that this instance will only manage its own containers and not accidentally
 * interfere with the container managed by another instance.
 *
 * Upon startup, the application checks to ensure that only one instance of the application
 * runs with the given application ID.
 *
 * @property value The raw ID.
 */
@JvmInline
value class ApplicationID(val value: String) {

    companion object {
        /**
         * Creates an application ID from the specified string.
         *
         * @param str the string
         * @return the application ID
         */
        fun fromString(str: String): ApplicationID {
            return ApplicationID(str.toId())
        }
    }

    init {
        require(value.isValidId()) { "The application ID is invalid." }
    }

    @Deprecated("Use the `value` field instead of toString(), to avoid boxing.", ReplaceWith("value"))
    override fun toString(): String = value

}