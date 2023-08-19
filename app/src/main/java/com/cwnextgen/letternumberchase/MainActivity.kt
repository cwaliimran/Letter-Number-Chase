package com.cwnextgen.letternumberchase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import com.cwnextgen.letternumberchase.databinding.ActivityMainBinding
import com.cwnextgen.letternumberchase.models.LettersRange
import com.cwnextgen.letternumberchase.models.NumbersRange
import com.network.utils.AppClass
import com.network.utils.AppConstants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private lateinit var lettersRange: CharRange
    private lateinit var numbersRange: IntRange

    private var isABCMode = true // Initially in ABC mode

    private var currentLetter: Char = 'A' // Starting letter
    private var currentNumber: Int = 0 // Starting number
    private lateinit var currentOptions: List<String>

    private lateinit var optionsAdapter: OptionsAdapter
    private var numbersRangeModel = NumbersRange()
    private var lettersRangeModel = LettersRange()
    private var maxOptions = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        maxOptions = AppClass.sharedPref.getInt(AppConstants.MAX_OPTIONS, 4)
        optionsAdapter = OptionsAdapter { selectedOption ->
            onOptionSelected(selectedOption)
        }

        binding.rvOptions.apply {
            adapter = optionsAdapter
        }
    }

    fun onSwitchButtonClick(view: View) {
        isABCMode = !isABCMode
        updateContent()

        val buttonText = if (isABCMode) "Switch to Numbers" else "Switch to Letters"
        binding.btnSwitch.text = buttonText
        // Set the drawableEnd

        val drawableRes = if (isABCMode) R.drawable.baseline_123_24 else R.drawable.baseline_abc_24
        val drawable = ContextCompat.getDrawable(this, drawableRes)
        val drawableStart = ContextCompat.getDrawable(this, R.drawable.baseline_cruelty_free_24)
        binding.btnSwitch.setCompoundDrawablesWithIntrinsicBounds(
            drawableStart, null, drawable, null
        )

    }

    fun onOptionButtonClick(view: View) {
        // No action needed here since the options are handled in OptionsAdapter
    }

    fun onSkipButtonClick(view: View) {
        updateContent()
    }

    fun onSettingsButtonClick(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)

        // Define the animation options (fade-in and fade-out)
        val animationOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair.create(binding.btnSettings, ViewCompat.getTransitionName(binding.btnSettings)!!)
            // You can add more pairs for other views if needed
        )

        // Start the activity with animation options
        startActivity(intent, animationOptions.toBundle())

    }

    private fun generateOptions(): List<String> {
        val options = mutableListOf<String>()

        if (isABCMode) {
            currentLetter = lettersRange.random()
            options.add(currentLetter.toString())
        } else {
            currentNumber = numbersRange.random()
            options.add(currentNumber.toString())
        }

        // Generate a list of available options for incorrect answers
        val availableOptions = if (isABCMode) {
            lettersRange.filter { it != currentLetter }.map { it.toString() }
        } else {
            numbersRange.filter { it != currentNumber }.map { it.toString() }
        }

        // Shuffle the available options and pick the first 3 to create incorrect options
        try {
            val incorrectOptions = availableOptions.shuffled().take(maxOptions)
            options.addAll(incorrectOptions)
        } catch (e: Exception) {
        }

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

    override fun onResume() {
        super.onResume()
        //numbers range
        numbersRangeModel =
            AppClass.sharedPref.getObject(AppConstants.NUMBERS_RANGE, NumbersRange::class.java)
                ?: NumbersRange(0, 100)
        numbersRange = numbersRangeModel.start..numbersRangeModel.end
        currentNumber = numbersRange.random()

        //abc range
        lettersRangeModel =
            AppClass.sharedPref.getObject(AppConstants.LETTERS_RANGE, LettersRange()::class.java)
                ?: LettersRange('A'.code, 'Z'.code)
        lettersRange = lettersRangeModel.start.toChar()..lettersRangeModel.end.toChar()
        currentLetter = lettersRange.random()

        //max options
        maxOptions = AppClass.sharedPref.getInt(AppConstants.MAX_OPTIONS, 4)

        updateContent()

    }
}