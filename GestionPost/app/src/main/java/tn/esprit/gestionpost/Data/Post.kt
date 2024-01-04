package tn.esprit.gestionpost.Data

enum class PostState {
    FRIENDS_ONLY,
    PRIVATE,
    PUBLIC
}

data class Post(
    val _id: String,
    val title: String,
    val desc: String,
    val image: String,
    val category: String?,
    val date: String,
    val state: PostState


) {
    override fun toString(): String {
        return "Post(_id='$_id', title='$title', desc='$desc', image='$image', category=$category, date='$date', state=$state)"
    }
}