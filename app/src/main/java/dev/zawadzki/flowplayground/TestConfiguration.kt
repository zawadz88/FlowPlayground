package dev.zawadzki.flowplayground

import android.os.Parcelable
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

sealed class TestConfiguration : Parcelable {

    abstract class LiveDatas : TestConfiguration() {

        protected abstract fun liveData(viewModel: TestViewModel): MutableLiveData<String>

        override fun postAction(viewModel: TestViewModel, action: String) {
            liveData(viewModel).postValue(action)
        }

        override fun observe(
            viewModel: TestViewModel,
            owner: LifecycleOwner,
            actionOnReceive: (String) -> Unit
        ) {
            liveData(viewModel).observe(owner, actionOnReceive::invoke)
        }

        @Parcelize
        object LiveData : TestConfiguration.LiveDatas() {
            override fun liveData(viewModel: TestViewModel) = viewModel.liveData
        }

        @Parcelize
        object SingleLiveEvent : TestConfiguration.LiveDatas() {
            override fun liveData(viewModel: TestViewModel) = viewModel.singleLiveEvent
        }

        @Parcelize
        object LiveEvent : TestConfiguration.LiveDatas() {
            override fun liveData(viewModel: TestViewModel) = viewModel.liveEvent
        }
    }

    abstract class Flows : TestConfiguration() {

        protected abstract fun flow(viewModel: TestViewModel): Flow<String>

        protected abstract suspend fun postActionInternal(
            viewModel: TestViewModel,
            action: String
        )

        override fun postAction(viewModel: TestViewModel, action: String) {
            viewModel.viewModelScope.launch {
                postActionInternal(viewModel, action)
            }
        }

        override fun observe(
            viewModel: TestViewModel,
            owner: LifecycleOwner,
            actionOnReceive: (String) -> Unit
        ) {
            owner.lifecycleScope.launch {
                owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    flow(viewModel).collect(actionOnReceive::invoke)
                }
            }
        }

        @Parcelize
        object MutableSharedFlow : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.mutableSharedFlow

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.mutableSharedFlow.emit(action)
            }
        }

        @Parcelize
        object MutableSharedFlowWithReplay : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.mutableSharedFlowWithReplay

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.mutableSharedFlowWithReplay.emit(action)
            }
        }

        @Parcelize
        object MutableStateFlow : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.mutableStateFlow

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.mutableStateFlow.emit(action)
            }
        }

        @Parcelize
        object ConflatedChannelReceivedAsFlow : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.conflatedChannelFlow

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.conflatedChannel.send(action)
            }
        }

        @Parcelize
        object RendezvousChannelReceivedAsFlow : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.rendezvousChannelFlow

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.rendezvousChannel.send(action)
            }
        }

        @Parcelize
        object UnlimitedChannelReceivedAsFlow : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.unlimitedChannelFlow

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.unlimitedChannel.send(action)
            }
        }

        @Parcelize
        object SharedEagerlyConflatedChannelReceivedAsFlow : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.sharedEagerlyConflatedChannelReceivedAsFlow

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.sharedEagerlyConflatedChannel.send(action)
            }
        }

        @Parcelize
        object SharedLazilyConflatedChannelReceivedAsFlow : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.sharedLazilyConflatedChannelReceivedAsFlow

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.sharedLazilyConflatedChannel.send(action)
            }
        }

        @Parcelize
        object SharedWhileSubscribedConflatedChannelReceivedAsFlow : TestConfiguration.Flows() {

            override fun flow(viewModel: TestViewModel): Flow<String> =
                viewModel.sharedWhileSubscribedConflatedChannelReceivedAsFlow

            override suspend fun postActionInternal(
                viewModel: TestViewModel,
                action: String
            ) {
                viewModel.sharedWhileSubscribedConflatedChannel.send(action)
            }
        }
    }

    abstract fun postAction(viewModel: TestViewModel, action: String)

    abstract fun observe(
        viewModel: TestViewModel,
        owner: LifecycleOwner,
        actionOnReceive: (String) -> Unit
    )
}