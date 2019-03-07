package org.pvik.st.engine

object Board {
    val playedCards : MutableSet<Card> = mutableSetOf()

    private lateinit var p1: Player
    private lateinit var p2: Player

    private lateinit var border : Array<Stone>

    private lateinit var p1Name : String
    private lateinit var p2Name : String

    fun init(pl1 : Player , pl2: Player) {
        p1 = pl1
        p1Name = p1.name.substring(0,minOf(3, p1.name.length))

        p2 = pl2
        p2Name = p2.name.substring(0,minOf(3, p2.name.length))

        border = Array(9) { Stone(p1, p2) }

        playedCards.removeIf { true } // empty playedCards set
    }

    fun play(p: Player, c: Card, stone: Int) {
        p.play(c)
        border[stone].play(p, c)
        playedCards.add(c)

        border.toList().filter { s -> !s.claimed }.map {s -> s.checkClaim()}
    }

    override fun toString(): String {

        var s = "      [$p1Name]                         [$p2Name]\n"
        for (i in 0 .. 8) {
            s += "${i+1}  ${border[i]}  ${i+1}\n"
        }
        return s
    }


}