package com.cwnextgen.letternumberchase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cwnextgen.letternumberchase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val LETTER_RANGE = 'A'..'Z'
    private val NUMBER_RANGE = 0..5000

    private var isABCMode = true // Initially in ABC mode
    private var currentLetter: Char = LETTER_RANGE.random() // Starting letter
    private var currentNumber: Int = NUMBER_RANGE.random() // Starting number
    private var currentOptions: List<String> = generateOptions()

    private lateinit var optionsAdapter: OptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        optionsAdapter = OptionsAdapter { selectedOption ->
            onOptionSelected(selectedOption)
        }

        binding.rvOptions.apply {
            adapter = optionsAdapter
        }

        updateContent()
    }

    fun onSwitchButtonClick(view: View) {
        isABCMode = !isABCMode
        updateContent()

        val buttonText = if (isABCMode) "Switch to Numbers" else "Switch to Letters"
        binding.btnSwitch.text = buttonText
    }

    fun onOptionButtonClick(view: View) {
        // No action needed here since the options are handled in OptionsAdapter
    }

    fun onSkipButtonClick(view: View) {
        updateContent()
    }

    fun onSettingsButtonClick(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun generateOptions(): List<String> {
        val options = mutableListOf<String>()

        if (isABCMode) {
            currentLetter = LETTER_RANGE.random()
            options.add(currentLetter.toString())
        } else {
            currentNumber = NUMBER_RANGE.random()
            options.add(currentNumber.toString())
        }

        // Generate a list of available options for incorrect answers
        val availableOptions = if (isABCMode) {
            LETTER_RANGE.filter { it != currentLetter }.map { it.toString() }
        } else {
            NUMBER_RANGE.filter { it != currentNumber }.map { it.toString() }
        }

        // Shuffle the available options and pick the first 3 to create incorrect options
        val incorrectOptions = availableOptions.shuffled().take(3)
        options.addAll(incorrectOptions)

        return options.shuffled()
    }

    private fun updateContent() {
        currentOptions = generateOptions()
        optionsAdapter.updateOptions(currentOptions)

        if (isABCMode) {
            binding.tvLetter.text = currentLetter.toString()
        } else {
            binding.tvLetter.text = currentNumber.toString()
        }
    }

    private fun onOptionSelected(selectedOption: String) {
        if ((isABCMode && selectedOption == currentLetter.toString()) || (!isABCMode && selectedOption == currentNumber.toString())) {
            showToast("Hurrah!")
            updateContent()

        } else {
            showToast("Alas...")
            val correctIndex = currentOptions.indexOf(
                if (isABCMode) currentLetter.toString()
                else currentNumber.toString()
            )
            optionsAdapter.highlightCorrectOption(correctIndex)
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}