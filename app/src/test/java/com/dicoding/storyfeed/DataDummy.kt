package com.dicoding.storyfeed

import com.dicoding.storyfeed.response.ListStoryItem
import com.dicoding.storyfeed.response.StoryResponse

object DataDummy {
    fun generateDummyStories(): StoryResponse {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 1..20) {
            val story = ListStoryItem(
                createdAt = "2024-30-213T7:12:127",
                description = "Description $i",
                id = "id_$i",
                name = "Name $i",
                photoUrl = "https://d1bpj0tv6vfxyp.cloudfront.net/articles/688004_12-3-2021_12-57-42.webp",
                lon = 0.0,
                lat = 0.0
            )
            listStory.add(story)
        }

        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }
}