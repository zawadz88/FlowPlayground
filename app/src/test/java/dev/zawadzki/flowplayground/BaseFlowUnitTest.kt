@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground

import dev.zawadzki.flowplayground.BaseUnitTest.Companion.ACTION_1
import dev.zawadzki.flowplayground.BaseUnitTest.Companion.ACTION_2
import dev.zawadzki.flowplayground.coroutine.testFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

abstract class BaseFlowUnitTest : BaseUnitTest {

    abstract val flow: Flow<String>

    abstract suspend fun postAction(action: String)

    @Test
    override fun `should consume value if emitted before collecting`() = runUnconfinedTest {
        postAction(ACTION_1)

        testFlow(flow) {
            assertExactValues(ACTION_1)
        }

    }

    @Test
    override fun `should consume value if emitted after collecting`() = runUnconfinedTest {
        testFlow(flow) {
            postAction(ACTION_1)

            assertExactValues(ACTION_1)
        }
    }

    @Test
    override fun `should consume both values if emitted after collecting`() = runUnconfinedTest {
        testFlow(flow) {
            postAction(ACTION_1)
            postAction(ACTION_2)

            assertExactValues(ACTION_1, ACTION_2)
        }
    }

    @Test
    override fun `should consume both same values if emitted after collecting`() =
        runUnconfinedTest {
            testFlow(flow) {
                postAction(ACTION_1)
                postAction(ACTION_1)

                assertExactValues(ACTION_1, ACTION_1)
            }
        }

    @Test
    override fun `should consume last value if multiple emitted before collecting`() =
        runUnconfinedTest {
            postAction(ACTION_1)
            postAction(ACTION_2)

            testFlow(flow) {
                assertExactValues(ACTION_2)
            }
        }

    @Test
    override fun `should consume values by all consumers if emitted before collecting`() =
        runUnconfinedTest {
            postAction(ACTION_1)

            testFlow(flow) observer1@{
                testFlow(flow) observer2@{
                    this@observer1.assertExactValues(ACTION_1)
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

                    this@observer1.assertExactValues(ACTION_1)
                    this@observer2.assertExactValues(ACTION_1)
                }
            }
        }

    @Test
    override fun `should consume value by second consumer if already consumed by first observer`() =
        runUnconfinedTest {
            testFlow(flow) observer1@{

                postAction(ACTION_1)
                testFlow(flow) observer2@{

                    this@observer1.assertExactValues(ACTION_1)
                    this@observer2.assertExactValues(ACTION_1)
                }
            }
        }

    protected fun runUnconfinedTest(
        testBody: suspend TestScope.() -> Unit
    ) = runTest(context = UnconfinedTestDispatcher(), dispatchTimeoutMs = 10L, testBody = testBody)

}