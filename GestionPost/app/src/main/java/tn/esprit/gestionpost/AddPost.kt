package tn.esprit.gestionpost

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.gestionpost.Api.PostApi
import tn.esprit.gestionpost.Data.PostState
import tn.esprit.gestionpost.Utils.RetrofitClient

class AddPostActivity : AppCompatActivity() {

    private lateinit var viewModel: AddPostViewModel
    private var selectedImageUri: Uri? = null
    private lateinit var imageLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        Log.d("aaaaaaaaaaaaaaa", "This is an info message")
        viewModel = ViewModelProvider(this).get(AddPostViewModel::class.java)
        initUI()
        setupImageLauncher()
        val backButton: Button = findViewById(R.id.back_btn)
        backButton.setOnClickListener {
            // Lancez l'activité PostsActivity lorsque le bouton de retour est cliqué
            val intent = Intent(this, PostsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupImageLauncher() {
        imageLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                // Handle the selected URI (image)
                Log.d("PhotoPicker", "Selected URI: $uri")
                selectedImageUri = uri
                val showImageView: ImageView = findViewById(R.id.showimage)
                showImageView.setImageURI(selectedImageUri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    private fun initUI() {
        val titleEditText: TextInputEditText = findViewById(R.id.title_txt)
        val descEditText: TextInputEditText = findViewById(R.id.desc_txt)
        val privateBox = findViewById<RadioButton>(R.id.private_box)
        val publicBox = findViewById<RadioButton>(R.id.public_box)
        val friendsBox = findViewById<RadioButton>(R.id.friends_box)
        val addPostButton: Button = findViewById(R.id.update_btn)
        val imageButton: ImageButton = findViewById(R.id.image_btn)
        val showImageView: ImageView = findViewById(R.id.showimage)

        val radioGroup: RadioGroup = findViewById(R.id.radio_grp)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.private_box -> {
                    publicBox.isChecked = false
                    friendsBox.isChecked = false
                }
                R.id.public_box -> {
                    privateBox.isChecked = false
                    friendsBox.isChecked = false
                }
                R.id.friends_box -> {
                    privateBox.isChecked = false
                    publicBox.isChecked = false
                }
            }
        }

        imageButton.setOnClickListener {
            Log.d("ImageButton", "Clicked!")
            imageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        addPostButton.setOnClickListener {
            if (radioGroup.checkedRadioButtonId == -1 || titleEditText.text.isNullOrBlank() || descEditText.text.isNullOrBlank() || selectedImageUri == null) {
                // Afficher une alerte si l'un des champs est vide
                showAlert("Veuillez remplir tous les champs !")
            } else {
                val postState = when (radioGroup.checkedRadioButtonId) {
                    R.id.private_box -> PostState.PRIVATE
                    R.id.public_box -> PostState.PUBLIC
                    R.id.friends_box -> PostState.FRIENDS_ONLY
                    else -> PostState.PRIVATE
                }

                val title = titleEditText.text.toString()

                // Vérifier si le titre commence par une majuscule
                if (!title[0].isUpperCase()) {
                    showAlert("Le titre doit commencer par une majuscule!")
                } else {
                    val desc = descEditText.text.toString()

                    val imagePart = selectedImageUri?.toMultipartBody(this, "image")

                    if (imagePart != null) {
                        viewModel.addPost(title, desc, postState, imagePart)
                    } else {

                    }
                }
            }
        }

    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    }



class AddPostViewModel : ViewModel() {
    private val postApi: PostApi = RetrofitClient().getRetroClinetInstance().create(PostApi::class.java)

    fun addPost(title: String, desc: String, state: PostState, image: MultipartBody.Part) {
        Log.i(TAG, "ajout")

        // Create RequestBody instances for textual parameters
        val titleRequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descRequestBody = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryRequestBody =
            "".toRequestBody("text/plain".toMediaTypeOrNull()) // You mentioned category is always empty
        val dateRequestBody = "".toRequestBody("text/plain".toMediaTypeOrNull())
        val stateRequestBody = state.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        Log.i(TAG,title+desc+state);

        val call = postApi.addpost(image, titleRequestBody, descRequestBody, categoryRequestBody, dateRequestBody, stateRequestBody)
        call.enqueue(object : Callback<PostApi.PostResponse> {
            override fun onResponse(call: Call<PostApi.PostResponse>, response: Response<PostApi.PostResponse>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "Post added successfully")
                    val addedPost = response.body()?.post
                    // Handle the added post
                } else {
                    Log.e(TAG, "Failed to add post: ${response.raw()}")
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<PostApi.PostResponse>, t: Throwable) {
                Log.e(TAG, "Failed to add post", t)
                // Handle the failure
            }
        })
    }
}
