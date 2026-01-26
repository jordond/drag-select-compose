package com.dragselectcompose.demo

enum class PhotoCategory {
    Landscape, Portrait, Nature, Urban
}

data class PhotoItem(
    val id: Int,
    val url: String,
    val category: PhotoCategory,
) {

    companion object {

        private fun genUrl(seed: Int): String = "https://picsum.photos/seed/$seed/256/256"

        fun createList(size: Int): List<PhotoItem> = List(size) { id ->
            val seed = (0..100_000).random()
            val category = PhotoCategory.entries.random()
            PhotoItem(id, genUrl(seed), category)
        }
    }
}