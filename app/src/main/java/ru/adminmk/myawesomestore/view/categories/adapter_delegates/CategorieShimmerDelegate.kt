package ru.adminmk.myawesomestore.view.categories.adapter_delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.adminmk.myawesomestore.databinding.ListItemCategorieBinding
import ru.adminmk.myawesomestore.databinding.ListItemCategorieShimmerBinding
import ru.adminmk.myawesomestore.model.Categorie

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
