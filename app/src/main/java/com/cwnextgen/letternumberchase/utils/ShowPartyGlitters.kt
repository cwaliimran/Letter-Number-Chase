package com.cwnextgen.letternumberchase.utils

import android.view.View
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

object ShowPartyGlitters {

    fun KonfettiView.showParty() {
        this.visibility = View.VISIBLE
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 50, TimeUnit.MILLISECONDS).max(50)
        )
        this.start(party)
    }
}