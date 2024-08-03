package com.example.openinapp.Adapters

import RecentLink
import TopLink
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openinapp.databinding.ToplinkitviewBinding
import com.squareup.picasso.Picasso

class Recent_linksAdapter(private val context: Context, private val topLinkList: List<RecentLink>):RecyclerView.Adapter<Recent_linksAdapter.ViewHolder>(

) {
    inner class ViewHolder(val binding: ToplinkitviewBinding):RecyclerView.ViewHolder(binding.root)
    override fun getItemCount(): Int =topLinkList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Recent_linksAdapter.ViewHolder {
        val binding = ToplinkitviewBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: Recent_linksAdapter.ViewHolder, position: Int) {
        holder.binding.linkName.text = topLinkList[position].app
        holder.binding.clickcounts.text = topLinkList[position].total_clicks.toString()
        holder.binding.date.text = topLinkList[position].times_ago
        holder.binding.linktoclick.text = topLinkList[position].web_link
        Picasso.get().load(topLinkList[position].original_image).into(holder.binding.appimage)
    }
}