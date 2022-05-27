@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground

import dev.zawadzki.flowplayground.livedata.InstantTaskExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class MutableSharedFlowWithReplayUnitTest : BaseFlowUnitTest() {

    override val flow: MutableSharedFlow<String> = MutableSharedFlow(replay = 1)

    override suspend fun postAction(action: String) {
        flow.emit(action)
    }

}