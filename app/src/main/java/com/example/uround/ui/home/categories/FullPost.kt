package com.example.uround.ui.home.categories

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.example.GetAllPostsQuery
import com.example.uround.R

class FullPost(private val list: GetAllPostsQuery.List) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(list.title)
                .setMessage(list.fullText)
                .setPositiveButton("Back") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}