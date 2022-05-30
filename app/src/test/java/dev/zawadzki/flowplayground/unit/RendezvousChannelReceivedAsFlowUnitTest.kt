@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground.unit

import dev.zawadzki.flowplayground.livedata.InstantTaskExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class RendezvousChannelReceivedAsFlowUnitTest : BaseFlowUnitTest() {

    private val channel = Channel<String>()

    override val flow = channel.receiveAsFlow()

    override suspend fun postAction(action: String) {
        channel.send(action)
    }

}