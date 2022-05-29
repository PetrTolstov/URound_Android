package com.example.uround.ui.home.categories

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.GetAllPostsQuery
import com.example.uround.R


class AllNewsAdapter(private val posts: MutableList<GetAllPostsQuery.List>, private val context: Context?) :
    RecyclerView.Adapter<AllNewsAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val title: TextView = itemView.findViewById(R.id.titleAllNewsItem)
        val smalText: TextView = itemView.findViewById(R.id.smallTextAllNewsItem)
        val imagePr: ImageView = itemView.findViewById(R.id.imageView)
        val seeMore: Button = itemView.findViewById(R.id.seeMore)
        //var ConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.allnewitemconstwraintlayout)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.allnews_recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }



    fun addItems(postsList: MutableList<GetAllPostsQuery.List>){
        val lastItem = itemCount - 1
        for (i in postsList){
            posts.add(i)
        }

        notifyItemInserted(lastItem)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*val txt = TextView(holder.ConstraintLayout.context)
        txt.setId(R.id.titleAllNewsItem)
        txt.text = posts[position].title

        holder.ConstraintLayout.addView(txt)

        txt.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        */

        //holder.smalText.visibility = View.GONE
        holder.title.text = posts[position].title
        holder.dateTextView.text = posts[position].date.subSequence(3, 15)
        holder.smalText.text = posts[position].shortText

        holder.seeMore.setOnClickListener {
            val myDialogFragment = FullPost(posts[position])
            val manager = (context as FragmentActivity).supportFragmentManager
            myDialogFragment.show(manager, "myDialog")
        }


        if (posts[position].images?.isNotEmpty() == true) {
            println(posts[position].images)
            Glide.with(context!!)
                .load(posts[position].images?.get(0))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_points_sized)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }
                })
                .into(holder.imagePr)
        }else{
            holder.imagePr.visibility = View.GONE
        }

    }





    override fun getItemCount(): Int {
        return posts.size
    }
}
