package id.co.mka.narasource.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.mka.narasource.core.R
import id.co.mka.narasource.core.databinding.ItemListArticleBinding
import id.co.mka.narasource.core.domain.model.Article
import id.co.mka.narasource.core.utils.loadImage

class ArticleListAdapter : RecyclerView.Adapter<ArticleListAdapter.ListViewHolder>() {

    var listData: List<Article>?
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    var onItemClick: ((String) -> String)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_article, parent, false))

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
        private val binding = ItemListArticleBinding.bind(itemView)
        fun bind(data: Article) {
            with(binding) {
                ivItemImage.loadImage(data.image)
                tvItemTitle.text = data.name
                tvItemSubtitle.text = data.desc
            }
        }

        init {
            binding.root.setOnClickListener {
                listData?.get(adapterPosition)?.name?.let { onItemClick?.invoke(it) }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    private val recyclerListDiffer = AsyncListDiffer(this, differCallback)
}
