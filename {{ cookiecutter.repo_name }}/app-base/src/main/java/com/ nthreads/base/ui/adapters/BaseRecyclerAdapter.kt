package com.nthreads.base.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.View


abstract class BaseRecyclerAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    val items = emptyList<T>().toMutableList()

    var onLoadMoreListener: OnLoadMoreListener? = null
    var isAllItemsLoaded = false
    var isLoading = true


    override fun getItemCount(): Int = items.size

    class VH(v: View) : RecyclerView.ViewHolder(v)

    fun setItems(newItems : ArrayList<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun insertItem(item : T) {
        items.add(item)
        notifyItemInserted(itemCount)
    }

    fun appendItems(newItems : ArrayList<T>) {
        val startCount = itemCount
        items.addAll(newItems)
        notifyItemRangeChanged(startCount, itemCount)
    }

    fun prependItems(newItems : ArrayList<T>) {
        val startCount = itemCount
        val allItems = (newItems + items).toMutableList()
        items.clear()
        items.addAll(allItems)
        notifyItemRangeChanged(startCount, itemCount)
    }

    fun removeLastItem() {
        if(items.isEmpty()) return

        val lastIndex = itemCount - 1
        items.removeAt(lastIndex)
        notifyItemRemoved(lastIndex)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}
