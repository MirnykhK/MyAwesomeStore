package ru.adminmk.myawesomestore.model

import android.graphics.drawable.Drawable

data class Categorie(
    val name: String,
    val pic: Drawable
)

enum class TopCategorie {
    WOMEN, MEN, KIDS
}
