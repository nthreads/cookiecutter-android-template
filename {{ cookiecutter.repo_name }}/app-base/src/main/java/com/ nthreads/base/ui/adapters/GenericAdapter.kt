package com.nthreads.base.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class GenericAdapter<T>(
    var items: List<T> = emptyList<T>().toMutableList(),
    val mClickListener: ((View, T, Int, Int?) -> Unit)? = null,
    val mOnLoadMoreListener: (() -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isAllItemsLoaded = false
    var isLoading = true

    fun setItemList(listItems: List<T>) {
        items = emptyList<T>().toMutableList()
        items = listItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false), viewType
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as Binder<T>).bind(item, {view, item, action ->
            mClickListener?.invoke(view, item, position, action)
        })

        holder.itemView.setOnClickListener { view ->
            mClickListener?.invoke(view, item, position, null)
        }

        if (position >= itemCount - 2 && !isAllItemsLoaded && !isLoading) {
            mOnLoadMoreListener?.let {
                isLoading = true
                mOnLoadMoreListener.invoke()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, items[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

    interface Binder<T> {
        fun bind(data: T, mCallback: ((View, T, Int) -> Unit)? = null)
    }
}
