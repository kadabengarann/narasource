package id.co.mka.narasource.presentation.becomeNarasumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import id.co.mka.narasource.R
import id.co.mka.narasource.core.domain.model.Field
import id.co.mka.narasource.core.ui.FieldAdapter
import id.co.mka.narasource.databinding.FragmentBecomeNarasumberBinding

class BecomeNarasumberFragment : Fragment() {

    private var _binding: FragmentBecomeNarasumberBinding? = null
    private val binding get() = _binding

    private val listData: MutableList<Field> = mutableListOf()
    private val fieldList = arrayOf(
        "UI/UX Design",
        "Frontend Developer",
        "Backend Developer",
        "Mobile Developer",
        "Data Scientist",
        "Data Analyst",
        "Product Manager",
        "Project Manager",
        "Business Analyst",
        "Digital Marketing",
        "Content Writer",
        "Graphic Designer",
        "Video Editor",
        "Photographer",
        "Animator",
        "Translator",
        "Voice Over"
    )

    private val selectedField = mutableListOf<Field>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBecomeNarasumberBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = ""
        }
        setHasOptionsMenu(true)

        setupData()
        setupView()
    }

    private fun setupData() {
        fieldList.forEachIndexed { i, s ->
            val article = Field(
                id = i + 1,
                name = fieldList[i]
            )
            listData.add(article)
        }
    }

    private fun setupView() {
        val adapter = FieldAdapter(requireContext(), listData)
        adapter.onItemClick = {
            creteNewChips(it)
            binding?.inputFieldOfExpertise?.text?.clear()
            adapter.resetData()
        }

        binding?.inputFieldOfExpertise?.apply {
            setAdapter(adapter)
            threshold = 1
        }

        binding?.tvCheckboxHint?.setOnClickListener {
            val action = BecomeNarasumberFragmentDirections.actionNavigationBecomeNarasumberToNavigationTermsUser("narasumber")
            findNavController().navigate(action)
        }
    }
    private fun creteNewChips(txt: Field) {
        if (!selectedField.contains(txt)) {
            selectedField.add(txt)
            val chip = Chip(requireContext())
            chip.apply {
                text = txt.name
                chipIcon = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_launcher_background
                )
                isChipIconVisible = false
                isCloseIconVisible = true
                isClickable = true
                isCheckable = false
                binding?.fieldOfExpertiseGroup?.addView(chip as View)
                setOnCloseIconClickListener {
                    binding?.fieldOfExpertiseGroup?.removeView(chip as View)
                    selectedField.remove(txt)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
