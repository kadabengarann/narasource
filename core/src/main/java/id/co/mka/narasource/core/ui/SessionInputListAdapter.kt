package id.co.mka.narasource.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.mka.narasource.core.R
import id.co.mka.narasource.core.databinding.ItemListSessionInputBinding
import id.co.mka.narasource.core.domain.model.Session
import id.co.mka.narasource.core.utils.DateUtils

class SessionInputListAdapter : RecyclerView.Adapter<SessionInputListAdapter.ListViewHolder>() {

    var listData: List<Session>?
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    var onDateClick: ((Session) -> Unit)? = null
    var onTimeStartClick: ((Session) -> Unit)? = null
    var onTimeEndClick: ((Session) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_session_input, parent, false)
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
        private val binding = ItemListSessionInputBinding.bind(itemView)
        fun bind(data: Session) {
            with(binding) {
                tvSession.text = "Sesi ${data.session}"
                data.date?.let {
                    inputDate.setText(DateUtils.formatDate(it))
                }
                if (data.timeStart != null) {
                    data.timeStart?.let {
                        inputStartClock.setText(DateUtils.formatTime(it))
                    }
                    inputLayoutEndClock.isEnabled = true
                    inputEndClock.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    inputEndClock.setHintTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                } else {
                    inputLayoutEndClock.isEnabled = false
                }
                data.timeEnd?.let {
                    inputEndClock.setText(DateUtils.formatTime(it))
                }
                inputDate.setOnClickListener {
                    onDateClick?.invoke(data)
                }
                inputStartClock.setOnClickListener {
                    onTimeStartClick?.invoke(data)
                }
                inputEndClock.setOnClickListener {
                    onTimeEndClick?.invoke(data)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Session>() {
        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem == newItem
        }
    }
    private val recyclerListDiffer = AsyncListDiffer(this, differCallback)
}
