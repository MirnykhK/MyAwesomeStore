package ru.adminmk.myawesomestore.view.main_page

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder
import ru.adminmk.myawesomestore.databinding.ListItemProductMainPageBinding
import ru.adminmk.myawesomestore.model.Sale
import ru.adminmk.myawesomestore.view.custom_view.dp2px

fun RecyclerView.setupOnScrollListener() {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var currentPosition = -1
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            calculateVisibility(dx)
        }

        private fun calculateVisibility(dx: Int) {
            if (dx == 0) return

            if (this@setupOnScrollListener.adapter?.itemCount == 0) {
                return
            }

            val firstCompletelyVisibleItemPosition: Int =
                (this@setupOnScrollListener.layoutManager as LinearLayoutManager)
                    .findFirstCompletelyVisibleItemPosition()

            if (firstCompletelyVisibleItemPosition == -1) return

            if (currentPosition == firstCompletelyVisibleItemPosition) return

            if (dx > 0) {
                if (firstCompletelyVisibleItemPosition > 0) {
                    (this@setupOnScrollListener.layoutManager as LinearLayoutManager)
                        .findViewByPosition(firstCompletelyVisibleItemPosition - 1)
                        ?.let {
                            val viewHolder = this@setupOnScrollListener.getChildViewHolder(it)
                            @Suppress("UNCHECKED_CAST")
                            (viewHolder as AdapterDelegateViewBindingViewHolder<Sale, ListItemProductMainPageBinding>).run {
                                binding.cardContainer.elevation = 0f
                                binding.salePercentView.elevation = 0f
                            }
                        }
                }
            } else {
                if (firstCompletelyVisibleItemPosition + 1 < (
                    this@setupOnScrollListener.adapter?.itemCount
                        ?: 0
                    )
                ) {
                    (this@setupOnScrollListener.layoutManager as LinearLayoutManager)
                        .findViewByPosition(firstCompletelyVisibleItemPosition + 1)
                        ?.let {
                            val viewHolder = this@setupOnScrollListener.getChildViewHolder(it)
                            @Suppress("UNCHECKED_CAST")
                            (viewHolder as AdapterDelegateViewBindingViewHolder<Sale, ListItemProductMainPageBinding>).run {
                                binding.cardContainer.elevation = 0f
                                binding.salePercentView.elevation = 0f
                            }
                        }
                }
            }

            (this@setupOnScrollListener.layoutManager as LinearLayoutManager)
                .findViewByPosition(firstCompletelyVisibleItemPosition)?.let {
                    val viewHolder = this@setupOnScrollListener.getChildViewHolder(it)
                    @Suppress("UNCHECKED_CAST")
                    (viewHolder as AdapterDelegateViewBindingViewHolder<Sale, ListItemProductMainPageBinding>).run {
                        binding.cardContainer.elevation = dp2px(2f)
                        binding.salePercentView.elevation = dp2px(2f)
                    }
                }

            currentPosition = firstCompletelyVisibleItemPosition
        }
    })
}
