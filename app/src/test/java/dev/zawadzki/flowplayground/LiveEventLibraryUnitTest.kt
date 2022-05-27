@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground

import com.hadilq.liveevent.LiveEvent
import dev.zawadzki.flowplayground.livedata.InstantTaskExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LiveEventLibraryUnitTest : BaseLiveDataUnitTest() {

    override val liveData = LiveEvent<String>()

}