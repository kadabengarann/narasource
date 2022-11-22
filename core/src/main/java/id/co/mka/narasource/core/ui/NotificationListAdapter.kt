package id.co.mka.narasource.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.mka.narasource.core.R
import id.co.mka.narasource.core.databinding.ItemListNotificationBinding
import id.co.mka.narasource.core.domain.model.Notification

class NotificationListAdapter : RecyclerView.Adapter<NotificationListAdapter.ListViewHolder>() {

    var listData: List<Notification>?
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_notification, parent, false)
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
        private val binding = ItemListNotificationBinding.bind(itemView)
        fun bind(data: Notification) {
            with(binding) {
                when (data.status) {
                    0 -> ivItemStatus.setImageResource(R.drawable.ic_notification_square_filled)
                    1 -> ivItemStatus.setImageResource(R.drawable.ic_notif)
                }
                tvItemDescription.text = data.description
                tvItemTitle.text = data.title
                tvItemTimestamp.text = data.timeStamp
            }
        }

        init {
            binding.root.setOnClickListener {
                listData?.get(adapterPosition)?.title?.let { onItemClick?.invoke(it) }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
    private val recyclerListDiffer = AsyncListDiffer(this, differCallback)
}
