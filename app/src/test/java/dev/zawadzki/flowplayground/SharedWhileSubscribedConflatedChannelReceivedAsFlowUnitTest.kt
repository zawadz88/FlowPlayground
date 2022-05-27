@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zawadzki.flowplayground

import dev.zawadzki.flowplayground.livedata.InstantTaskExecutorExtension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.coroutines.CoroutineContext

@ExtendWith(InstantTaskExecutorExtension::class)
class SharedWhileSubscribedConflatedChannelReceivedAsFlowUnitTest : BaseFlowUnitTest() {

    private val channel = Channel<String>(CONFLATED)

    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext = UnconfinedTestDispatcher()
    }

    override val flow = channel.receiveAsFlow()
        .shareIn(scope, started = SharingStarted.WhileSubscribed(), replay = 1)

    override suspend fun postAction(action: String) {
        channel.send(action)
    }

}