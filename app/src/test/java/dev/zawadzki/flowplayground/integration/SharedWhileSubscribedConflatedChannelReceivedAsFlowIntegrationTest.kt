package dev.zawadzki.flowplayground.integration

import dev.zawadzki.flowplayground.TestConfiguration

class SharedWhileSubscribedConflatedChannelReceivedAsFlowIntegrationTest : BaseIntegrationTest() {
    override val configuration =
        TestConfiguration.Flows.SharedWhileSubscribedConflatedChannelReceivedAsFlow
}
