package dev.zawadzki.flowplayground.livedata

import androidx.arch.core.executor.ArchTaskExecutor
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class InstantTaskExecutorExtension : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext) {
        ArchTaskExecutor.getInstance().setDelegate(InstantTaskExecutor)
    }

    override fun afterAll(context: ExtensionContext) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

}
