package com.inostudio.avinnikov.bestofbehanceandroid.model.project

import com.inostudio.avinnikov.bestofbehanceandroid.model.project.comments.Comment

data class CombineModule(
        val viewType: Int,
        val module: Module?,
        val comment: Comment?
)