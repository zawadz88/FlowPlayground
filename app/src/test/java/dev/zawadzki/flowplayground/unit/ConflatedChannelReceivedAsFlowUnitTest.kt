@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground.unit

import dev.zawadzki.flowplayground.livedata.InstantTaskExecutorExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class ConflatedChannelReceivedAsFlowUnitTest : BaseFlowUnitTest() {

    private val channel = Channel<String>(CONFLATED)

    override val flow = channel.receiveAsFlow()

    override suspend fun postAction(action: String) {
        channel.send(action)
    }

}