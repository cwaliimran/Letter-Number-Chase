package com.cwnextgen.letternumberchase

import android.widget.SeekBar
import android.widget.TextView
import com.cwnextgen.letternumberchase.databinding.ActivitySettingsBinding
import com.network.base.BaseActivity

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate() {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.acTitle(getString(R.string.settings))

    }

    override fun clicks() {

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
    }

    private fun createLetterSeekBarChangeListener(
        textView: TextView,
        minOffset: Int,
        maxOffset: Int
    ): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // This method is not used for this purpose
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

            }
        }
    }

    private fun createNumberSeekBarChangeListener(
        textView: TextView,
        minOffset: Int,
        maxOffset: Int
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

            }
        }
    }

}