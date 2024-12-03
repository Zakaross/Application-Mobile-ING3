package com.example.helloing3

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class GalleryAdapter(private val context: Context, private var galleryItems: List<GalleryItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return galleryItems.size
    }

    override fun getItem(position: Int): Any {
        return galleryItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)

        val imageView: ImageView = view.findViewById(R.id.imageView)


        val item = galleryItems[position]

        // Si l'élément est une image
        if (item.type == "image") {
            Picasso.get().load(item.path).into(imageView)
        }
        // Si l'élément est une vidéo
        else if (item.type == "video") {
            val videoUri = Uri.parse(item.path)
            val thumbnail = getVideoThumbnail(videoUri)
            if (thumbnail != null) {
                imageView.setImageBitmap(thumbnail)
            } else {
                // Affiche une image par défaut si la miniature n'a pas pu être générée
                imageView.setImageResource(R.drawable.ic_video_placeholder)
            }
        }

        return view
    }

    // Méthode pour obtenir la miniature d'une vidéo
    private fun getVideoThumbnail(uri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            // Extraire la première image de la vidéo (miniature)
            return retriever.getFrameAtTime(0)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return null
    }

    // Méthode pour supprimer un élément
    fun removeItem(item: GalleryItem) {
        galleryItems = galleryItems.filter { it != item }
        notifyDataSetChanged()
    }
}
