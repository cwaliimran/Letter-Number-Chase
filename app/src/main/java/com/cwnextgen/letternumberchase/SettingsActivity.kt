package com.cwnextgen.letternumberchase

import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.cwnextgen.letternumberchase.databinding.ActivitySettingsBinding
import com.cwnextgen.letternumberchase.models.LettersRange
import com.cwnextgen.letternumberchase.models.NumbersRange
import com.network.base.BaseActivity
import com.network.utils.AppClass
import com.network.utils.AppConstants

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate() {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //numbers range
        var numbersRange =
            AppClass.sharedPref.getObject(AppConstants.NUMBERS_RANGE, NumbersRange::class.java)
                ?: NumbersRange(0, 100)
        binding.textViewNumberRange.text = "${numbersRange.start} - ${numbersRange.end}"
        binding.seekBarNumber.progress = numbersRange.end

        //letters range
        var lettersRange =
            AppClass.sharedPref.getObject(AppConstants.LETTERS_RANGE, LettersRange::class.java)
                ?: LettersRange('A'.code, 'Z'.code)
        val displayedStart = lettersRange.start.toChar()
        val displayedEnd = lettersRange.end.toChar()
        binding.textViewLetterRange.text = "$displayedStart - $displayedEnd"
        binding.seekBarLetter.progress = positionInAlphabet(displayedEnd)

        //options range
        var maxOptions = AppClass.sharedPref.getInt(AppConstants.MAX_OPTIONS, 3) // Offset by -1
        binding.textViewOptionsRange.text = "${maxOptions + 1}" // Offset by +1
        binding.seekBarOptions.progress = maxOptions


        //OPTIONS
        val radioGroup = binding.radioGroup
        val selectedOption = AppClass.sharedPref.getInt(AppConstants.MATCH_TYPE, 1)
        // Set the selected radio button based on the loaded value
        when (selectedOption) {
            1 -> radioGroup.check(R.id.radioOption1)
            2 -> radioGroup.check(R.id.radioOption2)
            3 -> radioGroup.check(R.id.radioOption3)
        }


        binding.randomOptions.isChecked =
            AppClass.sharedPref.getBoolean(AppConstants.RANDOM_MULTIPLE_CHOICE)

    }

    override fun clicks() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Handle radio button selection changes
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedValue = when (checkedId) {
                R.id.radioOption1 -> 1
                R.id.radioOption2 -> 2
                R.id.radioOption3 -> 3
                else -> 0 // Default value when none selected
            }

            AppClass.sharedPref.storeInt(AppConstants.MATCH_TYPE, selectedValue)
        }

        binding.seekBarLetter.setOnSeekBarChangeListener(
            createLetterSeekBarChangeListener(
                binding.textViewLetterRange, 'A'.code, 'Z'.code
            )
        )

        binding.seekBarNumber.setOnSeekBarChangeListener(
            createNumberSeekBarChangeListener(
                binding.textViewNumberRange, 0, 5000
            )
        )
        binding.count10.setOnClickListener {
            binding.textViewNumberRange.text = "0 - 10"
            binding.seekBarNumber.progress = 10
            AppClass.sharedPref.storeObject(
                AppConstants.NUMBERS_RANGE, NumbersRange(0, 10)
            )
        }

        binding.count50.setOnClickListener {
            binding.textViewNumberRange.text = "0 - 50"
            binding.seekBarNumber.progress = 50
            AppClass.sharedPref.storeObject(
                AppConstants.NUMBERS_RANGE, NumbersRange(0, 50)
            )
        }

        binding.count100.setOnClickListener {
            binding.textViewNumberRange.text = "0 - 100"
            binding.seekBarNumber.progress = 100
            AppClass.sharedPref.storeObject(
                AppConstants.NUMBERS_RANGE, NumbersRange(0, 100)
            )
        }

        /*    binding.add1.setOnClickListener {
                binding.textViewNumberRange.text = "0 - 100"
                binding.seekBarNumber.progress = 100
                AppClass.sharedPref.storeObject(
                    AppConstants.NUMBERS_RANGE, NumbersRange(0, 100)
                )
            }

            binding.minus1.setOnClickListener {
                binding.textViewNumberRange.text = "0 - 100"
                binding.seekBarNumber.progress = 100
                AppClass.sharedPref.storeObject(
                    AppConstants.NUMBERS_RANGE, NumbersRange(0, 100)
                )
            }*/

        binding.btnResetSettings.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.MATCH_TYPE, 1)
            AppClass.sharedPref.storeObject(
                AppConstants.NUMBERS_RANGE, NumbersRange(0, 100)
            )
            AppClass.sharedPref.storeObject(
                AppConstants.LETTERS_RANGE, LettersRange('A'.code, 'Z'.code)
            )
            AppClass.sharedPref.storeInt(
                AppConstants.MAX_OPTIONS, 4
            )

            Toast.makeText(this, "Settings reset.", Toast.LENGTH_SHORT).show()
            finish()
        }


        binding.seekBarOptions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Display the progress with the offset
                binding.textViewOptionsRange.text = "${progress + 1}" // Offset by +1
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Save the progress with the offset
                val rangeEnd = seekBar?.progress!!  // Offset by +1
                AppClass.sharedPref.storeInt(AppConstants.MAX_OPTIONS, rangeEnd)
            }
        })

        binding.randomOptions.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            AppClass.sharedPref.storeBoolean(AppConstants.RANDOM_MULTIPLE_CHOICE, b)
        }
    }

    private fun createLetterSeekBarChangeListener(
        textView: TextView, minOffset: Int, maxOffset: Int
    ): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // This method is not used for this purpose
                //it auto shows ABC because range is passed from A..Z
                val rangeStart = minOffset
                val rangeEnd = minOffset + seekBar?.progress!!

                val displayedStart = rangeStart.toChar()
                val displayedEnd = rangeEnd.toChar()

                textView.text = "$displayedStart - $displayedEnd"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // This method is not used for this purpose
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val rangeStart = minOffset
                val rangeEnd = minOffset + seekBar?.progress!!

                val displayedStart = rangeStart.toChar()
                val displayedEnd = rangeEnd.toChar()

                AppClass.sharedPref.storeObject(
                    AppConstants.LETTERS_RANGE, LettersRange(rangeStart, rangeEnd)
                )
            }
        }
    }

    private fun createNumberSeekBarChangeListener(
        textView: TextView, minOffset: Int, maxOffset: Int
    ): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // This method is not used for this purpose
                val rangeStart = minOffset
                val rangeEnd = minOffset + seekBar?.progress!!

                textView.text = "$rangeStart - $rangeEnd"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // This method is not used for this purpose
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val rangeStart = minOffset
                val rangeEnd = minOffset + seekBar?.progress!!

                AppClass.sharedPref.storeObject(
                    AppConstants.NUMBERS_RANGE, NumbersRange(rangeStart, rangeEnd)
                )
            }
        }
    }


    fun <T : Char> positionInAlphabet(character: T): Int {
        val lowercaseChar = character.lowercaseChar()
        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        val index = alphabet.indexOf(lowercaseChar)

        return if (index != -1) {
            index + 1 // Adding 1 to make the position 1-based
        } else {
            -1 // Character not found in the alphabet
        }
    }
}