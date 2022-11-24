package id.co.mka.narasource.presentation.findNarasumber

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.domain.model.Field
import id.co.mka.narasource.core.ui.FieldAdapter
import id.co.mka.narasource.databinding.FragmentFirstFormBinding

@AndroidEntryPoint
class FirstFormFragment : Fragment() {

    private var _binding: FragmentFirstFormBinding? = null
    private val binding get() = _binding

    private val findNarasumberViewModel: FindNarasumberViewModel by activityViewModels()
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstFormBinding.inflate(inflater, container, false)
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
        toggleButton()
        setupView()
        setupAction()
    }

    private fun setupView() {
        val adapter = FieldAdapter(requireContext(), listData, false)
        binding?.apply {
            inputCategory.apply {
                setAdapter(adapter)
                threshold = 1
            }
        }
        adapter.onItemClick = {
            binding?.inputCategory?.apply {
                setText(it.name)
                dismissDropDown()
            }
            adapter.resetData()
            toggleButton()
        }
    }

    private fun setupAction() {
        binding?.apply {
            inputTitle.doOnTextChanged { _, _, _, _ ->
                toggleButton()
            }
            inputPurpose.doOnTextChanged { _, _, _, _ ->
                toggleButton()
            }
            inputCategory.doOnTextChanged { _, _, _, _ ->
            }
            inputCategory.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    inputCategory.dismissDropDown()
                    val item = inputCategory.adapter.getItem(0) as Field
                    if (item.id == 0) {
                        inputCategory.setText("")
                    } else {
                        inputCategory.setText(item.name)
                    }
                }
            }

            inputAmmount.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotEmpty()) {
                    findNarasumberViewModel.postNarasumberCount(text.toString())
                }
                toggleButton()
            }
            stepperPlusAmmount.setOnClickListener {
                if (inputAmmount.text.toString().isEmpty()) {
                    inputAmmount.setText("1")
                } else inputAmmount.setText((inputAmmount.text.toString().toInt() + 1).toString())
                findNarasumberViewModel.postNarasumberCount(inputAmmount.text.toString())
                inputAmmount.setSelection(inputAmmount.text.toString().length)
                toggleButton()
            }
            stepperMinusAmmount.setOnClickListener {
                if (inputAmmount.text.toString().isEmpty()) {
                    inputAmmount.setText("0")
                } else if (inputAmmount.text.toString().toInt() >= 1) {
                    inputAmmount.setText((inputAmmount.text.toString().toInt() - 1).toString())
                }
                findNarasumberViewModel.postNarasumberCount(inputAmmount.text.toString())
                toggleButton()
                inputAmmount.setSelection(inputAmmount.text.toString().length)
            }
            btnSubmit.setOnClickListener {
                val action = FirstFormFragmentDirections.actionNavFirstFormToNavSecondForm()
                findNavController().navigate(action)
            }
        }
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

    private fun toggleButton() {
        val inputTitle = binding?.inputTitle?.text?.isNotEmpty()
        val inputPurpose = binding?.inputPurpose?.text?.isNotEmpty()
        val inputCategory = binding?.inputCategory?.text?.isNotEmpty()
        val inputAmmount = binding?.inputAmmount?.text?.isNotEmpty() == true && binding?.inputAmmount?.text.toString().toInt() > 0
        val enabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button)
        val disabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_disabled)

        if (inputTitle == true && inputPurpose == true && inputCategory == true && inputAmmount) {
            binding?.btnSubmit?.background = enabledBackground
            binding?.btnSubmit?.isEnabled = true
        } else {
            binding?.btnSubmit?.background = disabledBackground
            binding?.btnSubmit?.isEnabled = false
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
