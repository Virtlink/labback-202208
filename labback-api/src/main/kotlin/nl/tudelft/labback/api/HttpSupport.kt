package nl.tudelft.labback.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import nl.tudelft.labback.application.ApplicationID
import org.http4k.format.ConfigurableJackson
import org.http4k.format.asConfigurable
import org.http4k.format.withStandardMappings
import org.http4k.format.text

object MyJackson : ConfigurableJackson(
    KotlinModule.Builder()
    .configure(KotlinFeature.NullIsSameAsDefault, true)
    .build()
    .asConfigurable()
    .withStandardMappings()
    .text(::ApplicationID, ApplicationID::value)
    .done()
    .deactivateDefaultTyping()
    .configure(SerializationFeature.INDENT_OUTPUT, true)    // Only for debugging
    .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
)