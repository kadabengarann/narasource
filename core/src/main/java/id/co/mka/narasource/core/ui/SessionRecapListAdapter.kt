package id.co.mka.narasource.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.mka.narasource.core.R
import id.co.mka.narasource.core.databinding.ItemListSessionResultBinding
import id.co.mka.narasource.core.domain.model.Session
import id.co.mka.narasource.core.utils.DateUtils

class SessionRecapListAdapter : RecyclerView.Adapter<SessionRecapListAdapter.ListViewHolder>() {

    var listData: List<Session>?
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

//    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_session_result, parent, false)
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
        private val binding = ItemListSessionResultBinding.bind(itemView)
        fun bind(data: Session) {
            with(binding) {
                tvItemDate.text = data.date?.let { DateUtils.formatDate(it) }
                val startTime = data.timeStart?.let { DateUtils.formatTime(it) }
                val endTime = data.timeEnd?.let { DateUtils.formatTime(it) }
                tvItemTime.text = "$startTime - $endTime"
                tvItemPrice.text = "Rp20k"
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Session>() {
        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.session == newItem.session
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem == newItem
        }
    }
    private val recyclerListDiffer = AsyncListDiffer(this, differCallback)
}
