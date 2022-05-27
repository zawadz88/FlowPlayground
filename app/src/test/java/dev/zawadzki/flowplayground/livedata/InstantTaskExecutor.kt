package dev.zawadzki.flowplayground.livedata

import androidx.arch.core.executor.TaskExecutor

object InstantTaskExecutor : TaskExecutor() {

    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

    override fun postToMainThread(runnable: Runnable) = runnable.run()

    override fun isMainThread() = true

}
