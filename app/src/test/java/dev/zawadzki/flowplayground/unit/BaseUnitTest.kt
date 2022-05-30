package dev.zawadzki.flowplayground.unit

interface BaseUnitTest {

    fun `should consume value if emitted before collecting`()

    fun `should consume value if emitted after collecting`()

    fun `should consume both values if emitted after collecting`()

    fun `should consume both same values if emitted after collecting`()

    fun `should consume last value if multiple emitted before collecting`()

    fun `should consume values by all consumers if emitted before collecting`()

    fun `should consume values by all consumers if emitted after collecting`()

    fun `should consume value by second consumer if already consumed by first observer`()

    companion object {
        const val ACTION_0 = "I am the initial action"
        const val ACTION_1 = "I am action 1"
        const val ACTION_2 = "I am action 2"
    }
}