@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground

import dev.zawadzki.flowplayground.livedata.InstantTaskExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class SingleLiveEventUnitTest : BaseLiveDataUnitTest() {

    override val liveData = SingleLiveEvent<String>()

}