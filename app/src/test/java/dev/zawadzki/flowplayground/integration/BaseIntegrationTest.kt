package dev.zawadzki.flowplayground.integration

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.app.launchActivity
import dev.zawadzki.flowplayground.R
import dev.zawadzki.flowplayground.TestActivity
import dev.zawadzki.flowplayground.TestActivity.Companion.newInstance
import dev.zawadzki.flowplayground.TestConfiguration
import dev.zawadzki.flowplayground.TestFragment
import dev.zawadzki.flowplayground.TestViewModel.Companion.INIT_ACTION
import dev.zawadzki.flowplayground.coroutine.runUnconfinedTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class BaseIntegrationTest {

    abstract val configuration: TestConfiguration

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @After
    fun cleanUp() {
        TestActivity.receivedValues.clear()
        TestFragment.receivedValues.clear()
    }

    @Test
    fun `should have one value if received while RESUMED`() = launchTest {
        it.onActivity { activity ->
            activity.postAction(ACTION_1)
        }

        assertEquals(listOf(ACTION_1), TestActivity.receivedValues)
    }

    @Test
    fun `should have one value if received while STOPPED`() = launchTest {
        it.moveToState(Lifecycle.State.CREATED)
        it.onActivity { activity ->
            activity.postAction(ACTION_1)
        }

        it.moveToState(Lifecycle.State.RESUMED)

        assertEquals(listOf(ACTION_1), TestActivity.receivedValues)
    }

    @Test
    fun `should have one value if posted in ViewModel init`() = launchTest(postOnInit = true) {
        it.onActivity { activity ->
            activity.postAction(ACTION_1)
        }

        assertEquals(listOf(INIT_ACTION, ACTION_1), TestActivity.receivedValues)
    }

    @Test
    fun `should have two values if both received while RESUMED`() = launchTest {
        it.onActivity { activity ->
            activity.postAction(ACTION_1)
            activity.postAction(ACTION_2)
        }

        assertEquals(listOf(ACTION_1, ACTION_2), TestActivity.receivedValues)
    }

    @Test
    fun `should receive value again if resumed again`() = launchTest {
        it.onActivity { activity ->
            activity.postAction(ACTION_1)
        }

        it.recreate()

        assertEquals(listOf(ACTION_1, ACTION_1), TestActivity.receivedValues)
    }

    @Test
    fun `should receive value in Activity and Fragment`() = launchTest {
        it.onActivity { activity ->
            createFragment(activity)

            activity.postAction(ACTION_1)
        }

        assertEquals(listOf(ACTION_1), TestActivity.receivedValues)
        assertEquals(listOf(ACTION_1), TestFragment.receivedValues)
    }

    @Test
    fun `should receive value in Activity and Fragment if posted in ViewModel init`() =
        launchTest(postOnInit = true) {
            it.onActivity { activity ->
                createFragment(activity)

                activity.postAction(ACTION_1)
            }

            assertEquals(listOf(INIT_ACTION, ACTION_1), TestActivity.receivedValues)
            assertEquals(listOf(INIT_ACTION, ACTION_1), TestFragment.receivedValues)
        }

    @Test
    fun `should receive value in Activity and Fragment if posted while STOPPED`() = launchTest {
        it.onActivity { activity ->
            createFragment(activity)
        }
        it.moveToState(Lifecycle.State.CREATED)
        it.onActivity { activity ->
            activity.postAction(ACTION_1)
        }

        it.moveToState(Lifecycle.State.RESUMED)

        assertEquals(listOf(ACTION_1), TestActivity.receivedValues)
        assertEquals(listOf(ACTION_1), TestFragment.receivedValues)
    }

    @Test
    fun `should receive value again if resumed again in Activity and Fragment`() =
        launchTest {
            it.onActivity { activity ->
                createFragment(activity)
                activity.postAction(ACTION_1)
            }

            it.recreate()

            assertEquals(listOf(ACTION_1, ACTION_1), TestActivity.receivedValues)
            assertEquals(listOf(ACTION_1, ACTION_1), TestFragment.receivedValues)
        }

    private fun createFragment(activity: TestActivity) {
        activity.supportFragmentManager.commitNow {
            replace(R.id.root, TestFragment.newInstance(configuration))
        }
    }

    private fun launchTest(
        postOnInit: Boolean = false,
        testBlock: (ActivityScenario<TestActivity>) -> Unit,
    ): Unit =
        launchActivity<TestActivity>(
            getApplicationContext<Application>().newInstance(configuration, postOnInit)
        ).use {
            if (configuration is TestConfiguration.Flows) {
                @Suppress("EXPERIMENTAL_API_USAGE")
                runUnconfinedTest {
                    testBlock(it)
                }
            } else {
                testBlock(it)
            }
        }

    private fun TestActivity.postAction(action: String) {
        configuration.postAction(this.viewModel, action)
    }

    companion object {
        const val ACTION_1 = "I am action 1!"
        const val ACTION_2 = "I am action 2!"
    }

}