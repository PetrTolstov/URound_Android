package com.example.uround.ui.home.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.apollographql.apollo3.ApolloClient
import com.example.GetAllPostsQuery
import com.example.type.UserAuthInput
import com.example.uround.MainActivity
import com.example.uround.R
import com.example.uround.databinding.FragmentAllnewsBinding
import com.example.uround.ui.home.HomeViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AllNewsFragment : Fragment() {

    private var _binding: FragmentAllnewsBinding? = null
    lateinit var postList: MutableList<GetAllPostsQuery.List>
    lateinit var recyclerView: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val AllNewsViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(AllNewsModelView::class.java)

        _binding = FragmentAllnewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        GlobalScope.launch{
            while (true){
                try {
                    renderPosts(getPosts(), root )
                    println()
                    break
                }catch (err : kotlin.UninitializedPropertyAccessException){
                    delay(5000)
                    continue
                }
            }


        }

         */
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun renderPosts(response: GetAllPostsQuery.GetAllPosts, root: View) {
        val receivedPosts = response.list
        println(receivedPosts)

        recyclerView = root.findViewById(R.id.AllNewsrecyclerView)

        if (this::postList.isInitialized) {
            val lastItem = postList.size - 1
            if (receivedPosts != null) {
                for (i in receivedPosts) {
                    postList.add(i)
                }
            }
            activity?.runOnUiThread(java.lang.Runnable {
                recyclerView.adapter!!.notifyItemInserted(lastItem + 1)
                recyclerView.scrollToPosition(lastItem + 1)
            })


        } else {
            postList = receivedPosts as MutableList<GetAllPostsQuery.List>

            activity?.runOnUiThread(java.lang.Runnable {
                recyclerView.layoutManager = LinearLayoutManager(context)

                recyclerView.adapter = AllNewsAdapter(postList, context)


            })

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {


                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1)) {

                        GlobalScope.launch {
                            renderPosts(getPosts(), root )
                        }
                        println("last")
                    }
                }
            })
        }
    }



    suspend fun getPosts(): GetAllPostsQuery.GetAllPosts {
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://uround-server.herokuapp.com")
            .build()

        val user = UserAuthInput(MainActivity.USER_INFO.getString("username")!!, MainActivity.USER_INFO.getString("password")!!)

        val amount : Int = if (this::postList.isInitialized){
            postList.size
        }else{
            0
        }



        val response = apolloClient.query(GetAllPostsQuery(amount, user)).execute()
        return response.data!!.getAllPosts
    }
}