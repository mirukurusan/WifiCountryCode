package io.github.mirukurusan.wificountrycode.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Configuration(
    // Enable or disable the hook
    var enabled: Boolean = false,
    // WiFi country code (e.g., "US", "CN", "JP", "GB", etc.)
    var countryCode: String = DEFAULT_COUNTRY_CODE,
) {
    companion object {
        const val DEFAULT_COUNTRY_CODE = "US"
        
        val default = Configuration()
        
        fun fromJSON(json: String): Configuration {
            return try {
                Json.decodeFromString(serializer(), json)
            } catch (e: Exception) {
                default
            }
        }
    }

    fun toJSON(): String = Json.encodeToString(serializer(), this)
}
