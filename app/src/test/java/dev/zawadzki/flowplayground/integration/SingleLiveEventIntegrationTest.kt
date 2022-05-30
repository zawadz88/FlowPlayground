package dev.zawadzki.flowplayground.integration

import dev.zawadzki.flowplayground.TestConfiguration

class SingleLiveEventIntegrationTest : BaseIntegrationTest() {
    override val configuration = TestConfiguration.LiveDatas.SingleLiveEvent
}
