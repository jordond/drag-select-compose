package com.dragselectcompose.demo

data class PhotoItem(
    val id: Int,
    val url: String,
) {

    companion object {

        private fun genUrl(seed: Int): String = "https://picsum.photos/seed/$seed/256/256"

        fun createList(size: Int): List<PhotoItem> = List(size) { id ->
            val seed = (0..100_000).random()
            PhotoItem(id, genUrl(seed))
        }
    }
}