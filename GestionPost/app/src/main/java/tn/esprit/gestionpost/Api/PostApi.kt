package tn.esprit.gestionpost.Api

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import tn.esprit.gestionpost.Data.Post


interface PostApi {
    data class PostResponse(
        @SerializedName("post")
        val post: Post
    )


    data class PostBody(val title: String, val desc: String, val category: String,val date : String,val state : String, val image: MultipartBody.Part )
    @Multipart
    @POST("api/posts/add-post-and")
    fun addpost(@Part image: MultipartBody.Part,
                @Part("title") title: RequestBody,
                @Part("desc") desc: RequestBody,
                @Part("category") category: RequestBody,
                @Part("date") date: RequestBody,
                @Part("state") state: RequestBody

                 ): Call<PostResponse>


    @GET("api/posts/get-all-posts")
    suspend fun listPosts(): List<Post>


    @POST("api/posts/delete-post")
    @FormUrlEncoded
    suspend fun deletePost(@Field("postId") postId: String): String

}