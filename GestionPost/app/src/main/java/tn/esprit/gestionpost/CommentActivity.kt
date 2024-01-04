package tn.esprit.gestionpost

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Comment

class CommentActivity : AppCompatActivity() {

    // ... (Autres déclarations de variables)

    private lateinit var commentAdapter: CommentAdapter // Assurez-vous de créer cet adaptateur

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        // Initialiser le RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Utiliser votre adaptateur pour les commentaires
        commentAdapter = CommentAdapter() // Assurez-vous de créer cet adaptateur
        recyclerView.adapter = commentAdapter

        // Afficher des commentaires statiques (à remplacer par vos propres données)
        val staticComments = listOf(
            Comment("Utilisateur 1", "Ayouuub s work"),
            Comment("Utilisateur 2", "UHeyyyyyy.") ,
            Comment("Utilisateur 3", "woooow."),
        Comment("Utilisateur 4", "Un autre commentaire."),
        Comment("Utilisateur 5", "Un autre commentaire.")
            // ... Ajoutez d'autres commentaires statiques ici
        )

        commentAdapter.submitList(staticComments)

        // Gérer l'ajout d'un nouveau commentaire
        val newCommentTextView: TextView = findViewById(R.id.new_comment)
        val sendButton: ImageButton = findViewById(R.id.add_new_comment)

        sendButton.setOnClickListener {
            val newCommentText = newCommentTextView.text.toString()
            if (newCommentText.isNotEmpty()) {
                val newComment = Comment("Utilisateur actuel", newCommentText)
                commentAdapter.addComment(newComment)
                newCommentTextView.text = "" // Effacer le champ de commentaire
            }
        }
    }
}
