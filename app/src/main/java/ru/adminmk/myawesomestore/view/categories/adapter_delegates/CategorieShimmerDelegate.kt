package ru.adminmk.myawesomestore.view.categories.adapter_delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.adminmk.myawesomestore.databinding.ListItemCategorieShimmerBinding

fun categorieShimmerDelegate() =
    adapterDelegateViewBinding(
        on = { item: ShimmerStub, _: List<ShimmerStub>, _: Int -> true },
        viewBinding = { layoutInflater, root ->
            ListItemCategorieShimmerBinding.inflate(
                layoutInflater,
                root,
                false
            )
        }
    ) { }
