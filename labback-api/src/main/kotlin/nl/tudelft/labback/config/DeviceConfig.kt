package nl.tudelft.labback.config

import nl.tudelft.labback.utils.minOrNull

/**
 * Configuration for block device IO.
 *
 * If both a maximum byte rate and IO rate are specified, then IO is subjected to both constraints.
 */
data class DeviceConfig(
    /** The device relative weight (between 10 and 1000); or `null` if not set. */
    val relativeWeight: Int? = null,
    /** The device maximum read rate, in bytes per second; or `null` if not set. */
    val maxReadByteRate: Long? = null,
    /** The device maximum write rate, in bytes per second; or `null` if not set. */
    val maxWriteByteRate: Long? = null,
    /** The device maximum read rate, in IO operations per second; or `null` if not set. */
    val maxReadIoRate: Long? = null,
    /** The device maximum write rate, in IO operations per second; or `null` if not set. */
    val maxWriteIoRate: Long? = null,
) {

    init {
        require(relativeWeight == null || relativeWeight in 10..1000) { "${::relativeWeight.name} must be between 10 and 1000." }

        require(maxReadByteRate == null || maxReadByteRate >= 0) { "${::maxReadByteRate.name} must be positive or zero." }
        require(maxWriteByteRate == null || maxWriteByteRate >= 0) { "${::maxWriteByteRate.name} must be positive or zero." }

        require(maxReadIoRate == null || maxReadIoRate >= 0) { "${::maxReadIoRate.name} must be positive or zero." }
        require(maxWriteIoRate == null || maxWriteIoRate >= 0) { "${::maxWriteIoRate.name} must be positive or zero." }
    }

    /**
     * Returns a configuration based on this configuration or the specified
     * configuration when a value in this configuration is not set.
     *
     * @param baseConfig the base configuration from which to get values not set in this configuration
     *
     * @return the resulting configuration
     */
    infix fun or(baseConfig: DeviceConfig): DeviceConfig =
        DeviceConfig(
            relativeWeight ?: baseConfig.relativeWeight,
            maxReadByteRate ?: baseConfig.maxReadByteRate,
            maxWriteByteRate ?: baseConfig.maxWriteByteRate,
            maxReadIoRate ?: baseConfig.maxReadIoRate,
            maxWriteIoRate ?: baseConfig.maxWriteIoRate,
        )

    /**
     * Returns a configuration based on this configuration
     * where the values are at least as restrictive as the specified configuration.
     *
     * @param otherConfig the configuration that determines the restrictions on this configuration
     * @return the resulting configuration
     */
    infix fun max(otherConfig: DeviceConfig): DeviceConfig =
        DeviceConfig(
            minOrNull(relativeWeight, otherConfig.relativeWeight),
            minOrNull(maxReadByteRate, otherConfig.maxReadByteRate),
            minOrNull(maxWriteByteRate, otherConfig.maxWriteByteRate),
            minOrNull(maxReadIoRate, otherConfig.maxReadIoRate),
            minOrNull(maxWriteIoRate, otherConfig.maxWriteIoRate),
        )

    /**
     * Gets a summary of this object and its child objects.
     */
    fun getSummary(): Map<String, Any?> = mapOf(
        "relativeWeight" to relativeWeight,
        "maxReadByteRate" to maxReadByteRate,
        "maxWriteByteRate" to maxWriteByteRate,
        "maxReadIoRate" to maxReadIoRate,
        "maxWriteIoRate" to maxWriteIoRate,
    )
}
