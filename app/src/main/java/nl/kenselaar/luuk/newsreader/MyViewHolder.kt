package nl.kenselaar.luuk.newsreader

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.Title)
    val image: ImageView = itemView.findViewById(R.id.Image)
}