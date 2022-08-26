package nl.tudelft.labback.config

import nl.tudelft.labback.utils.ByteSize
import nl.tudelft.labback.utils.logicalAndOrNull
import nl.tudelft.labback.utils.merge
import nl.tudelft.labback.utils.minOrNull


/**
 * A job's allocated system hardware constraints.
 */
data class HardwareConstraints(
    /** The maximum number of PIDs per job; or `null` if not specified. */
    val pidLimit: Long? = null,
    /** The maximum number of CPUs per job; or `null` if not specified. */
    val cpuLimit: Double? = null,
    /** The hard memory limit per job; or `null` if not specified. */
    val memoryLimit: ByteSize? = null,
    /** The soft memory limit per job; or `null` if not specified. */
    val softMemoryLimit: ByteSize? = null,
    /** The disk space limit per job; or `null` if not specified. */
    val diskSpaceLimit: ByteSize? = null,
    /** The device limits per job. */
    val devices: Map<String, DeviceConfig> = emptyMap(),
    /** Whether to allow network access; or `null` if not specified. */
    val hasNetwork: Boolean? = null,
) {

    init {
        require(pidLimit == null || pidLimit >= 0) { "${::pidLimit.name} must be positive or zero." }
        require(cpuLimit == null || cpuLimit >= 0.0) { "${::cpuLimit.name} must be positive or zero." }
        require(memoryLimit == null || memoryLimit >= 0) { "${::memoryLimit.name} must be positive or zero." }
        require(softMemoryLimit == null || softMemoryLimit >= 0) { "${::softMemoryLimit.name} must be positive or zero." }
        require(diskSpaceLimit == null || diskSpaceLimit >= 0) { "${::diskSpaceLimit.name} must be positive or zero." }

        if (softMemoryLimit != null) {
            requireNotNull(memoryLimit) { "${::memoryLimit.name} must be specified when ${::softMemoryLimit.name} is specified." }
            require(memoryLimit >= softMemoryLimit) { "${::memoryLimit.name} ($memoryLimit) must be greater than or equal to ${::softMemoryLimit.name} ($softMemoryLimit)." }
        }

        require(devices.keys.none { it.isBlank() }) { "${::devices.name} paths must not be blank or empty." }
    }

    /**
     * Returns a configuration based on this configuration or the specified
     * configuration when a value in this configuration is not set.
     *
     * @param baseConfig the base configuration from which to get values not set in this configuration
     * @return the resulting configuration
     */
    infix fun or(baseConfig: HardwareConstraints): HardwareConstraints =
        HardwareConstraints(
            pidLimit ?: baseConfig.pidLimit,
            cpuLimit ?: baseConfig.cpuLimit,
            memoryLimit ?: baseConfig.memoryLimit,
            softMemoryLimit ?: baseConfig.softMemoryLimit,
            diskSpaceLimit ?: baseConfig.diskSpaceLimit,
            devices.merge(baseConfig.devices) { a, b -> a.or(b) },
            hasNetwork ?: baseConfig.hasNetwork,
        )

    /**
     * Returns a configuration based on this configuration
     * where the values are at least as restrictive as the specified configuration.
     *
     * @param maxConfig the configuration that determines the restrictions on this configuration
     * @return the resulting configuration
     */
    infix fun max(maxConfig: HardwareConstraints): HardwareConstraints =
        HardwareConstraints(
            minOrNull(pidLimit, maxConfig.pidLimit),
            minOrNull(cpuLimit, maxConfig.cpuLimit),
            minOrNull(memoryLimit, maxConfig.memoryLimit),
            minOrNull(softMemoryLimit, maxConfig.softMemoryLimit),
            minOrNull(diskSpaceLimit, maxConfig.diskSpaceLimit),
            devices.merge(maxConfig.devices) { a, b -> a.max(b) },
            logicalAndOrNull(hasNetwork, maxConfig.hasNetwork),
        )

    /**
     * Gets a summary of this object and its child objects.
     */
    fun getSummary(): Map<String, Any?> = mapOf(
        "pidLimit" to pidLimit,
        "cpuLimit" to cpuLimit,
        "memoryLimit" to memoryLimit,
        "softMemoryLimit" to softMemoryLimit,
        "diskSpaceLimit" to diskSpaceLimit,
        "devices" to devices.mapValues { it.value.getSummary() },
        "hasNetwork" to hasNetwork,
    )
}
