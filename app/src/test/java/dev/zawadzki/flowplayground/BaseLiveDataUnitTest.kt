package dev.zawadzki.flowplayground

import androidx.lifecycle.MutableLiveData
import dev.zawadzki.flowplayground.livedata.test
import org.junit.jupiter.api.Test

abstract class BaseLiveDataUnitTest {
    abstract val liveData: MutableLiveData<String>

    @Test
    fun `should consume value if emitted before collecting`() {
        liveData.postValue(ACTION_1)

        liveData.test {
            assertExactValues(ACTION_1)
        }
    }

    @Test
    fun `should consume value if emitted after collecting`() {
        liveData.test {
            liveData.postValue(ACTION_1)

            assertExactValues(ACTION_1)
        }
    }

    @Test
    fun `should consume both values if emitted after collecting`() {
        liveData.test {
            liveData.postValue(ACTION_1)
            liveData.postValue(ACTION_2)

            assertExactValues(ACTION_1, ACTION_2)
        }
    }

    @Test
    fun `should consume both same values if emitted after collecting`() {
        liveData.test {
            liveData.postValue(ACTION_1)
            liveData.postValue(ACTION_1)

            assertExactValues(ACTION_1, ACTION_1)
        }
    }

    @Test
    fun `should consume last value if multiple emitted before collecting`() {
        liveData.postValue(ACTION_1)
        liveData.postValue(ACTION_2)

        liveData.test {
            assertExactValues(ACTION_2)
        }
    }

    @Test
    fun `should consume values by all consumers if emitted before collecting`() {
        liveData.postValue(ACTION_1)

        liveData.test observer1@{
            liveData.test observer2@{
                this@observer1.assertExactValues(ACTION_1)
                this@observer2.assertExactValues(ACTION_1)
            }
        }

    }

    @Test
    fun `should consume values by all consumers if emitted after collecting`() {
        liveData.test observer1@{
            liveData.test observer2@{
                liveData.postValue(ACTION_1)
                this@observer1.assertExactValues(ACTION_1)
                this@observer2.assertExactValues(ACTION_1)
            }
        }
    }

    @Test
    fun `should consume value by second consumer if already consumed by first observer`() {
        liveData.test observer1@{
            liveData.postValue(ACTION_1)

            liveData.test observer2@{
                this@observer1.assertExactValues(ACTION_1)
                this@observer2.assertExactValues(ACTION_1)
            }
        }
    }

    companion object {
        protected const val ACTION_1 = "I am action 1"
        protected const val ACTION_2 = "I am action 2"
    }
}