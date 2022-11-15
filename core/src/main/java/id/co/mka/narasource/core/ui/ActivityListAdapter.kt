package id.co.mka.narasource.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.mka.narasource.core.R
import id.co.mka.narasource.core.databinding.ItemListActivityBinding
import id.co.mka.narasource.core.domain.model.Activity

class ActivityListAdapter : RecyclerView.Adapter<ActivityListAdapter.ListViewHolder>() {

    var listData: List<Activity>?
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    var onItemClick: ((String) -> String)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_activity, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData?.get(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return listData?.size ?: 0
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListActivityBinding.bind(itemView)
        fun bind(data: Activity) {
            with(binding) {
                when (data.status) {
                    0 -> ivItemStatus.setImageResource(R.drawable.ic_processing)
                    1 -> ivItemStatus.setImageResource(R.drawable.ic_success)
                    2 -> ivItemStatus.setImageResource(R.drawable.ic_failed)
                }
                tvItemCategory.text = data.category
                tvItemTitle.text = data.name
                tvItemTimestamp.text = data.timeStamp
            }
        }

        init {
            binding.root.setOnClickListener {
                listData?.get(adapterPosition)?.name?.let { onItemClick?.invoke(it) }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Activity>() {
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem == newItem
        }
    }
    private val recyclerListDiffer = AsyncListDiffer(this, differCallback)
}
