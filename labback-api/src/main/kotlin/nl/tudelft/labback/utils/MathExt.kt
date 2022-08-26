@file:Suppress("NOTHING_TO_INLINE")

package nl.tudelft.labback.utils

/**
 * Returns the minimum of two values, or the value that is not `null`, or `null`.
 *
 * @param a the first value; or `null`
 * @param b the second value; or `null`
 * @return the minimum of the two, or the value that is not `null`, or `null`
 */
inline fun minOrNull(a: Int?, b: Int?): Int? =
    if (a == null) b else if (b == null) a else kotlin.math.min(a, b)

/**
 * Returns the minimum of two values, or the value that is not `null`, or `null`.
 *
 * @param a the first value; or `null`
 * @param b the second value; or `null`
 * @return the minimum of the two, or the value that is not `null`, or `null`
 */
inline fun minOrNull(a: Long?, b: Long?): Long? =
    if (a == null) b else if (b == null) a else kotlin.math.min(a, b)

/**
 * Returns the minimum of two values, or the value that is not `null`, or `null`.
 *
 * @param a the first value; or `null`
 * @param b the second value; or `null`
 * @return the minimum of the two, or the value that is not `null`, or `null`
 */
inline fun minOrNull(a: Float?, b: Float?): Float? =
    if (a == null) b else if (b == null) a else kotlin.math.min(a, b)

/**
 * Returns the minimum of two values, or the value that is not `null`, or `null`.
 *
 * @param a the first value; or `null`
 * @param b the second value; or `null`
 * @return the minimum of the two, or the value that is not `null`, or `null`
 */
inline fun minOrNull(a: Double?, b: Double?): Double? =
    if (a == null) b else if (b == null) a else kotlin.math.min(a, b)

/**
 * Returns the minimum of two values, or the value that is not `null`, or `null`.
 *
 * @param a the first value; or `null`
 * @param b the second value; or `null`
 * @return the minimum of the two, or the value that is not `null`, or `null`
 */
inline fun <T : Comparable<T>> minOrNull(a: T?, b: T?): T? =
    if (a == null) b else if (b == null) a else if (a <= b) a else b

/**
 * Returns the logical-or of two values, or the value that is not `null`, or `null`.
 *
 * @param a the first value; or `null`
 * @param b the second value; or `null`
 * @return the logical-or of the two, or the value that is not `null`, or `null`
 */
inline fun logicalOrOrNull(a: Boolean?, b: Boolean?): Boolean? =
    if (a == null) b else if (b == null) a else a || b

/**
 * Returns the logical-and of two values, or the value that is not `null`, or `null`.
 *
 * @param a the first value; or `null`
 * @param b the second value; or `null`
 * @return the logical-and of the two, or the value that is not `null`, or `null`
 */
inline fun logicalAndOrNull(a: Boolean?, b: Boolean?): Boolean? =
    if (a == null) b else if (b == null) a else a && b
