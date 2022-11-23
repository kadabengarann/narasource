package id.co.mka.narasource.core.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.core.text.HtmlCompat
import id.co.mka.narasource.core.R
import id.co.mka.narasource.core.databinding.ItemDropdownListBinding
import id.co.mka.narasource.core.domain.model.Field

class FieldAdapter(
    context: Context,
    private var data: List<Field>,
    private val multipleSelection: Boolean = true
) : ArrayAdapter<Field>(context, 0, data) {

    private val mFilter: ItemFilter = ItemFilter()

    private var filterString: String = ""

    private var filteredData: ArrayList<Field> = data as ArrayList<Field>

    var onItemClick: ((Field) -> Unit)? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemDropdownListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val item = filteredData[position]

        if (item.id == 0) {
            if (multipleSelection) {
                binding.tvNameList.text = HtmlCompat.fromHtml(
                    context.getString(R.string.add_new, item.name),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                binding.tvNameList.text = "Tidak ada hasil"
            }
        } else {
            val start = item.name.indexOf(filterString, 0, true)
            val end = start + filterString.length
            val spannable = android.text.SpannableString(item.name)
            if (start != -1) {
                spannable.setSpan(
                    android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    start,
                    end,
                    android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.tvNameList.text = spannable
            } else {
                binding.tvNameList.text = item.name
            }
        }

        binding.root.setOnClickListener {
            item.let { onItemClick?.invoke(it) }
        }

        return binding.root
    }

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int): Field? {
        return filteredData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    fun resetData() {
        filteredData = mutableListOf<Field>() as ArrayList<Field>
        filteredData.addAll(data as ArrayList<Field>)
        setFilterString("")
        notifyDataSetChanged()
    }

    fun setFilterString(filterString: String) {
        this.filterString = filterString
    }

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            filterString = constraint.toString().trim()
            val filterStringLower = filterString.lowercase()
            val results = FilterResults()
            val list = data
            val nlist = ArrayList<Field>(count)

            if (filterStringLower.isEmpty()) {
                nlist.addAll(list)
            } else {
                list.forEach {
                    if (it.name.lowercase().contains(filterStringLower)) {
                        nlist.add(it)
                    }
                }
            }
            results.values = nlist
            results.count = nlist.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            filteredData = mutableListOf<Field>() as ArrayList<Field>
            if (constraint != null) {
                results.values?.let {
                    filteredData.addAll(it as ArrayList<Field>)
                    notifyDataSetChanged()
                }
                if (results.count == 0) {
                    filteredData.add(Field(0, filterString))
                }
            } else filteredData.addAll(data as ArrayList<Field>)
        }
    }
}
