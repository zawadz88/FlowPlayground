package dev.zawadzki.flowplayground.coroutine


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals

suspend fun <T> CoroutineScope.testFlow(
    flow: Flow<T>,
    testBlock: suspend TestCollector<T>.() -> Unit
) {
    val collector = TestCollector<T>()
    val job = collector.test(this, flow)

    testBlock(collector)

    job.cancel()
}

class TestCollector<T> {
    private val _emissions = mutableListOf<T>()
    val emissions: List<T> get() = _emissions.toList()

    fun test(scope: CoroutineScope, flow: Flow<T>) = scope.launch { flow.toList(_emissions) }

    fun assertExactValues(vararg values: T) {
        assertEquals(values.toList(), emissions)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun runUnconfinedTest(
    testBody: suspend TestScope.() -> Unit
) = runTest(context = UnconfinedTestDispatcher(), dispatchTimeoutMs = 10L, testBody = testBody)
