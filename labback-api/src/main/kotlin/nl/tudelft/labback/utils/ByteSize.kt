package nl.tudelft.labback.utils

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.pow

/**
 * Represents a size in bytes.
 *
 * @property size The size, in bytes.
 */
@JvmInline
value class ByteSize(val size: Long): Comparable<ByteSize> {

    constructor(size: Int): this(size.toLong())

    companion object {
        private const val Kilo = 1000L
        private const val Mega = 1000000L
        private const val Giga = 1000000000L
        private const val Tera = 1000000000000L
        private const val Peta = 1000000000000000L

        private const val Kibi = 1L shl 10
        private const val Mebi = 1L shl 20
        private const val Gibi = 1L shl 30
        private const val Tebi = 1L shl 40
        private const val Pebi = 1L shl 50

        private val units: Map<String, Long> = mapOf(
            // Shortcuts
            "" to 1,
            "byte" to 1,
            "bytes" to 1,

            "b" to 1,           // Supported in Docker CLI
            "k" to Kibi,        // Supported in Docker CLI
            "m" to Mebi,        // Supported in Docker CLI
            "g" to Gibi,        // Supported in Docker CLI

            // Decimal sizes
            "kb" to Kilo,
            "mb" to Mega,
            "gb" to Giga,
            "tb" to Tera,
            "pb" to Peta,

            // Binary sizes
            "kib" to Kibi,
            "mib" to Mebi,
            "gib" to Gibi,
            "tib" to Tebi,
            "pib" to Pebi,
        )

        fun Int.byte() = ByteSize(this)

        fun Int.kilobyte() = this.toLong().kilobyte()
        fun Int.megabyte() = this.toLong().megabyte()
        fun Int.gigabyte() = this.toLong().gigabyte()
        fun Int.terabyte() = this.toLong().terabyte()
        fun Int.petabyte() = this.toLong().petabyte()

        fun Int.kibibyte() = this.toLong().kibibyte()
        fun Int.mebibyte() = this.toLong().mebibyte()
        fun Int.gibibyte() = this.toLong().gibibyte()
        fun Int.tebibyte() = this.toLong().tebibyte()
        fun Int.pebibyte() = this.toLong().pebibyte()

        fun Long.byte() = ByteSize(this)

        fun Long.kilobyte() = ByteSize(Math.multiplyExact(this, Kilo))
        fun Long.megabyte() = ByteSize(Math.multiplyExact(this, Mega))
        fun Long.gigabyte() = ByteSize(Math.multiplyExact(this, Giga))
        fun Long.terabyte() = ByteSize(Math.multiplyExact(this, Tera))
        fun Long.petabyte() = ByteSize(Math.multiplyExact(this, Peta))

        fun Long.kibibyte() = ByteSize(Math.multiplyExact(this, Kibi))
        fun Long.mebibyte() = ByteSize(Math.multiplyExact(this, Mebi))
        fun Long.gibibyte() = ByteSize(Math.multiplyExact(this, Gibi))
        fun Long.tebibyte() = ByteSize(Math.multiplyExact(this, Tebi))
        fun Long.pebibyte() = ByteSize(Math.multiplyExact(this, Pebi))

        /**
         * Attempts to parse the given input into a byte size.
         *
         * The input is written in the form `42 GiB`, `2MB`, or `42` (bytes).
         *
         * @param input the input to parse
         * @return the parsed [ByteSize]; or `null` when parsing failed
         */
        fun tryParse(input: String): ByteSize? {
            return try {
                parse(input)
            } catch (ex: IllegalArgumentException) {
                null
            }
        }

        /**
         * Parses the given input into a byte size.
         *
         * The input is written in the form `42 GiB`, `2MB`, or `42` (bytes).
         *
         * @param input the input to parse
         * @return the parsed [ByteSize]; or `null` when parsing failed
         */
        fun parse(input: String): ByteSize {
            val trimmedInput = input.trim()
            val (sizeStr, symbol) = "(\\d+)(.*)".toRegex().matchEntire(trimmedInput)?.destructured ?: throw IllegalArgumentException("Cannot parse byte size: $trimmedInput")
            val unit = symbol.trim().lowercase()
            val multiplier = units[unit] ?: throw IllegalArgumentException("Unknown or unsupported byte unit '$unit' in value: $input")
            val size = sizeStr.toLongOrNull() ?: throw IllegalArgumentException("Invalid number '$sizeStr' in value: $input")
            return ByteSize(size * multiplier)
        }
    }

    init {
        require(size >= 0L) { "The value must be greater than or equal to zero." }
    }

    /** Whether this size is zero. */
    val isZero get() = size == 0L

    /** Convert this size to a number of kilobytes, rounded toward zero. */
    fun toKilobyte() = size / Kilo
    /** Convert this size to a number of megabytes, rounded toward zero. */
    fun toMegabyte() = size / Mega
    /** Convert this size to a number of gigabytes, rounded toward zero. */
    fun toGigabyte() = size / Giga
    /** Convert this size to a number of terabytes, rounded toward zero. */
    fun toTerabyte() = size / Tera
    /** Convert this size to a number of petabytes, rounded toward zero. */
    fun toPetabyte() = size / Peta

    /** Convert this size to a number of kibibytes, rounded toward zero. */
    fun toKibibyte() = size / Kibi
    /** Convert this size to a number of mebibytes, rounded toward zero. */
    fun toMebibyte() = size / Mebi
    /** Convert this size to a number of gibibytes, rounded toward zero. */
    fun toGibibyte() = size / Gibi
    /** Convert this size to a number of tebibytes, rounded toward zero. */
    fun toTebibyte() = size / Tebi
    /** Convert this size to a number of pebibytes, rounded toward zero. */
    fun toPebibyte() = size / Pebi

    operator fun plus(that: ByteSize): ByteSize = ByteSize(this.size + that.size)
    operator fun plus(that: Int): ByteSize = ByteSize(this.size + that)
    operator fun plus(that: Long): ByteSize = ByteSize(this.size + that)

    operator fun minus(that: ByteSize): ByteSize = ByteSize(this.size - that.size)
    operator fun minus(that: Int): ByteSize = ByteSize(this.size - that)
    operator fun minus(that: Long): ByteSize = ByteSize(this.size - that)

    operator fun times(that: Int): ByteSize = ByteSize(this.size * that)

    operator fun div(that: Int): ByteSize = ByteSize(this.size / that)

    override operator fun compareTo(other: ByteSize): Int = this.size.compareTo(other.size)
    operator fun compareTo(other: Int): Int = this.size.compareTo(other.toLong())
    operator fun compareTo(other: Long): Int = this.size.compareTo(other)

    /**
     * Returns a human-readable representation of a number of bytes as kilobytes, megabytes, etc.
     *
     * This is adapted from the world's most copied Stackoverflow snippet,
     * by Andreas Lundblad at <https://programming.guide/worlds-most-copied-so-snippet.html>
     *
     * @param si `true` to format using the SI-standard unit (where 1 MB = 1000 * 1000 bytes);
     * otherwise, `false` to format using the binary unit (where 1 MiB = 1024 * 1024 bytes).
     * @return the (hopefully) human-readable string representation of the value
     */
    @Strictfp
    fun toString(si: Boolean): String {
        var bytes = this.size
        val unit = if (si) 1000 else 1024
        val absBytes = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
        if (absBytes < unit) return "$bytes bytes"
        var exp = (ln(absBytes.toDouble()) / ln(unit.toDouble())).toInt()
        val th = ceil(unit.toDouble().pow(exp.toDouble()) * (unit - 0.05)).toLong()
        if (exp < 6 && absBytes >= th - (if (th and 0xFFF == 0xD00L) 51 else 0)) exp++
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + if (si) "" else "i"
        if (exp > 4) {
            bytes /= unit.toLong()
            exp -= 1
        }
        return String.format("%.1f %sB", bytes / unit.toDouble().pow(exp.toDouble()), pre)
    }

    override fun toString(): String = toString(false)

}
