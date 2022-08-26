package nl.tudelft.labback.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Executors

/**
 * A command.
 *
 * @property parts the parts of the command
 */
@JvmInline
value class Cmd(
    val parts: List<String>
) {

    constructor(vararg parts: String) : this(parts.toList())

    companion object {
        /**
         * Parses a command-line.
         *
         * @param cmd the command-line to parse
         */
        fun parse(cmd: String): Cmd {
            val parts = mutableListOf<String>()
            val sb = StringBuilder()
            var quote: Char? = null
            var i = 0
            while (i < cmd.length) {
                val c = cmd[i]
                when (c) {
                    '"' -> when (quote) {
                        null -> {
                            // Starting a double-quoted string
                            quote = '"'
                        }
                        '"' -> {
                            // Ending a double-quoted string
                            quote = null
                        }
                        else -> sb.append(c)
                    }
                    '\'' -> when (quote) {
                        null -> {
                            // Starting a single-quoted string
                            quote = '\''
                        }
                        '\'' -> {
                            // Ending a single-quoted string
                            quote = null
                        }
                        else -> sb.append(c)
                    }
                    '\\' -> {
                        if ((quote == '\"' || quote == null) && i + 1 < cmd.length) {
                            sb.append(cmd[i + 1])
                            i += 1
                        }
                        else sb.append('\\')
                    }
                    ' ' -> {
                        if (quote != null) {
                            sb.append(c)
                        } else {
                            // Split
                            if (sb.isNotEmpty()) parts.add(sb.toString())
                            sb.clear()
                        }
                    }
                    else -> sb.append(c)
                }
                i += 1
            }
            if (sb.isNotEmpty()) parts.add(sb.toString())
            return Cmd(parts)
        }
    }

    /**
     * Executes this command.
     *
     * @return the exit code; or 0 when the command succeeded
     */
    fun exec(): Int {
        val pb = ProcessBuilder(*toArray())
        pb.redirectErrorStream(true)
        val process = pb.start()
        val gobbler = Runnable { BufferedReader(InputStreamReader(process.inputStream)).lines().forEach { println(it) } }
        Executors.newSingleThreadExecutor().submit(gobbler)
        return process.waitFor()
    }

    /**
     * Executes this command,
     * or throws when the command returns a non-zero exit code.
     */
    fun execOrThrow() {
        val exitCode = exec()
        if (exitCode != 0) {
            throw RuntimeException("Command failed with exit code $exitCode: ${toString()}")
        }
    }

    /**
     * Transforms the parts of the command into an array of strings.
     *
     * Note: this array may contain sensitive information. Do *not*
     * print this value to the log.
     *
     * @return the command parts as an array of strings
     */
    fun toArray(): Array<String> {
        return parts.toTypedArray()
    }

    /**
     * Use this *only* for debugging!
     */
    override fun toString(): String {
        // This does not properly escape the command,
        // so it may only be used for debug printing!
        return this.parts
            .joinToString(" ") { if (it.contains(' ')) "\"${it.replace("\"", "\\\"")}\"" else it }
    }
}