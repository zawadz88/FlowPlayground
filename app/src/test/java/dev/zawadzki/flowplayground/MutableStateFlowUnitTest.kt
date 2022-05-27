@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground

import dev.zawadzki.flowplayground.BaseUnitTest.Companion.ACTION_0
import dev.zawadzki.flowplayground.BaseUnitTest.Companion.ACTION_1
import dev.zawadzki.flowplayground.BaseUnitTest.Companion.ACTION_2
import dev.zawadzki.flowplayground.coroutine.testFlow
import dev.zawadzki.flowplayground.livedata.InstantTaskExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class MutableStateFlowUnitTest : BaseFlowUnitTest() {

    override val flow: MutableStateFlow<String> = MutableStateFlow(ACTION_0)

    override suspend fun postAction(action: String) {
        flow.emit(action)
    }

    @Test
    override fun `should consume value if emitted after collecting`() = runUnconfinedTest {
        testFlow(flow) {
            postAction(ACTION_1)

            assertExactValues(ACTION_0, ACTION_1)
        }
    }

    @Test
    override fun `should consume both same values if emitted after collecting`() =
        runUnconfinedTest {
            testFlow(flow) {
                postAction(ACTION_1)
                postAction(ACTION_1)

                assertExactValues(ACTION_0, ACTION_1, ACTION_1)
            }
        }

    @Test
    override fun `should consume both values if emitted after collecting`() = runUnconfinedTest {
        testFlow(flow) {
            postAction(ACTION_1)
            postAction(ACTION_2)

            assertExactValues(ACTION_0, ACTION_1, ACTION_2)
        }
    }

    @Test
    override fun `should consume value by second consumer if already consumed by first observer`() =
        runUnconfinedTest {
            testFlow(flow) observer1@{

                postAction(ACTION_1)
                testFlow(flow) observer2@{

                    this@observer1.assertExactValues(ACTION_0, ACTION_1)
                    this@observer2.assertExactValues(ACTION_1)
                }
            }
        }

    @Test
    override fun `should consume values by all consumers if emitted after collecting`() =
        runUnconfinedTest {
            testFlow(flow) observer1@{
                testFlow(flow) observer2@{
                    postAction(ACTION_1)

                    this@observer1.assertExactValues(ACTION_0, ACTION_1)
                    this@observer2.assertExactValues(ACTION_0, ACTION_1)
                }
            }
        }
}