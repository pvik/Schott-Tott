package org.pvik.st.engine

class Board (private val p1: Player, private val p2: Player) {

    private val playedCards : MutableSet<Card> = mutableSetOf()

    private val border = Array(9) { Stone(p1, p2) }

    private val p1Name = p1.name.substring(0,minOf(3, p1.name.length))
    private val p2Name = p2.name.substring(0,minOf(3, p2.name.length))

    fun play(p: Player, c: Card, stone: Int) {
        p.play(c)
        border[stone].play(p, c)
        playedCards.add(c)
    }

    override fun toString(): String {

        var s = "      [$p1Name]                  [$p2Name]\n"
        for (i in 0 .. 8) {
            s += "${i+1}  ${border[i]}  ${i+1}\n"
        }
        return s
    }


}