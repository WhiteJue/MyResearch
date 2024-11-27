package com.my.search.ui.search_page

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.my.search.ui.details_page.DetailsWebActivity
import com.my.search.R
import com.my.search.model.SearchItem


class SearchPageItemAdapter(val itemList: List<SearchItem>, val layoutId: Int) : RecyclerView.Adapter<SearchPageItemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage = view.findViewById<SimpleDraweeView>(R.id.item_image)
        val itemTitle = view.findViewById<TextView>(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val holder = ViewHolder(view)
        holder.itemImage.setOnClickListener {
            val position = holder.adapterPosition
            val item = itemList[position]
            DetailsWebActivity.open(parent.context, item)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        val uri = Uri.parse(item.imageUrl)
        holder.itemImage.setImageURI(uri)
        holder.itemTitle.text = item.title
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}