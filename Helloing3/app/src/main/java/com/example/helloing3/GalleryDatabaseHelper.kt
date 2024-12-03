package com.example.helloing3

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GalleryDatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Créer la table pour stocker les images/vidéos
        db.execSQL(CREATE_TABLE_GALLERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Supprimer et recréer la table en cas de mise à jour du schéma
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GALLERY")
        onCreate(db)
    }

    // Ajouter un élément (image/vidéo) à la base de données
    fun addItem(path: String, type: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PATH, path)
            put(COLUMN_TYPE, type)
        }

        try {
            db.insert(TABLE_GALLERY, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    // Récupérer tous les éléments de la galerie
    fun getAllItems(): List<GalleryItem> {
        val galleryItems = mutableListOf<GalleryItem>()
        val db = readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.query(TABLE_GALLERY, null, null, null, null, null, null)

            cursor?.let {
                while (it.moveToNext()) {
                    val path = it.getString(it.getColumnIndex(COLUMN_PATH))
                    val type = it.getString(it.getColumnIndex(COLUMN_TYPE))
                    galleryItems.add(GalleryItem(path, type))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return galleryItems
    }

    // Supprimer un élément de la base de données
    fun deleteGalleryItem(path: String) {
        val db = writableDatabase
        try {
            db.delete(TABLE_GALLERY, "$COLUMN_PATH = ?", arrayOf(path))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    companion object {
        private const val DATABASE_NAME = "gallery.db"
        private const val DATABASE_VERSION = 1

        // Nom de la table et colonnes
        const val TABLE_GALLERY = "gallery"
        const val COLUMN_ID = "_id"
        const val COLUMN_PATH = "path"
        const val COLUMN_TYPE = "type"

        // Instruction de création de table
        private const val CREATE_TABLE_GALLERY = """
            CREATE TABLE $TABLE_GALLERY (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PATH TEXT NOT NULL,
                $COLUMN_TYPE TEXT NOT NULL
            );
        """
    }
}
