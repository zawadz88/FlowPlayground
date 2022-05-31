package dev.zawadzki.flowplayground

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import dev.zawadzki.flowplayground.TestActivity.Companion.CONFIGURATION_EXTRA_NAME
import dev.zawadzki.flowplayground.TestActivity.Companion.POST_ACTION_ON_INIT_EXTRA_NAME
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class TestViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    val liveData = MutableLiveData<String>()

    val singleLiveEvent = SingleLiveEvent<String>()

    val liveEvent = LiveEvent<String>()

    val liveDataWithEventWrapper = MutableLiveData<Event<String>>()

    val mutableSharedFlow: MutableSharedFlow<String> = MutableSharedFlow()

    val mutableSharedFlowWithReplay: MutableSharedFlow<String> = MutableSharedFlow(replay = 1)

    val mutableStateFlow: MutableStateFlow<String> = MutableStateFlow(DEFAULT_ACTION)

    val conflatedChannel = Channel<String>(Channel.CONFLATED)
    val conflatedChannelFlow = conflatedChannel.receiveAsFlow()

    val rendezvousChannel = Channel<String>()
    val rendezvousChannelFlow = rendezvousChannel.receiveAsFlow()

    val unlimitedChannel = Channel<String>(Channel.UNLIMITED)
    val unlimitedChannelFlow = unlimitedChannel.receiveAsFlow()

    val sharedEagerlyConflatedChannel = Channel<String>(Channel.CONFLATED)
    val sharedEagerlyConflatedChannelReceivedAsFlow = sharedEagerlyConflatedChannel.receiveAsFlow()
        .shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)

    val sharedLazilyConflatedChannel = Channel<String>(Channel.CONFLATED)
    val sharedLazilyConflatedChannelReceivedAsFlow = sharedLazilyConflatedChannel.receiveAsFlow()
        .shareIn(viewModelScope, started = SharingStarted.Lazily, replay = 1)

    val sharedWhileSubscribedConflatedChannel = Channel<String>(Channel.CONFLATED)
    val sharedWhileSubscribedConflatedChannelReceivedAsFlow =
        sharedWhileSubscribedConflatedChannel.receiveAsFlow()
            .shareIn(viewModelScope, started = SharingStarted.WhileSubscribed(), replay = 1)

    val mutableSharedFlowWithReplayAndEventWrapper: MutableSharedFlow<Event<String>> = MutableSharedFlow(replay = 1)

    init {
        val postOnInit = savedStateHandle.get<Boolean>(POST_ACTION_ON_INIT_EXTRA_NAME) ?: false
        val testConfiguration =
            requireNotNull(savedStateHandle.get<TestConfiguration>(CONFIGURATION_EXTRA_NAME))
        if (postOnInit) {
            testConfiguration.postAction(this, INIT_ACTION)
        }
    }

    companion object {
        const val INIT_ACTION = "I am init action!"
        const val DEFAULT_ACTION = "I a default action!"
    }
}