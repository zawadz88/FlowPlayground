@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground.unit

import androidx.lifecycle.MutableLiveData
import dev.zawadzki.flowplayground.livedata.InstantTaskExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LiveDataUnitTest : BaseLiveDataUnitTest() {

    override val liveData = MutableLiveData<String>()

}