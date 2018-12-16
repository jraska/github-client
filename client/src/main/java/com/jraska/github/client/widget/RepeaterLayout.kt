package com.jraska.github.client.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

class RepeaterLayout : LinearLayout {
  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  @Suppress("unused")
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
    : super(context, attrs, defStyleAttr, defStyleRes)

  fun <T : RecyclerView.ViewHolder> setAdapter(adapter: RecyclerView.Adapter<T>) {
    removeAllViews()

    val itemCount = adapter.itemCount
    for (position in 0 until itemCount) {
      val itemViewType = adapter.getItemViewType(position)
      val viewHolder = adapter.createViewHolder(this, itemViewType)

      addView(viewHolder.itemView)

      adapter.bindViewHolder(viewHolder, position)
    }
  }
}
