package com.tstudioz.fax.fme.util

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object PreferenceHelper {

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a value for the given [key].
     */
    operator fun SharedPreferences.set(key: SPKey, value: Any?) {
        val key = key.toString()

        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds a preference based on the given [key].
     * [T] is the type of value
     * @param defaultValue optional defaultValue - will take a default defaultValue if it is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(
        key: SPKey,
        defaultValue: T? = null
    ): T {
        val key = key.toString()

        return when (T::class) {
            String::class -> getString(key, defaultValue as? String ?: "") as T
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
            Boolean::class -> getBoolean(key, defaultValue as? Boolean == true) as T
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun SharedPreferences.booleanFlow(key: String, defaultValue: Boolean): Flow<Boolean> = callbackFlow {
        // Send the current initial value immediately
        trySend(getBoolean(key, defaultValue))

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) {
                trySend(getBoolean(key, defaultValue))
            }
        }

        registerOnSharedPreferenceChangeListener(listener)
        // Clean up listener when the flow collection stops
        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }

}
