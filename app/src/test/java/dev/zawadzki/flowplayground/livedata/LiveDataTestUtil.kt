package dev.zawadzki.flowplayground.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.jupiter.api.Assertions.assertEquals

class TestLiveDataObserver<T> : Observer<T> {
    private val values = mutableListOf<T>()

    override fun onChanged(t: T) {
        values += t
    }

    fun assertExactValues(vararg expected: T): TestLiveDataObserver<T> {
        assertEquals(expected.toList(), values)
        return this
    }
}

fun <T> LiveData<T>.test(observerBlock: TestLiveDataObserver<T>.() -> Unit) {
    val observer = TestLiveDataObserver<T>()
    observeForever(observer)
    observerBlock(observer)
    removeObserver(observer)
}
