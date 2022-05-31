package dev.zawadzki.flowplayground

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<Event<T>>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    scope: String = "",
    observer: Observer<T>
) {
    observe(lifecycleOwner) { event ->
        event?.consume(scope)?.let { observer.onChanged(it) }
    }
}