package tn.esprit.gestionpost

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.gestionpost.Api.PostApi


class PostsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    private val apiService: PostApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.219.249:5004/") // Replace with your server's base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PostApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        recyclerView = findViewById(R.id.recyclerView)
        postAdapter = PostAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        lifecycleScope.launch {
            fetchPosts()
        }
    }
    fun onAddPostButtonClick(view: View) {
        // Handle the button click, navigate to the AddPostActivity
        val intent = Intent(this, AddPostActivity::class.java)
        startActivity(intent)
    }

    private suspend fun fetchPosts() {
        try {
            val posts = apiService.listPosts()
            Log.d("PostsList", posts.toString())
            postAdapter.submitList(posts)
            Log.d("wwww", "MRIIIGEL")
        } catch (e: Exception) {
            // Handle error
            Toast.makeText(this@PostsActivity, "Error fetching posts", Toast.LENGTH_SHORT).show()
            Log.d("wwww", "MOCH MRIIIGEL")
        }
    }
}
