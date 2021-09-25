package nl.kenselaar.luuk.newsreader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import nl.kenselaar.luuk.newsreader.model.Article
import nl.kenselaar.luuk.newsreader.model.ArticleResult

class MyAdapter(private val items: List<Article>) : RecyclerView.Adapter<MyViewHolder>() {

    private var itemListener : MyItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_item_view, parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position].Title
        holder.image.load(items[position].Image)

        holder.itemView.setOnClickListener { itemListener?.onItemClicked(items[position]) }
    }

    fun setItemListener(listener: MyItemListener) {
        itemListener = listener
    }

    override fun getItemCount(): Int = items.size
}