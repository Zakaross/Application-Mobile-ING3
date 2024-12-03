package com.calculatrice

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private var tvDisplay: TextView? = null
    private var tvHistory: TextView? = null
    private var memory = 0.0 // Memory
    private val history: MutableList<String> = ArrayList() // History

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById<TextView>(R.id.tvDisplay)
        tvHistory = findViewById<TextView>(R.id.tvHistory)

        // Set hint for tvDisplay
        tvDisplay?.hint = "0"

        // Memory
        findViewById<View>(R.id.btnMC).setOnClickListener { memory = 0.0 }
        findViewById<View>(R.id.btnMR).setOnClickListener { tvDisplay?.setText(memory.toString()) }
        findViewById<View>(R.id.btnMPlus).setOnClickListener { updateMemory("+") }
        findViewById<View>(R.id.btnMMinus).setOnClickListener { updateMemory("-") }

        // Operators
        findViewById<View>(R.id.btnAdd).setOnClickListener { appendOperator("+") }
        findViewById<View>(R.id.btnSubtract).setOnClickListener { appendOperator("-") }
        findViewById<View>(R.id.btnMultiply).setOnClickListener { appendOperator("*") }
        findViewById<View>(R.id.btnDivide).setOnClickListener { appendOperator("/") }

        // Scientific functions
        findViewById<View>(R.id.btnPi).setOnClickListener { tvDisplay?.setText(Math.PI.toString()) }
        findViewById<View>(R.id.btnE).setOnClickListener { tvDisplay?.setText(Math.E.toString()) }
        findViewById<View>(R.id.btnSqrt).setOnClickListener { calculateUnary { a: Double -> sqrt(a) } }
        findViewById<View>(R.id.btnLog).setOnClickListener { calculateUnary { a: Double -> log10(a) } }
        findViewById<View>(R.id.btnLn).setOnClickListener { calculateUnary { a: Double -> ln(a) } }

        // Calculate result
        findViewById<View>(R.id.btnEqual).setOnClickListener { calculateResult() }

        // Clear display
        findViewById<View>(R.id.btnClear).setOnClickListener { clearDisplay() }

        // Toggle sign (+/-)
        findViewById<View>(R.id.btnPlusMinus).setOnClickListener { toggleSign() }

        // Number buttons
        val numberButtons = intArrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot
        )

        for (id in numberButtons) {
            findViewById<View>(id).setOnClickListener { v: View -> appendNumber(v) }
        }

        // Handle parentheses
        findViewById<View>(R.id.btnOpenParen).setOnClickListener { appendParenthesis("(") }
        findViewById<View>(R.id.btnCloseParen).setOnClickListener { appendParenthesis(")") }
    }

    // Display numbers
    private fun appendNumber(v: View) {
        val button = v as Button
        val value = button.text.toString()
        val current = tvDisplay!!.text.toString()

        // If the display is currently "0" or "Error", replace with the new value
        if (current == "0" || current == "Error") {
            tvDisplay!!.text = value
        } else {
            tvDisplay!!.text = current + value
        }

        // If the display is empty, reset the hint
        if (tvDisplay!!.text.isEmpty()) {
            tvDisplay!!.hint = "0"
        }
    }


    // Add operator
    private fun appendOperator(operator: String) {
        val current = tvDisplay!!.text.toString()
        if (current.isNotEmpty() && !"+-*/^".contains(current.last())) {
            tvDisplay!!.append(operator)
        }
    }

    // Toggle sign (+/-)
    private fun toggleSign() {
        try {
            val value = tvDisplay!!.text.toString().toDouble()
            tvDisplay!!.text = if (value < 0) value.toString().removePrefix("-") else "-$value"
        } catch (e: NumberFormatException) {
            tvDisplay!!.text = "Error"
        }
    }

    // Update memory
    private fun updateMemory(operation: String) {
        try {
            val currentValue = tvDisplay!!.text.toString().toDouble()
            memory = if (operation == "+") memory + currentValue else memory - currentValue
        } catch (e: NumberFormatException) {
            tvDisplay!!.text = "Error"
        }
    }

    // Calculate unary functions (sqrt, log, ln)
    private fun calculateUnary(operation: UnaryOperation) {
        try {
            val value = tvDisplay!!.text.toString().toDouble()
            tvDisplay!!.text = operation.apply(value).toString()
        } catch (e: NumberFormatException) {
            tvDisplay!!.text = "Error"
        }
    }

    // Calculate result of expression
    private fun calculateResult() {
        try {
            val expression = tvDisplay!!.text.toString()
            val result = evaluateExpression(expression)
            tvDisplay!!.text = result.toString()
            updateHistory(expression, result.toString())
        } catch (e: Exception) {
            tvDisplay!!.text = "Error"
        }
    }

    // Clear display
    private fun clearDisplay() {
        // Reset the display text to empty
        tvDisplay!!.text = ""

        // Set the hint when the display is cleared
        tvDisplay!!.hint = "0"

        // Clear history
        history.clear()
        tvHistory!!.text = "Historique"
    }


    // Update history
    private fun updateHistory(expression: String, result: String) {
        history.add("$expression = $result")
        tvHistory!!.text = history.joinToString("\n")
    }

    // Evaluate expression
    private fun evaluateExpression(expression: String): Double {
        val expr = expression.replace("^", "**") // Replace ^ with ** for exp4j
        val e = ExpressionBuilder(expr).build()
        return e.evaluate()
    }

    // Handle parentheses
    private fun appendParenthesis(paren: String) {
        val current = tvDisplay!!.text.toString()

        if (paren == "(") {
            // You can open a parenthesis if the expression is empty, if the last character is an operator,
            // or after a number or a closing parenthesis
            if (current.isEmpty() || "+-*/^(".contains(current.last()) || current.last().isDigit() || current.last() == ')') {
                tvDisplay!!.append(paren)
            }
        } else if (paren == ")") {
            // You can close a parenthesis if the expression contains an opening parenthesis and the last character is a digit or closing parenthesis
            if (current.contains("(") && !current.contains(")") || current.last().isDigit() || current.last() == ')') {
                tvDisplay!!.append(paren)
            }
        }
    }

    // Interface for unary operations
    internal fun interface UnaryOperation {
        fun apply(value: Double): Double
    }
}
