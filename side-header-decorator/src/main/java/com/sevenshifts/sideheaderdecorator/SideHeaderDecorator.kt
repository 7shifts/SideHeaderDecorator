package com.sevenshifts.sideheaderdecorator

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.math.max
import kotlin.math.min

abstract class SideHeaderDecorator<H>(private val headerProvider: HeaderProvider<H>) : RecyclerView.ItemDecoration() {

    interface HeaderProvider<H> {
        fun getHeader(position: Int): H
    }

    abstract fun getHeaderView(header: H, parent: RecyclerView): View

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val headerView = getHeaderView(headerProvider.getHeader(position), parent)

        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val parentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)

        view.measure(parentWidthMeasureSpec, wrapContentMeasureSpec)
        headerView.measure(wrapContentMeasureSpec, wrapContentMeasureSpec)

        headerView.measuredHeight.takeIf { it > view.measuredHeight }?.let {
            view.layoutParams = view.layoutParams.apply { height = it }
        }

        view.apply { setPadding(headerView.measuredWidth, paddingTop, paddingRight, paddingBottom) }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val lastIndex = parent.adapter?.let { it.itemCount - 1 } ?: return

        for (childPosition in (0..parent.childCount)) {
            val view = parent.getChildAt(childPosition)

            val position = parent.getChildAdapterPosition(view)

            if (position == RecyclerView.NO_POSITION) {
                continue
            }

            val header = headerProvider.getHeader(position)
            val headerView = getHeaderView(header, parent)
            val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            headerView.measure(wrapContentMeasureSpec, wrapContentMeasureSpec)
            headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)

            val beginsGroup = (position == 0 || header != headerProvider.getHeader(position - 1))
            val endsGroup = (position == lastIndex || header != headerProvider.getHeader(position + 1))

            c.save()
            if (childPosition == 0) {
                val top = when {
                    beginsGroup && endsGroup -> view.top.toFloat()
                    beginsGroup -> max(view.top, 0).toFloat()
                    endsGroup -> min(view.bottom - headerView.measuredHeight, 0).toFloat()
                    else -> 0f
                }

                c.translate(0f, top)
                headerView.draw(c)
            } else if (beginsGroup) {
                c.translate(0f, view.top.toFloat())
                headerView.draw(c)
            }
            c.restore()
        }
    }
}
