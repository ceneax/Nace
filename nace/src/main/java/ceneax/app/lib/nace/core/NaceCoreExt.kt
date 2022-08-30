package ceneax.app.lib.nace.core

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

data class DelegatedProperty<T : Any, DELEGATE : Any>(
    val property: KProperty1<T, *>,
    val delegatingToInstance: DELEGATE
)

inline fun <reified T : Any, DELEGATE : Any> findDelegatingPropertyInstances(
    instance: T,
    delegatingTo: KClass<DELEGATE>
): List<DelegatedProperty<T, DELEGATE>> = T::class.declaredMemberProperties.mapNotNull { prop ->
    val javaField = prop.javaField
    if (javaField != null && delegatingTo.java.isAssignableFrom(javaField.type)) {
        javaField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val delegateInstance = javaField.get(instance) as DELEGATE
        DelegatedProperty(prop, delegateInstance)
    } else {
        null
    }
}