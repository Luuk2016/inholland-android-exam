package nl.kenselaar.luuk.newsreader

import nl.kenselaar.luuk.newsreader.model.Article

interface MyItemListener {
    fun onItemClicked(item: Article)
}