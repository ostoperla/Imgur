package com.trelp.imgur.di

import timber.log.Timber

interface IComponent

fun interface HasComponent<out T : IComponent> {
    fun createComponent(): T
}

class ComponentNotFoundException : RuntimeException("Component was not found")

class ComponentStore {
    private val store = mutableMapOf<String, IComponent>()

    fun put(key: String, value: IComponent) {
        store[key] = value
    }

    fun get(key: String) = store[key] ?: throw ComponentNotFoundException()

    fun remove(key: String) = store.remove(key) ?: throw ComponentNotFoundException()

    fun isExist(key: String) = store.containsKey(key)

    fun find(predicate: (IComponent) -> Boolean): IComponent {
        for ((_, component) in store)
            if (predicate(component)) return component
        throw ComponentNotFoundException()
    }

    override fun toString() =
        store.toList().joinToString(
            prefix = "[",
            postfix = "]"
        ) { "(${it.first} = ${it.second.javaClass.simpleName}#${it.hashCode()})" }
}

object Injector {
    private val store = ComponentStore()

    fun <T : IComponent> init(rootComponent: T) {
        val key = "${rootComponent.javaClass.simpleName}#${rootComponent.hashCode()}"
        store.put(key, rootComponent)
        log(message = "Create $key")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : IComponent> getComponent(owner: HasComponent<T>, key: String): T {
        return if (store.isExist(key)) {
            val component = store.get(key) as T
            log(message = "Get $key")
            component
        } else {
            val component = owner.createComponent().also { store.put(key, it) }
            log(message = "Create $key")
            component
        }
    }

    fun findComponent(predicate: (IComponent) -> Boolean) = store.find(predicate)

    inline fun <reified T : IComponent> findComponent() = findComponent { it is T } as T

    fun destroyComponent(key: String): IComponent {
        val component = store.remove(key)
        log(message = "Destroy $key")
        return component
    }

    fun log(cause: String = "", message: String = "") {
        if (cause != "") {
            Timber.d(cause)
            if (message != "") {
                Timber.d("$message: $store")
            }
        } else {
            if (message != "") {
                Timber.d("$message: $store")
            }
        }
    }
}