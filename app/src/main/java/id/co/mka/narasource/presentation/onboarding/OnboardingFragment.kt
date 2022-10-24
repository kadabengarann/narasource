package id.co.mka.narasource.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.databinding.FragmentOnboardingBinding

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding
    private var indicatorContainer: LinearLayout? = null
    private lateinit var introSliderAdapter: IntroSliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnboarding()
        setupIndicator()
    }

    private fun setupOnboarding() {
        introSliderAdapter = IntroSliderAdapter(
            listOf(
                IntroSlide(
                    getString(R.string.slide_title_1),
                    getString(R.string.lorem_ipsum),
                    R.drawable.img_onboarding_1
                ),
                IntroSlide(
                    getString(R.string.slide_title_2),
                    getString(R.string.lorem_ipsum),
                    R.drawable.img_onboarding_2
                ),
                IntroSlide(
                    getString(R.string.slide_title_3),
                    getString(R.string.lorem_ipsum),
                    R.drawable.img_onboarding_3
                )
            )
        )

        binding?.vpOnboarding?.adapter = introSliderAdapter
        binding?.vpOnboarding?.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                    if (position == introSliderAdapter.itemCount - 1) {
                        binding?.buttonNext?.text = getString(R.string.get_started)
                        binding?.buttonNext?.setOnClickListener {
                            findNavController().navigate(OnboardingFragmentDirections.actionNavigationOnboardingToNavigationLogin())
                        }
                    } else {
                        binding?.buttonNext?.text = getString(R.string.next)
                        binding?.buttonNext?.setOnClickListener {
                            binding?.vpOnboarding?.currentItem?.let {
                                binding?.vpOnboarding?.setCurrentItem(it + 1, true)
                            }
                        }
                    }
                }
            }
        )
        (binding?.vpOnboarding?.getChildAt(0) as? RecyclerView)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding?.textSkip?.setOnClickListener {
            findNavController().navigate(OnboardingFragmentDirections.actionNavigationOnboardingToNavigationLogin())
        }
    }

    private fun setupIndicator() {
        indicatorContainer = binding?.indicatorContainer

        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.let {
                it.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.bg_inactive_indicator
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer?.addView(it)
            }
        }
        setCurrentIndicator(0)
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = indicatorContainer?.childCount
        if (childCount != null) {
            for (i in 0 until childCount) {
                val imageView = indicatorContainer?.getChildAt(i) as ImageView
                if (i == index) {
                    imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.bg_active_indicator
                        )
                    )
                } else {
                    imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.bg_inactive_indicator
                        )
                    )
                }
            }
        }
    }
}
