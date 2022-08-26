package nl.tudelft.labback.utils

/**
 * Escapes the string.
 *
 * This is used for debugging.
 *
 * @param escapeQuotes whether to escape single and double quotes
 * @return the escaped string; or `null` if the input string was `null`
 */
fun String?.escape(escapeQuotes: Boolean = true): String? {
    if (this == null) return null
    return StringBuilder().apply {
        this@escape.forEach { c ->
            when {
                c == '\"' && escapeQuotes -> append("\\\"")
                c == '\'' && escapeQuotes -> append("\\\'")
                c == '\\' -> append("\\\\")
                c == '\t' -> append("\\t")
                c == '\b' -> append("\\b")
                c == '\r' -> append("\\r")
                c == '\n' -> append("\\n")
                c.isISOControl() -> append("\\x${"%02x".format(c.code)}")
                else -> append(c)
            }
        }
    }.toString()
}

/**
 * Turns an arbitrary string into a valid lowercase identifier.
 *
 * A valid identifier consists of only alphanumeric characters and dashes.
 * The returned identifier will not start or end with a dash or have consecutive dashes,
 * and is _not_ guaranteed to be unique.
 *
 * Note: this implementation removes any non-basic alphanumeric characters,
 * including characters with accents. If the resulting identifier is empty,
 * the string `id` is returned.
 */
fun String.toId(): String = this
    .trim()                                         // Remove any surrounding whitespace
    .lowercase()                                    // Convert to lowercase
    .replace("[^a-z0-9\\-]".toRegex(), "-")         // Replace all non-alphanumeric characters with dashes
    .replace("-+".toRegex(), "-")                   // Replace consecutive dashes with a single dash
    .replace("^-".toRegex(), "")                    // Remove leading dash
    .replace("-$".toRegex(), "")                    // Remove trailing dash
    .takeIf { it.isNotEmpty() } ?: "id"             // Return "id" if the string is empty

/**
 * Checks whether the string is a valid lowercase identifier.
 *
 * A valid identifier consists of only alphanumeric characters and dashes.
 */
fun String.isValidId(): Boolean =
    this.matches("^[a-z0-9\\-]+$".toRegex())

/**
 * Returns the string with normalized line endings.
 */
fun String.normalizeLineEndings(): String =
    this.replace("\r\n".toRegex(), "\n").replace("\r".toRegex(), "\n")

/**
 * Returns the string without any empty lines or lines consisting of only whitespace.
 */
fun String.removeBlankLines(): String =
    this.replace(Regex("(?<=^)\\s+|\\s+(?=(\\n|\$))"), "")

/**
 * Converts the specified name to "Title Case".
 *
 * @return the title-cased string
 */
fun String.toTitleCase(): String = StringBuilder().apply {
    for (i in indices) {
        val prev: Char? = if (i > 0) this[i - 1] else null
        val curr: Char = this[i]
        val next: Char? = if (i < length - 1) this[i + 1] else null
        when {
            prev == null -> append(curr.uppercase())
            curr.isUpperCase() && (!prev.isUpperCase() || !(next?.isUpperCase()
                ?: true)) -> append(' ').append(curr)
            else -> append(curr)
        }
    }
}.toString()