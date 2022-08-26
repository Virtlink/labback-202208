package nl.tudelft.labback.utils

/**
 * Merges this map with the specified map.
 *
 * The resulting map has the keys that occur in this map and the keys
 * that occur in the specified map.
 *
 * If a key occurs in both maps, the [reduce] function is called to
 * determine the new value associated with the key in the merged map.
 *
 * @param other the other map
 * @param reduce the function that determines the value of the resulting key when the key occurs in both maps
 * @return the merged map
 */
fun <K, V> Map<K, V>.merge(other: Map<K, V>, reduce: (V, V) -> V): Map<K, V> {
    val result = LinkedHashMap<K, V>(this.size + other.size)
    result.putAll(this)
    other.forEach { e -> result[e.key] = result[e.key]?.let { reduce(it, e.value) } ?: e.value }
    return result
}

/**
 * Takes associations from this map, or from the specified map
 * if the key is not present in this map.
 *
 * @param base the base map
 * @return the resulting map
 */
infix fun <K, V> Map<K, V>.or(base: Map<K, V>): Map<K, V> =
    merge(base) { v1, _ -> v1 }