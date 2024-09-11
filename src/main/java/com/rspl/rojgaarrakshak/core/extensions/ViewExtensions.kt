package com.aqube.ram.extension

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Shorthand extension function to make view gone
 */
fun View.makeGone() {
    this.visibility = View.GONE
}

/**
 * Shorthand extension function to make view visible
 */
fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeVisible(isVisible : Boolean){
    this.visibility = if(isVisible) View.VISIBLE else View.GONE
}

val verticalItemDecoration = object : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top=30

        when(parent.getChildAdapterPosition(view)){
            3,8,13,18 -> {
                outRect.left=90
            }

            4,9,14,19 -> {
                outRect.right=80
            }

        }

        if(parent.getChildAdapterPosition(view)==parent.adapter!!.itemCount-1)
            outRect.bottom=30
    }
}

val verticalItemDecorationHome = object : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top=30

        if(parent.getChildAdapterPosition(view)==parent.adapter!!.itemCount-1)
            outRect.bottom=30
    }
}
