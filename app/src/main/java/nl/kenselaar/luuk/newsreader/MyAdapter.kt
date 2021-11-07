package nl.kenselaar.luuk.newsreader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import nl.kenselaar.luuk.newsreader.model.Article

class MyAdapter(private val items: List<Article>) : RecyclerView.Adapter<MyViewHolder>() {

    private var itemListener : MyItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_item_view, parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position].Title
        holder.image.load(items[position].Image) {
            crossfade(true)
            placeholder(R.drawable.ic_baseline_image_24)
            transformations(CircleCropTransformation())
        }

        holder.itemView.setOnClickListener { itemListener?.onItemClicked(items[position]) }
    }

    fun setItemListener(listener: MyItemListener) {
        itemListener = listener
    }

    override fun getItemCount(): Int = items.size
}