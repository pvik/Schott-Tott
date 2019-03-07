package org.pvik.st

import mu.KotlinLogging
import org.pvik.st.engine.*

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {

    logger.info { "Starting..." }

    println("Scott Tott")
    println("==========")

    val p1 = "pv"
    val p2 = "ash"

    Game().newGame(p1, p2)
}