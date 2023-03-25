package nl.tudelft.labback.utils

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * An event with one argument.
 *
 * To use this, declare a read-only field with the event.
 * Use the `+=` (and `-=`) operators to add (and remove) event handlers.
 * Call the event to invoke it.
 *
 * ```kotlin
 * class Foo {
 *     val onClick = Event<String>()
 *
 *     fun click() {
 *         onClick(this, "submit")            // Invoke the event
 *     }
 * }
 *
 * fun listenToClicks(foo: Foo) {
 *     foo.onClick += ::handleClick     // Add the handler
 * }
 *
 * fun handleClick(name: String) {
 *     // Handle the event...
 * }
 * ```
 *
 * @param A the type of the first parameter of the event
 */
class Event<A>: (A) -> Unit {

    /**
     * The event handlers.
     *
     * This is `null` when no event handlers have been added.
     * This is an instance of `MutableList<(A) -> Unit>` when one or more event handlers have been added.
     */
    private var handlers: ConcurrentLinkedQueue<(A) -> Unit>? = null

    /**
     * Invokes this event.
     *
     * @param arg1 the first argument of the event
     */
    override fun invoke(arg1: A) {
        // Atomic copy for thread-safety.
        val handlers = this.handlers ?: return

        for (listener in handlers) {
            listener(arg1)
        }
    }

    /**
     * Adds a handler to this event.
     *
     * Note that adding the same handler twice is possible.
     *
     * @param handler the event handler to add
     */
    @Synchronized
    operator fun plusAssign(handler: (A) -> Unit) {
        var handlers = this.handlers
        if (handlers == null) {
            handlers = ConcurrentLinkedQueue()
            this.handlers = handlers
        }
        handlers.add(handler)
    }

    /**
     * Removes a handler from this event.
     *
     * Note that the handler passed as the argument has to have the same identity
     * as the handler to remove, or the handler will not be removed.
     * If there are multiple copied of the same handler, it is undetermined which
     * handler gets removed.
     *
     * @param handler the event handler to remove
     */
    @Synchronized
    operator fun minusAssign(handler: (A) -> Unit) {
        val handlers = this.handlers ?: return
        handlers.remove(handler)
        if (handlers.isEmpty()) {
            this.handlers = null
        }
    }
}
