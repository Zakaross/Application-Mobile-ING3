package com.example.convertion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ibm.icu.text.RuleBasedNumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var etNumber: EditText? = null
    private var tvResultFrench: TextView? = null
    private var tvResultEnglish: TextView? = null
    private var tvResultMaternal: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lier les éléments XML
        etNumber = findViewById<EditText>(R.id.et_number)
        tvResultFrench = findViewById<TextView>(R.id.tv_result_french)
        tvResultEnglish = findViewById<TextView>(R.id.tv_result_english)
        tvResultMaternal = findViewById<TextView>(R.id.tv_result_maternal)
        val btnConvert = findViewById<Button>(R.id.btn_convert)

        // Action du bouton Convertir
        btnConvert.setOnClickListener {
            val input = etNumber?.text.toString().trim() // Supprimer les espaces
            if (input.isNotEmpty()) {
                val cleanedInput = input.toIntOrNull() ?: 0 // Nettoyer l'entrée : gérer les "00", "000" etc.

                if (cleanedInput in 0..10_000_000) {
                    // Convertir et afficher les résultats si le nombre est valide
                    tvResultFrench?.text = "Français : " + convertToFrench(cleanedInput)
                    tvResultEnglish?.text = "English : " + convertToEnglish(cleanedInput)
                    tvResultMaternal?.text = "Langue Maternelle : " + convertToMaternal(cleanedInput)
                } else {
                    // Toast d'erreur pour un nombre hors plage
                    showToast("Erreur : Entrez un nombre entre 0 et 10 000 000 !")
                }
            } else {
                // Toast d'erreur pour une saisie vide
                showToast("Erreur : Veuillez entrer un chiffre valide !")
            }
        }
    }

    // Conversion en français
    private fun convertToFrench(number: Int): String {
        val rbnf = RuleBasedNumberFormat(Locale.FRENCH, RuleBasedNumberFormat.SPELLOUT)
        return rbnf.format(number.toLong())
    }

    // Conversion en anglais
    private fun convertToEnglish(number: Int): String {
        val rbnf = RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.SPELLOUT)
        return rbnf.format(number.toLong())
    }

    // Conversion en langue maternelle (arabe phonétique étendu jusqu'à 10 millions)
    private fun convertToMaternal(number: Int): String {
        return when {
            number == 0 -> "sifr" // Gestion du zéro
            number in 1..10 -> { // Gérer 1 à 10
                val numbers = mapOf(
                    1 to "wahid",
                    2 to "ithnan",
                    3 to "thalatha",
                    4 to "arba'a",
                    5 to "khamsa",
                    6 to "sitta",
                    7 to "sab'a",
                    8 to "thamaniya",
                    9 to "tis'a",
                    10 to "ashara"
                )
                numbers[number] ?: "Non supporté"
            }
            number in 11..19 -> { // Gérer 11 à 19
                val base = mapOf(
                    11 to "ahada 'ashar",
                    12 to "ithna 'ashar",
                    13 to "thalatha 'ashar",
                    14 to "arba'a 'ashar",
                    15 to "khamsa 'ashar",
                    16 to "sitta 'ashar",
                    17 to "sab'a 'ashar",
                    18 to "thamaniya 'ashar",
                    19 to "tis'a 'ashar"
                )
                base[number] ?: "Non supporté"
            }
            number in 20..99 -> { // Gérer 20 à 99
                val tens = number / 10
                val units = number % 10
                val tensText = when (tens) {
                    2 -> "ishreen"
                    3 -> "thalathin"
                    4 -> "arba'in"
                    5 -> "khamsin"
                    6 -> "sittin"
                    7 -> "sab'in"
                    8 -> "thamanin"
                    9 -> "tis'in"
                    else -> ""
                }
                val unitsText = if (units > 0) " wa ${convertToMaternal(units)}" else ""
                "$tensText$unitsText"
            }
            number in 100..999 -> { // Gérer 100 à 999
                val hundreds = number / 100
                val remainder = number % 100
                val hundredsText = when (hundreds) {
                    1 -> "mi'a"
                    2 -> "mi'atayn"
                    else -> "mi'at ${convertToMaternal(hundreds)}"
                }
                val remainderText = if (remainder > 0) " ${convertToMaternal(remainder)}" else ""
                "$hundredsText$remainderText"
            }
            number in 1000..999999 -> { // Gérer 1 000 à 999 999
                val thousands = number / 1000
                val remainder = number % 1000
                val thousandsText = if (thousands == 1) "alf" else "${convertToMaternal(thousands)} alf"
                val remainderText = if (remainder > 0) " ${convertToMaternal(remainder)}" else ""
                "$thousandsText$remainderText"
            }
            number in 1_000_000..10_000_000 -> { // Gérer 1 000 000 à 10 000 000
                val millions = number / 1_000_000
                val remainder = number % 1_000_000
                val millionsText = if (millions == 1) "milyon" else "${convertToMaternal(millions)} milyon"
                val remainderText = if (remainder > 0) " ${convertToMaternal(remainder)}" else ""
                "$millionsText$remainderText"
            }
            else -> "Non supporté" // Gérer au-delà de 10 000 000 ou cas non pris en charge
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
