package dev.zawadzki.flowplayground

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {

    val viewModel by viewModels<TestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        requireNotNull(intent.getParcelableExtra<TestConfiguration>(CONFIGURATION_EXTRA_NAME))
            .observe(viewModel, this, receivedValues::add)
    }

    companion object {
        const val POST_ACTION_ON_INIT_EXTRA_NAME = "POST_ON_INIT"
        const val CONFIGURATION_EXTRA_NAME = "EXTRA"

        var receivedValues = mutableListOf<String>()

        fun Context.newInstance(
            configuration: TestConfiguration,
            postActionOnViewModelInit: Boolean
        ): Intent =
            Intent(this, TestActivity::class.java)
                .putExtra(CONFIGURATION_EXTRA_NAME, configuration)
                .putExtra(POST_ACTION_ON_INIT_EXTRA_NAME, postActionOnViewModelInit)
    }

}

