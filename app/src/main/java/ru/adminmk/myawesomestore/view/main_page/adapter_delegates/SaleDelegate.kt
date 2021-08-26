package ru.adminmk.myawesomestore.view.main_page.adapter_delegates

import android.content.Context
import android.content.res.ColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.adminmk.myawesomestore.R
import ru.adminmk.myawesomestore.databinding.ListItemProductMainPageBinding
import ru.adminmk.myawesomestore.model.Sale
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import ru.adminmk.myawesomestore.model.Item
import ru.adminmk.myawesomestore.model.Product

fun saleDelegate() =
    adapterDelegateViewBinding(
        on = { item: Sale, _: List<Sale>, _: Int -> true },
        viewBinding = { layoutInflater, root ->
            ListItemProductMainPageBinding.inflate(
                layoutInflater,
                root,
                false
            )
        }
    ) {
        bind {
            makeBinding(binding, item, layoutPosition, context)
        }
    }

fun productDelegate() =
    adapterDelegateViewBinding(
        on = { item: Product, _: List<Product>, _: Int -> true },
        viewBinding = { layoutInflater, root ->
            ListItemProductMainPageBinding.inflate(
                layoutInflater,
                root,
                false
            )
        }
    ) {
        bind {
            makeBinding(binding, item, layoutPosition, context)
        }
    }

private fun makeBinding(
    binding: ListItemProductMainPageBinding,
    item: Item,
    layoutPosition: Int,
    context: Context
) {
    when (item) {
        is Product -> {
            if (item.isNew) {
                binding.salePercentView.visibility = View.VISIBLE
                binding.salePercentView.text = context.getString(R.string.new_product_main_page)
                binding.salePercentView.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black_text))
            } else {
                binding.salePercentView.visibility = View.GONE
            }
            binding.priceTextView.text = String.format("%d\$", item.price)
            binding.priceTextView.setTextColor(ContextCompat.getColor(context, R.color.black_text))
        }
        is Sale -> {
            binding.salePercentView.text = String.format("-%d%%", item.discountPercent)
            binding.priceTextView.text = getSalePriceString(item, binding.root.context)
        }
    }

    binding.starRating.level = item.rating
    binding.ratingTextView.text = String.format("(%d)", item.ratingReviews)
    binding.brandTextView.text = item.brand
    binding.productNameTextView.text = item.name

    val elevation = if (layoutPosition == 0) {
        binding.root.context.resources.getDimensionPixelSize(R.dimen.list_item_product_main_page_first_item_elevation)
    } else {
        0
    }
    binding.cardContainer.elevation = elevation.toFloat()
    binding.salePercentView.elevation = elevation.toFloat()

    (binding.root.layoutParams as RecyclerView.LayoutParams)
        .marginStart =
        if (layoutPosition == 0) {
            binding.root.context.resources.getDimensionPixelSize(R.dimen.list_item_product_main_page_start_margin)
        } else {
            0
        }
}

private fun getSalePriceString(item: Sale, context: Context): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder(item.oldPrice.toString() + "\$")

    val greyForegroundColorSpan = ForegroundColorSpan(
        ContextCompat.getColor(context, R.color.grey_text)
    )
    spannableStringBuilder.setSpan(
        greyForegroundColorSpan,
        0,
        spannableStringBuilder.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    // SPAN_EXCLUSIVE_EXCLUSIVE means to not extend the span when additional
    // text is added in later
    val strikethroughSpan = StrikethroughSpan()
    spannableStringBuilder.setSpan(
        strikethroughSpan,
        0,
        spannableStringBuilder.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    spannableStringBuilder.append(" ")

    spannableStringBuilder.append(item.newPrice.toString() + "\$")
    val redForegroundColorSpan = ForegroundColorSpan(
        ContextCompat.getColor(context, R.color.sale)
    )
    spannableStringBuilder.setSpan(
        redForegroundColorSpan,
        spannableStringBuilder.length - (item.newPrice.toString() + "\$").length,
        spannableStringBuilder.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannableStringBuilder
}
