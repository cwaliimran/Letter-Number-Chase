package com.cwnextgen.letternumberchase

import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.cwnextgen.letternumberchase.databinding.ActivityMainBinding
import com.cwnextgen.letternumberchase.dialogs.answerDialog
import com.cwnextgen.letternumberchase.dialogs.coinsInfoDialog
import com.cwnextgen.letternumberchase.models.LettersRange
import com.cwnextgen.letternumberchase.models.NumbersRange
import com.cwnextgen.letternumberchase.utils.GlobalUtils.increaseFontSizeSingleView
import com.cwnextgen.letternumberchase.utils.ShowPartyGlitters.showParty
import com.cwnextgen.letternumberchase.utils.textChangeWithAnimation
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.network.base.BaseActivity
import com.network.utils.AppClass
import com.network.utils.AppConstants

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

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
    var correctOption = ""
    private var mRewardedAd: RewardedAd? = null

    private var isReward = false
    private var continuousCorrectAnswers = 0

    override fun onCreate() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (bundle?.getString(AppConstants.BUNDLE)) {
            "lettersGame" -> {
                isABCMode = true
            }

            "numbersGame" -> {
                isABCMode = false
            }

            else -> {}
        }

        updateGameModeView()


        if (BuildConfig.FLAVOR != "adfree") {
            //load ad
            MobileAds.initialize(this) {}
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }
        //set fonts
        maxOptions = AppClass.sharedPref.getInt(AppConstants.MAX_OPTIONS, 4)
        optionsAdapter = OptionsAdapter { selectedOption ->
            onOptionSelected(selectedOption)
        }


    }

    override fun clicks() {
        binding.tvCoins.setOnClickListener {
            coinsInfoDialog()
        }
        binding.tvDiamonds.setOnClickListener {
            coinsInfoDialog()
        }

        binding.btnHome.setOnClickListener {
            finish()
        }
        binding.tvRevealAnswer.setOnClickListener {
            showRewardedAd()
        }

        binding.btnSwitch.setOnClickListener {
            isABCMode = !isABCMode
            updateContent()

            updateGameModeView()
        }


        binding.btnSkip.setOnClickListener {
            updateContent()

        }

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)

            // Define the animation options (fade-in and fade-out)
            val animationOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, Pair.create(
                    binding.btnSettings, ViewCompat.getTransitionName(binding.btnSettings)!!
                )
                // You can add more pairs for other views if needed
            )

            // Start the activity with animation options
            startActivity(intent, animationOptions.toBundle())

        }

    }

    private fun updateGameModeView() {
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

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(this,
            getString(R.string.rewarded_ad_unit_id),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mRewardedAd = rewardedAd
                    rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Ad was dismissed.")
                            mRewardedAd = null
                            giveReward()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.d(TAG, "Ad failed to show.")
                            mRewardedAd = null
                        }

                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "Ad showed fullscreen content.")
                            mRewardedAd = null
                        }
                    }
                }
            })
    }

    private fun showRewardedAd() {
        if (mRewardedAd != null) {
            mRewardedAd?.show(this) {
                Log.d(TAG, "User earned the reward.")
            }
        } else {
            // Ad was not loaded yet, show a toast message.
            Toast.makeText(this, getString(R.string.ad_not_load_msg), Toast.LENGTH_LONG).show()

            // Or start loading a new ad.
            loadRewardedAd()
        }
    }


    private fun giveReward() {
        //show answer
        isReward = true
        answerDialog(correctOption)
        loadRewardedAd() // Load a new ad after the reward is given.
    }

    private fun generateOptions() {
        val options = mutableListOf<String>()

        //random multiple choice count enabled
        if (AppClass.sharedPref.getBoolean(AppConstants.RANDOM_MULTIPLE_CHOICE)) {
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

        //save the correct option
        correctOption = when (gameMode) {
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
        if (selectedOption == correctOption) {

            if (AppClass.sharedPref.getBooleanDefaultTrue(AppConstants.CELEBRATION)) {
                binding.viewKonfetti.showParty()
            }

            //update coins
            var count = AppClass.sharedPref.getLong(AppConstants.COINS) + 1
            binding.tvCoins.text = count.toString()
            AppClass.sharedPref.storeLong(AppConstants.COINS, count)
            continuousCorrectAnswers += 1
            if (continuousCorrectAnswers == 5) {
                continuousCorrectAnswers = 0
                //diamonds only increase when 5 correct options are made in a row
                var countDiamonds = AppClass.sharedPref.getLong(AppConstants.DIAMONDS) + 1
                binding.tvDiamonds.text = countDiamonds.toString()
                AppClass.sharedPref.storeLong(AppConstants.DIAMONDS, countDiamonds)
            }



            updateContent()
        } else {
            continuousCorrectAnswers = 0

            var count = AppClass.sharedPref.getLong(AppConstants.COINS) - 1
            binding.tvCoins.text = count.toString()
            AppClass.sharedPref.storeLong(AppConstants.COINS, count)


            val correctIndex = currentOptions.indexOf(correctOption)
            optionsAdapter.highlightCorrectOption(correctIndex)
        }
    }


    private fun updateContent() {
        generateOptions()

        if (isABCMode) {
            textChangeWithAnimation(binding.tvLetter, currentLetter.toString())
        } else {
            textChangeWithAnimation(binding.tvLetter, currentNumber.toString())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()

        if (isReward) {
            isReward = false
            return
        }

        binding.adView.resume()

        //options column span count
        val count = AppClass.sharedPref.getInt(AppConstants.OPTIONS_COLUMN_COUNT, 3)
        val layoutManagerGrid = GridLayoutManager(this, count)
        binding.rvOptions.apply {
            layoutManager = layoutManagerGrid
            adapter = optionsAdapter
        }


        //set fonts
        val selectedOptionFont =
            AppClass.sharedPref.getString(AppConstants.FONT_TYPE, "palamecia_titling")
        val fontResourceId = this.resources.getIdentifier(
            selectedOptionFont, "font", this.packageName
        )
        val customFont = ResourcesCompat.getFont(this, fontResourceId)
        binding.tvLetter.apply {
            typeface = customFont
            includeFontPadding = false
        }
        binding.tvMode.typeface = customFont
        binding.btnSwitch.typeface = customFont


        //set font size
        binding.tvLetter.setTextSize(
            TypedValue.COMPLEX_UNIT_SP, 120.toFloat()
        ) //reset to default so not increased every time in on resume
        binding.tvMode.setTextSize(
            TypedValue.COMPLEX_UNIT_SP, 20.toFloat()
        ) //reset to default so not increased every time in on resume
        val fontSize = AppClass.sharedPref.getInt(AppConstants.FONT_PERCENT, 0)
        Log.d("TAG", "onResume: " + fontSize)
        increaseFontSizeSingleView(binding.tvLetter, fontSize.toFloat())
        //add check so game mode title is not increased too much
        if (fontSize > 50) {
            increaseFontSizeSingleView(binding.tvMode, 20.toFloat())
        } else {
            increaseFontSizeSingleView(binding.tvMode, fontSize.toFloat())
        }


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


        //coins count
        binding.tvCoins.text = AppClass.sharedPref.getLong(AppConstants.COINS).toString()
        binding.tvDiamonds.text = AppClass.sharedPref.getLong(AppConstants.DIAMONDS).toString()

    }


    public override fun onPause() {
        binding.adView.pause()
        super.onPause()
    }

    public override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }
}