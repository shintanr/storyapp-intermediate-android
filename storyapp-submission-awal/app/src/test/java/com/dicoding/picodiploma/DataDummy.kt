package com.dicoding.picodiploma

import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                "name $i",
                "photoUrl $i",
                "createdAt + $i",
                "description $i",
                0.0,
                0.0,
            )
            items.add(quote)
        }
        return items
    }
}