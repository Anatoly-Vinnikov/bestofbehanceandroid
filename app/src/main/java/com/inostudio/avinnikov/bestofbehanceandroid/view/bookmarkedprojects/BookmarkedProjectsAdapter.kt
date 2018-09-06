package com.inostudio.avinnikov.bestofbehanceandroid.view.bookmarkedprojects

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.inostudio.avinnikov.bestofbehanceandroid.BR
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.model.projects.Project
import com.inostudio.avinnikov.bestofbehanceandroid.utilities.ImageBindingAdapter
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.bookmarkedprojects.BookmarkedProjectsVM
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.main.ProjectListBinding
import kotlinx.android.synthetic.main.post_row.view.*


internal class BookmarkedProjectsAdapter(private var projects: MutableList<Project>) : android.support.v7.widget.RecyclerView.Adapter<BookmarkedProjectsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.post_row, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myVM = ViewModelProviders.of(holder.itemView.context as AppCompatActivity).get(BookmarkedProjectsVM::class.java)
        val owner = projects[position].owners?.get(0)
        val stats = projects[position].stats
        val images = owner?.images
        var curProfileImage = ""
        when {
            images?.image276 != "" -> curProfileImage = images?.image276!!
            images.image130 != "" -> curProfileImage = images.image130
            images.image138 != "" -> curProfileImage = images.image138
            images.image115 != "" -> curProfileImage = images.image115
            images.image100 != "" -> curProfileImage = images.image100
            images.image50 != "" -> curProfileImage = images.image50
        }
        val covers = projects[position].covers
        var curCover = ""
        when {
            covers?.cover404 != "" -> curCover = covers?.cover404!!
            covers.cover230 != "" -> curCover = covers.cover230
            covers.cover202 != "" -> curCover = covers.cover202
            covers.cover115 != "" -> curCover = covers.cover115
            covers.coverOrig != "" -> curCover = covers.coverOrig
        }
        holder.bind(ProjectListBinding(curCover, withSuffix(stats?.appreciations?.toLong()!!),
                withSuffix(stats.views.toLong()), withSuffix(stats.comments.toLong()),
                owner?.displayName!!, owner.occupation, curProfileImage, ImageBindingAdapter.isGrid))

        if (myVM.checkIfInDB(projects[position].id.toString())) {
            holder.itemView.bookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context as AppCompatActivity, R.drawable.ic_bookmark))
        } else {
            holder.itemView.bookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context as AppCompatActivity, R.drawable.ic_bookmark_border))
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle().also { it.putString(holder.itemView.context.getString(R.string.my_project_id), projects[position].id.toString()) }
            Navigation.findNavController(holder.itemView.context as AppCompatActivity, R.id.my_nav_host_fragment)
                    .navigate(R.id.action_bookmarkedProjectsFragment_to_projectViewFragment, bundle)
        }

        holder.itemView.bookmarkCard.setOnClickListener {
            if (myVM.checkIfInDB(projects[position].id.toString())) {
                holder.itemView.bookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context as AppCompatActivity, R.drawable.ic_bookmark_border))
                myVM.removeFromDB(projects[position])
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            } else {
                holder.itemView.bookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context as AppCompatActivity, R.drawable.ic_bookmark))
                myVM.addToDB(projects[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Any) {
            binding.setVariable(BR.post, data)
            binding.executePendingBindings()
        }
    }

    private fun withSuffix(count: Long): String {
        if (count < 1000) return "$count"
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.0f%c",
                count / Math.pow(1000.0, exp.toDouble()),
                "kMGTPE"[exp - 1])
    }
}