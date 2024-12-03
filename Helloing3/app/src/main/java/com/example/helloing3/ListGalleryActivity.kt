package com.example.helloing3

import android.os.Bundle
import android.widget.GridView
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ListGalleryActivity : AppCompatActivity() {

    private lateinit var galleryDatabaseHelper: GalleryDatabaseHelper
    private lateinit var gridView: GridView
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_gallery)

        galleryDatabaseHelper = GalleryDatabaseHelper(this)
        gridView = findViewById(R.id.gridView)

        // Charger les éléments de la galerie dans l'adaptateur
        loadGalleryItems()

        // Configurer un clic sur un élément pour le supprimer
        gridView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as GalleryItem

            // Créer une boîte de dialogue pour confirmer la suppression
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Voulez-vous vraiment supprimer cette image?")
                .setCancelable(false)
                .setPositiveButton("Oui") { dialog, _ ->
                    // Supprimer l'élément de la base de données
                    galleryDatabaseHelper.deleteGalleryItem(selectedItem.path)

                    // Mettre à jour la liste avec les éléments actualisés
                    loadGalleryItems()

                    dialog.dismiss()
                }
                .setNegativeButton("Non") { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create().show()
        }
    }

    // Charger les éléments de la galerie et mettre à jour l'adaptateur
    private fun loadGalleryItems() {
        val galleryItems = galleryDatabaseHelper.getAllItems()
        galleryAdapter = GalleryAdapter(this, galleryItems)
        gridView.adapter = galleryAdapter
    }
}
