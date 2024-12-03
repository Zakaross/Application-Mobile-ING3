package com.example.helloing3

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddGalleryActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var dbHelper: GalleryDatabaseHelper
    private var photoURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gallery)

        // Vérifier et demander les permissions
        checkAndRequestPermissions()

        dbHelper = GalleryDatabaseHelper(this)
        imageView = findViewById(R.id.imageView)

        findViewById<Button>(R.id.btnCamera).setOnClickListener { openCamera() }
        findViewById<Button>(R.id.btnGallery).setOnClickListener { openGallery() }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Erreur lors de la création du fichier
                Toast.makeText(this, "Erreur lors de la création du fichier", Toast.LENGTH_SHORT).show()
                null
            }
            photoFile?.also {
                // Crée une URI pour l'image capturée et l'insert dans le MediaStore
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/uploads")
                }
                photoURI = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                photoURI?.let { uri ->
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(intent, REQUEST_CAMERA)
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    // L'image est automatiquement enregistrée dans MediaStore grâce à l'URI
                    photoURI?.let { uri ->
                        dbHelper.addItem(uri.toString(), "image")
                        imageView.setImageURI(uri)
                        Toast.makeText(this, "Image capturée et ajoutée avec succès!", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(this, "Erreur lors de la capture de l'image", Toast.LENGTH_SHORT).show()
                    }
                }

                REQUEST_GALLERY -> {
                    val selectedMedia: Uri? = data?.data
                    if (selectedMedia != null) {
                        // Vérifier le type MIME de l'image ou de la vidéo
                        val type = contentResolver.getType(selectedMedia)

                        // Vérifiez si le fichier est une image ou une vidéo
                        if (type != null) {
                            when {
                                type.startsWith("image") -> {
                                    val destinationUri = saveFileToPrivateStorage(selectedMedia)
                                    dbHelper.addItem(destinationUri.toString(), "image")
                                    imageView.setImageURI(destinationUri)
                                    Toast.makeText(this, "Image ajoutée avec succès!", Toast.LENGTH_SHORT).show()
                                }
                                type.startsWith("video") -> {
                                    val destinationUri = saveFileToPrivateStorage(selectedMedia)
                                    dbHelper.addItem(destinationUri.toString(), "video")
                                    imageView.setImageResource(R.drawable.ic_video_placeholder)  // Assurez-vous d'avoir une icône pour la vidéo
                                    Toast.makeText(this, "Vidéo ajoutée avec succès!", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(this, "Format non pris en charge", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Erreur de type de fichier", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Erreur lors de la sélection du média", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Méthode pour enregistrer un fichier dans un répertoire privé de l'application
    private fun saveFileToPrivateStorage(uri: Uri): Uri {
        val fileName = getFileNameFromUri(uri)

        // Créez un répertoire privé dans le stockage externe de l'application
        val privateDir = File(getExternalFilesDir(null), "uploads")
        if (!privateDir.exists()) {
            privateDir.mkdirs() // Créez le répertoire s'il n'existe pas
        }

        // Créez un fichier dans ce répertoire
        val destinationFile = File(privateDir, fileName)

        // Copiez le contenu du fichier sélectionné dans le nouveau fichier
        contentResolver.openInputStream(uri)?.use { inputStream ->
            destinationFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        // Retourne l'URI du fichier copié
        return Uri.fromFile(destinationFile)
    }

    // Méthode pour récupérer le nom du fichier à partir de son URI
    private fun getFileNameFromUri(uri: Uri): String {
        var fileName = ""
        val cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DISPLAY_NAME), null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                fileName = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            }
            it.close()
        }
        return fileName
    }

    // Vérifier et demander les permissions nécessaires
    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.CAMERA)
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            requestPermissions(permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission ${permissions[i]} refusée", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Créer un nom de fichier unique pour l'image
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"

        // Obtenez le répertoire de stockage pour les images
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: throw IOException("Could not access storage")

        // Créez le fichier image
        val image = File.createTempFile(
            imageFileName, /* préfixe */
            ".jpg",        /* suffixe */
            storageDir     /* répertoire */
        )

        // Retourne le fichier
        return image
    }

    companion object {
        private const val REQUEST_GALLERY = 1
        private const val REQUEST_CAMERA = 2
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
