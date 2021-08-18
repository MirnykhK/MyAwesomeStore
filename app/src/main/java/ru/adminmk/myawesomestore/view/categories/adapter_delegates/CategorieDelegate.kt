package ru.adminmk.myawesomestore.view.categories.adapter_delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.adminmk.myawesomestore.databinding.ListItemCategorieBinding
import ru.adminmk.myawesomestore.model.Categorie

fun categorieDelegate(onClick: () -> Unit) =
    adapterDelegateViewBinding(
        on = { item: Categorie, _: List<Categorie>, _: Int -> true },
        viewBinding = { layoutInflater, root ->
            ListItemCategorieBinding.inflate(
                layoutInflater,
                root,
                false
            )
        }
    ) {
        binding.cardView.setOnClickListener {
            onClick.invoke()
        }

        bind {
            binding.nameTextView.text = item.name
            binding.categortieImageView.setImageDrawable(item.pic)
        }
    }
