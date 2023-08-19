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
    private var gameMode = 1
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

    private fun generateOptions() {
        val options = mutableListOf<String>()

        //random multiple choice count enabled
        if (AppClass.sharedPref.getBoolean(AppConstants.RANDOM_MULTIPLE_CHOICE)){
            maxOptions = (1..12).random()
        }

        if (isABCMode) {
            currentLetter = lettersRange.random()
            val availableOptions = lettersRange.filter { it != currentLetter }.map { it.toString() }
            when (gameMode) {
                1 -> options.add(currentLetter.toString())
                2 -> options.add(if (currentLetter == 'Z') 'A'.toString() else (currentLetter + 1).toString())
                3 -> options.add(if (currentLetter == 'A') 'Z'.toString() else (currentLetter - 1).toString())
            }
            addIncorrectOptions(options, availableOptions)
        } else {
            currentNumber = numbersRange.random()
            val availableOptions = numbersRange.filter { it != currentNumber }.map { it.toString() }
            when (gameMode) {
                1 -> options.add(currentNumber.toString())
                2 -> options.add((currentNumber + 1).toString())
                3 -> options.add((currentNumber - 1).toString())
            }
            addIncorrectOptions(options, availableOptions)
        }

        currentOptions = options.shuffled().distinct()
        optionsAdapter.updateOptions(currentOptions)
    }

    private fun addIncorrectOptions(options: MutableList<String>, availableOptions: List<String>) {
        val maxOptionsMinusOne = maxOptions
        try {
            val incorrectOptions = availableOptions.shuffled().take(maxOptionsMinusOne)
            options.addAll(incorrectOptions)
        } catch (e: Exception) {
            // Handle the exception if necessary
        }
    }

    private fun onOptionSelected(selectedOption: String) {
        val correctOption = when (gameMode) {
            1 -> if (isABCMode) currentLetter.toString() else currentNumber.toString()
            2 -> if (isABCMode) {
                if (currentLetter == 'Z') 'A'.toString() else (currentLetter + 1).toString()
            } else {
                (currentNumber + 1).toString()
            }
            3 -> if (isABCMode) {
                if (currentLetter == 'A') 'Z'.toString() else (currentLetter - 1).toString()
            } else {
                (currentNumber - 1).toString()
            }
            else -> ""
        }

        if (selectedOption == correctOption) {
            showToast("Hurrah!")
            updateContent()
        } else {
            showToast("Alas...")
            val correctIndex = currentOptions.indexOf(correctOption)
            optionsAdapter.highlightCorrectOption(correctIndex)
        }
    }


    private fun updateContent() {
        generateOptions()

        if (isABCMode) {
            binding.tvLetter.text = currentLetter.toString()
        } else {
            binding.tvLetter.text = currentNumber.toString()
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

        //abc range
        lettersRangeModel =
            AppClass.sharedPref.getObject(AppConstants.LETTERS_RANGE, LettersRange()::class.java)
                ?: LettersRange('A'.code, 'Z'.code)
        lettersRange = lettersRangeModel.start.toChar()..lettersRangeModel.end.toChar()

        //max options
        maxOptions = AppClass.sharedPref.getInt(AppConstants.MAX_OPTIONS, 4)


        // Match play mode
        gameMode = AppClass.sharedPref.getInt(AppConstants.MATCH_TYPE, 1)

        when (gameMode) {
            1 -> {
                binding.tvMode.text = "Match Similar"
            }

            2 -> {
                binding.tvMode.text = "Match Next"
            }

            3 -> {
                binding.tvMode.text = "Match Previous"
            }
        }
        updateContent()

    }
}