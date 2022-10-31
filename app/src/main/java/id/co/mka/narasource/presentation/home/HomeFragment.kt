package id.co.mka.narasource.presentation.home

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import id.co.mka.narasource.R
import id.co.mka.narasource.core.utils.loadImage
import id.co.mka.narasource.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)

        val profileMenu = menu.findItem(R.id.menu_two)
        val layoutProfileMenu = profileMenu.actionView as FrameLayout
        val avatarImg = layoutProfileMenu.findViewById(R.id.toolbar_profile_image) as CircleImageView
        avatarImg.setOnClickListener {
            Toast.makeText(requireContext(), "Profile cliked", Toast.LENGTH_SHORT).show()
        }
        avatarImg.loadImage("https://picsum.photos/id/237/300")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
