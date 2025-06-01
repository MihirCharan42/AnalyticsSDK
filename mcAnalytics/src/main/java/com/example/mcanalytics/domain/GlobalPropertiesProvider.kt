package com.example.mcanalytics.domain

import java.util.concurrent.ConcurrentHashMap

class GlobalPropertiesProvider {
    private val properties = ConcurrentHashMap<String, Any?>()

    fun set(properties: Map<String, Any?>) {
        this.properties.putAll(properties)
    }

    fun unset(keys: List<String>) {
        keys.forEach { properties.remove(it) }
    }

    fun unsetAll() {
        properties.clear()
    }

    fun mergeWithGlobal(eventData: Map<String, Any?>?): Map<String, Any?>? {
        return eventData?.let { properties.toMutableMap().apply { putAll(it) } }
            ?: properties.toMap()
    }
}