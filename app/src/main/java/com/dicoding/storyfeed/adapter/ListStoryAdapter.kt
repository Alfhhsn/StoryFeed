package com.dicoding.storyfeed.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyfeed.R
import com.dicoding.storyfeed.response.ListStoryItem
import com.dicoding.storyfeed.view.detail.DetailActivity

class ListStoryAdapter(private val listStory: ArrayList<ListStoryItem>, private val onClick: (ListStoryItem) -> Unit): RecyclerView.Adapter<ListStoryAdapter.StoryViewHolder>() {
    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = listStory[position]
        holder.tvName.text = story.name
        holder.tvDescription.text = story.description
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener {
            onClick(story)

            val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
//                putExtra(DetailActivity.EXTRA_STORY, story)
            }

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                holder.imgPhoto,
                "photo_transition"
            )

            holder.itemView.context.startActivity(intent, options.toBundle())
        }
    }

    fun setStories(stories: List<ListStoryItem>) {
        listStory.clear()
        listStory.addAll(stories)
        notifyDataSetChanged()
    }
}