package com.cwnextgen.letternumberchase

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import com.cwnextgen.letternumberchase.databinding.ActivitySettingsBinding
import com.cwnextgen.letternumberchase.models.LettersRange
import com.cwnextgen.letternumberchase.models.NumbersRange
import com.cwnextgen.letternumberchase.utils.GlobalUtils
import com.cwnextgen.letternumberchase.utils.GlobalUtils.setCustomFontSpecificView
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

        //font size percent
        val fontPercent = AppClass.sharedPref.getInt(AppConstants.FONT_PERCENT, 0)
        binding.seekBarFontSize.progress = fontPercent

        //OPTIONS
        val radioGroup = binding.radioGroup
        val selectedOption = AppClass.sharedPref.getInt(AppConstants.MATCH_TYPE, 1)
        // Set the selected radio button based on the loaded value
        when (selectedOption) {
            1 -> radioGroup.check(R.id.radioOption1)
            2 -> radioGroup.check(R.id.radioOption2)
            3 -> radioGroup.check(R.id.radioOption3)
        }
        //font OPTIONS
        val radioGroupFonts = binding.radioGroupFonts
        val selectedOptionFont =
            AppClass.sharedPref.getString(AppConstants.FONT_TYPE, "palamecia_titling")
        // Set the selected radio button based on the loaded value
        when (selectedOptionFont) {
            "digitalt" -> radioGroupFonts.check(R.id.fontOption1)
            "palamecia_titling" -> radioGroupFonts.check(R.id.fontOption2)
            "palanquin_dark" -> radioGroupFonts.check(R.id.fontOption3)
            "roboto_medium" -> radioGroupFonts.check(R.id.fontOption4)
            "montserrat_semibold" -> radioGroupFonts.check(R.id.fontOption5)
            "architects_daughter" -> radioGroupFonts.check(R.id.fontOption6)
            "baloo_tamma" -> radioGroupFonts.check(R.id.fontOption7)
        }
        setFons()

        binding.randomOptions.isChecked =
            AppClass.sharedPref.getBoolean(AppConstants.RANDOM_MULTIPLE_CHOICE)

        binding.swParty.isChecked =
            AppClass.sharedPref.getBooleanDefaultTrue(AppConstants.CELEBRATION)

        //options column span count
        val count = AppClass.sharedPref.getInt(AppConstants.OPTIONS_COLUMN_COUNT, 3)
        binding.tvColCount.text = count.toString()


        //tables count
        val tableCount = AppClass.sharedPref.getInt(AppConstants.TABLE_COUNT, 10)
        binding.textViewTableRange.text = tableCount.toString()


    }

    private fun setFons() {
        val selectedOptionFont =
            AppClass.sharedPref.getString(AppConstants.FONT_TYPE, "palamecia_titling")
        GlobalUtils.setCustomFont(binding.root, this, selectedOptionFont)

        setCustomFontSpecificView(binding.fontOption1, "digitalt")
        setCustomFontSpecificView(binding.fontOption2, "palamecia_titling")
        setCustomFontSpecificView(binding.fontOption3, "palanquin_dark")
        setCustomFontSpecificView(binding.fontOption4, "roboto_medium")
        setCustomFontSpecificView(binding.fontOption5, "montserrat_semibold")
        setCustomFontSpecificView(binding.fontOption6, "architects_daughter")
        setCustomFontSpecificView(binding.fontOption7, "baloo_tamma")

    }

    override fun clicks() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Handle game mode button selection changes
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedValue = when (checkedId) {
                R.id.radioOption1 -> 1
                R.id.radioOption2 -> 2
                R.id.radioOption3 -> 3
                else -> 0 // Default value when none selected
            }

            AppClass.sharedPref.storeInt(AppConstants.MATCH_TYPE, selectedValue)
        }
        // Handle fonts button selection changes
        binding.radioGroupFonts.setOnCheckedChangeListener { _, checkedId ->
            val selectedValue = when (checkedId) {
                R.id.fontOption1 -> "digitalt"
                R.id.fontOption2 -> "palamecia_titling"
                R.id.fontOption3 -> "palanquin_dark"
                R.id.fontOption4 -> "roboto_medium"
                R.id.fontOption5 -> "montserrat_semibold"
                R.id.fontOption6 -> "architects_daughter"
                R.id.fontOption7 -> "baloo_tamma"
                else -> "digitalt"
            }

            AppClass.sharedPref.storeString(AppConstants.FONT_TYPE, selectedValue)
            setFons()
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


        binding.tableCount5.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.TABLE_COUNT, 5)
            binding.textViewTableRange.text = "5"
        }

        binding.tableCount10.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.TABLE_COUNT, 10)
            binding.textViewTableRange.text = "10"
        }

        binding.tableCount20.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.TABLE_COUNT, 20)
            binding.textViewTableRange.text = "20"
        }


        binding.btnResetSettings.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.MATCH_TYPE, 1)
            AppClass.sharedPref.storeObject(
                AppConstants.NUMBERS_RANGE, NumbersRange(0, 100)
            )
            AppClass.sharedPref.storeObject(
                AppConstants.LETTERS_RANGE, LettersRange('A'.code, 'Z'.code)
            )

            AppClass.sharedPref.storeInt(AppConstants.TABLE_COUNT, 10)

            AppClass.sharedPref.storeInt(
                AppConstants.MAX_OPTIONS, 4
            )
            AppClass.sharedPref.storeBoolean(AppConstants.RANDOM_MULTIPLE_CHOICE, false)


            AppClass.sharedPref.storeInt(
                AppConstants.OPTIONS_COLUMN_COUNT, 3
            )
            AppClass.sharedPref.storeString(
                AppConstants.FONT_TYPE, "palamecia_titling"
            )


            AppClass.sharedPref.storeInt(
                AppConstants.FONT_PERCENT, 0
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
                val rangeEnd = seekBar?.progress!!
                AppClass.sharedPref.storeInt(AppConstants.MAX_OPTIONS, rangeEnd)
            }
        })

        binding.randomOptions.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            AppClass.sharedPref.storeBoolean(AppConstants.RANDOM_MULTIPLE_CHOICE, b)
        }
        binding.swParty.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            AppClass.sharedPref.storeBoolean(AppConstants.CELEBRATION, b)
        }

        binding.seekBarFontSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val rangeEnd = seekBar?.progress!!
                AppClass.sharedPref.storeInt(AppConstants.FONT_PERCENT, rangeEnd)
            }

        })

        binding.spanCount1.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.OPTIONS_COLUMN_COUNT, 1)
            binding.tvColCount.text = "1"
        }
        binding.spanCount2.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.OPTIONS_COLUMN_COUNT, 2)
            binding.tvColCount.text = "2"
        }
        binding.spanCount3.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.OPTIONS_COLUMN_COUNT, 3)
            binding.tvColCount.text = "3"
        }
        binding.spanCount4.setOnClickListener {
            AppClass.sharedPref.storeInt(AppConstants.OPTIONS_COLUMN_COUNT, 4)
            binding.tvColCount.text = "4"
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