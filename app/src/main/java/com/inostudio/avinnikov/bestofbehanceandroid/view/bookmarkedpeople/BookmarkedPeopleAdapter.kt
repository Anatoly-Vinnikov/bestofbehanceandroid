package com.inostudio.avinnikov.bestofbehanceandroid.view.bookmarkedpeople

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
import com.inostudio.avinnikov.bestofbehanceandroid.model.user.User
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.bookmarkedpeople.BookmarkedPeopleVM
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.user.UserBinding
import kotlinx.android.synthetic.main.post_row.view.*


internal class BookmarkedPeopleAdapter(private var users: MutableList<User>) : android.support.v7.widget.RecyclerView.Adapter<BookmarkedPeopleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.people_row, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myVM = ViewModelProviders.of(holder.itemView.context as AppCompatActivity).get(BookmarkedPeopleVM::class.java)
        val stats = users[position].stats
        val images = users[position].images
        var curProfileImage = ""
        when {
            images?.image276 != "" -> curProfileImage = images?.image276!!
            images.image130 != "" -> curProfileImage = images.image130
            images.image138 != "" -> curProfileImage = images.image138
            images.image115 != "" -> curProfileImage = images.image115
            images.image100 != "" -> curProfileImage = images.image100
            images.image50 != "" -> curProfileImage = images.image50
        }
        var about = ""
        users[position].sections?.let {
            if (!users[position].sections?.isEmpty()!!) {
                about = users[position].sections?.values?.first()!!
            }
        }
        holder.bind(UserBinding(withSuffix(stats?.appreciations?.toLong()!!), withSuffix(stats.views.toLong()),
                withSuffix(stats.followers.toLong()), withSuffix(stats.following.toLong()), users[position].displayName,
                users[position].occupation, users[position].location, about, curProfileImage))

        holder.itemView.setOnClickListener {
            val bundle = Bundle().also { it.putString(holder.itemView.context.getString(R.string.user_id), users[position].id.toString()) }
            Navigation.findNavController(holder.itemView.context as AppCompatActivity, R.id.my_nav_host_fragment)
                    .navigate(R.id.action_bookmarkedPeopleFragment_to_userFragment, bundle)
        }

        holder.itemView.bookmark.setOnClickListener {
            if (myVM.checkIfInDB(users[position].id.toString())) {
                holder.itemView.bookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context as AppCompatActivity, R.drawable.ic_bookmark_border))
                myVM.removeFromDB(users[position])
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Any) {
            binding.setVariable(BR.user, data)
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