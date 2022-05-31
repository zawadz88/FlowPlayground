package dev.zawadzki.flowplayground

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

suspend fun <T> Flow<Event<T>>.collectEvent(
    scope: String = "",
    collector: FlowCollector<T>
) {
    collect { event ->
        event.consume(scope)?.let { collector.emit(it) }
    }
}