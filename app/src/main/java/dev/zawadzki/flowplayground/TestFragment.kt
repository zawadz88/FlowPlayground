package dev.zawadzki.flowplayground

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class TestFragment : Fragment(R.layout.fragment_test) {

    val activityViewModel by activityViewModels<TestViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireNotNull(requireArguments().getParcelable<TestConfiguration>(ARG_NAME))
            .observe(activityViewModel, viewLifecycleOwner, "TestFragment", receivedValues::add)
    }

    companion object {
        private const val ARG_NAME = "ARG"

        var receivedValues = mutableListOf<String>()

        fun newInstance(configuration: TestConfiguration): TestFragment =
            TestFragment().apply {
                arguments = bundleOf(
                    ARG_NAME to configuration
                )
            }
    }
}