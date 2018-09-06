package com.inostudio.avinnikov.bestofbehanceandroid.view.project

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.inostudio.avinnikov.bestofbehanceandroid.BR
import com.inostudio.avinnikov.bestofbehanceandroid.R
import com.inostudio.avinnikov.bestofbehanceandroid.model.project.CombineModule
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project.CommentBinding
import com.inostudio.avinnikov.bestofbehanceandroid.viewmodel.project.ModuleBinding
import kotlinx.android.synthetic.main.comment_row.view.*
import kotlinx.android.synthetic.main.comment_separation.view.*
import kotlinx.android.synthetic.main.module_row.view.*
import java.text.SimpleDateFormat
import java.util.*


internal class CombineAdapter(private var modules: MutableList<CombineModule>) : android.support.v7.widget.RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var background = "FFFFFF"
    var commentsCount = "0"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var binding: ViewDataBinding? = null
        when (viewType) {
            0 -> {
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.module_row, parent, false)
                return ModelViewHolder(binding)
            }
            1 -> {
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.comment_row, parent, false)
                return CommentViewHolder(binding)
            }
            2 -> {
                binding = DataBindingUtil.inflate(layoutInflater, R.layout.comment_separation, parent, false)
                return CommentSeparationViewHolder(binding)
            }
        }
        return CommentSeparationViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                (holder as ModelViewHolder).setBackgroung(background)
                holder.bindModule(ModuleBinding())
                when (modules[position].module?.type) {
                    "image" -> {
                        holder.moduleImage.visibility = View.VISIBLE
                        holder.moduleText.visibility = View.GONE
                        holder.webView.visibility = View.GONE
                        val sizes = modules[position].module!!.sizes
                        var curSize = ""
                        sizes.let {
                            when {
                                it.max1920 != "" -> curSize = it.max1920
                                it.max1400 != "" -> curSize = it.max1400
                                it.max1240 != "" -> curSize = it.max1240
                                it.max1200 != "" -> curSize = it.max1200
                                it.max2800 != "" -> curSize = it.max2800
                                it.max3840 != "" -> curSize = it.max3840
                                it.hd != "" -> curSize = it.hd
                                it.disp != "" -> curSize = it.disp
                                it.original != "" -> curSize = it.original
                            }
                        }
                        holder.bindModule(ModuleBinding(curSize, "", ""))
                    }
                    "text" -> {
                        holder.moduleText.visibility = View.VISIBLE
                        holder.moduleImage.visibility = View.GONE
                        holder.webView.visibility = View.GONE
                        holder.bindModule(ModuleBinding("", modules[position].module?.text!!, ""))
                    }
                    "embed" -> {
                        holder.webView.visibility = View.VISIBLE
                        holder.moduleImage.visibility = View.GONE
                        holder.moduleText.visibility = View.GONE
                        holder.bindModule(ModuleBinding("", "", modules[position].module?.embed!!))
                    }
                    "audio" -> {
                        holder.webView.visibility = View.VISIBLE
                        holder.moduleImage.visibility = View.GONE
                        holder.moduleText.visibility = View.GONE
                        holder.bindModule(ModuleBinding("", "", modules[position].module?.embed!!))
                    }
                }
            }
            1 -> {
                val owner = modules[position].comment?.user
                val images = owner?.images
                var curProfileImage = ""
                images?.let {
                    when {
                        it.image276 != "" -> curProfileImage = it.image276
                        it.image130 != "" -> curProfileImage = it.image130
                        it.image138 != "" -> curProfileImage = it.image138
                        it.image115 != "" -> curProfileImage = it.image115
                        it.image100 != "" -> curProfileImage = it.image100
                        it.image50 != "" -> curProfileImage = it.image50
                    }
                }
                (holder as CommentViewHolder).bindComment(CommentBinding(modules[position].comment!!.comment,
                        getDateTime(modules[position].comment!!.createdOn.toString()), owner?.displayName!!, curProfileImage))

                holder.itemView.profileImage.setOnClickListener {
                    val bundle = Bundle().also { it.putString(holder.itemView.context.getString(R.string.user_id), owner.id.toString()) }
                    Navigation.findNavController(holder.itemView.context as AppCompatActivity, R.id.my_nav_host_fragment)
                            .navigate(R.id.action_projectViewFragment_to_userFragment, bundle)
                }
            }
            2 -> {
                (holder as CommentSeparationViewHolder).text.text = String.format("Comments(%s)", commentsCount)
            }
        }
    }

    override fun getItemCount(): Int {
        return modules.size
    }

    override fun getItemViewType(position: Int): Int {

        return when (modules[position].viewType) {
            0 -> 0
            1 -> 1
            2 -> 2
            else -> -1
        }
    }

    class ModelViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var moduleImage = itemView.moduleImage
        var moduleText = itemView.moduleText
        var webView = itemView.webView
        var block = itemView.block
        fun bindModule(data: Any) {
            binding.setVariable(BR.module, data)
            binding.executePendingBindings()
        }

        fun setBackgroung(color: String) {
            try {
                block.setBackgroundColor(Color.parseColor("#$color"))
            } catch (e: java.lang.IllegalArgumentException) {
                block.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }
    }

    class CommentViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindComment(data: Any) {
            binding.setVariable(BR.comment, data)
            binding.executePendingBindings()
        }
    }

    class CommentSeparationViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var text = itemView.commentsCount2
    }

    private fun getDateTime(s: String): String {
        try {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun setColor(color: String) {
        background = color
    }

    fun setModules(newList: MutableList<CombineModule>) {
        this.modules = newList
    }

    fun setCommentCount(count: String) {
        commentsCount = count
    }
}