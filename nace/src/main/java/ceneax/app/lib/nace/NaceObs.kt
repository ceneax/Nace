package ceneax.app.lib.nace

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

class NaceObs<E : NaceEffect, T>(initialValue: T) {
    private val value = MutableStateFlow(initialValue)
    private var mBlock: (() -> Unit)? = null

    operator fun getValue(thisRef: E, property: KProperty<*>): T {
        return value.value
    }

    @OptIn(NaceInternalApi::class)
    operator fun setValue(thisRef: E, property: KProperty<*>, value: T) {
        if (this.value.tryEmit(value)) {
            if (mBlock == null) {
                thisRef.invalidater.tryEmit(!thisRef.invalidater.value)
            } else {
                mBlock!!()
            }
        } else {
            NLog.e("NaceObs setValue: $value => 执行失败")
        }
    }

    fun obx(block: () -> Unit) {
        mBlock = block
    }
}

fun <E : NaceEffect, T> E.obs(value: T) = NaceObs<E, T>(value)

//val <T> T.obs
//   get() = NaceObs(this)

data class DelegatedProperty<T : Any, DELEGATE : Any>(val property: KProperty1<T, *>, val delegatingToInstance: DELEGATE)

inline fun <reified T : Any, DELEGATE : Any> findDelegatingPropertyInstances(instance: T, delegatingTo: KClass<DELEGATE>): List<DelegatedProperty<T, DELEGATE>> {
    return T::class.declaredMemberProperties.map { prop ->
        val javaField = prop.javaField
        if (javaField != null && delegatingTo.java.isAssignableFrom(javaField.type)) {
            javaField.isAccessible = true // is private, have to open that up
            @Suppress("UNCHECKED_CAST")
            val delegateInstance = javaField.get(instance) as DELEGATE
            DelegatedProperty(prop, delegateInstance)
        } else {
            null
        }
    }.filterNotNull()
}