package org.pvik.st.engine

import kotlin.random.Random
import kotlin.streams.toList

enum class Suit {
    A,B,C,D,E,F
}

class Card (val suit: Suit, val rank : Int) {
    override fun toString(): String {
        return "$suit$rank"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (suit != other.suit) return false
        if (rank != other.rank) return false

        return true
    }

    override fun hashCode(): Int {
        var result = suit.hashCode()
        result = 31 * result + rank
        return result
    }

    companion object {
        fun fromString(ip: String): Card {
            val fc = ip[0].toUpperCase()

            if (ip.length == 2 && ip[1].isDigit() &&
                (fc == 'A' || fc == 'B' || fc == 'C' ||
                        fc == 'D' || fc == 'E' || fc == 'F')
            ) {
                return Card(Suit.valueOf(fc.toString()), Integer.valueOf(ip[1].toString()))
            } else {
                throw Exception("Invalid Card String")
            }
        }
    }
}

object Deck {
    private val openedCards : MutableSet<Card> = mutableSetOf()

    fun new() {
        openedCards.removeIf {true} // empty openedCards set
    }

    fun getCard() : Card {
        if (openedCards.size < 54) {

            while (true) {
                val r = Random.nextInt(1, 10)
                val s = Suit.values()[Random.nextInt(0, 6)]

                val c = Card(s, r)

                if (!openedCards.contains(c)) {
                    openedCards.add(c)
                    return c
                }
            }
        }
        throw Exception("deck is empty")
    }

    fun size() : Int {
        return (54 - openedCards.size)
    }
}

class Player (val name : String) {
    private val hand : MutableSet<Card>
            = mutableSetOf(
        Deck.getCard(), Deck.getCard(), Deck.getCard(),
        Deck.getCard(),Deck.getCard(), Deck.getCard())

    fun play(c : Card) {
        if (hand.contains(c)) {
            hand.remove(c)
            if (Deck.size() > 0)
                hand.add(Deck.getCard())
        } else {
            throw Exception("Invalid Play: player [$name] doesn't have card on hand")
        }
    }

    fun handStr() : String {
        return (hand.fold("") { s, c -> "$s|$c" }) + "|"
    }

    fun hasCard (c : Card) :Boolean {
        return hand.contains(c)
    }
}

class Stack(private val cards: MutableSet<Card>) {
    //private var cards : MutableSet<Card> = c

    fun add(c : Card) {
        if (cards.size < 3)
            cards.add(c)
        else
            throw Exception ("Battle Stack Full")
    }

    override fun toString(): String {
        var initialStr = ""
        if(cards.size < 3) {
            for(i in 0..(2-cards.size)) {
                initialStr += "|--"
            }
        }
        val s = cards.fold(initialStr) { s, c -> "$s|$c" }
        return "$s| (" + String.format("%3d", value()) + ")"
    }

    private fun value(): Int {

        var sum : Int = cards.fold(0) { v, c -> v + c.rank  }

        if (cards.size == 3) {

            val s : Set<Suit> = cards.stream().map { c -> c.suit }.toList().toSet()
            val r : List<Int> = cards.stream().map { c -> c.rank }.toList().sorted()

            if (s.size == 1 && Helper.isSequence(r)) // Same Suit & in seq
                sum += 300
            else if (r.toSet().size == 1)     // Same Rank
                sum += 200
            else if (s.size == 1)             // Same Suit
                sum += 100
            else if (Helper.isSequence(r))    // Sequence
                sum += 50
        }

        return sum
    }

    fun genPossibleSequenceSameSuitStack (from: Int, suit: Suit): List<Stack> {
        return (from..7).map { r -> Stack(mutableSetOf(Card(suit, r), Card(suit, r+1), Card(suit, r+2)))  }
                .filter {stackPossibleInFuture()}
    }

    fun genPossibleSameRankStack (rank: Int): List<Stack> {
        val possibleCards : List<Card> =
                ('A'..'F').map { s -> Card(Suit.valueOf(s.toString()), rank) }
                        .filter { c -> !Board.playedCards.contains(c) }

        val possibleStacks : MutableList<Stack> = mutableListOf()

        if (possibleCards.size >= 3) {

        }

        for (s1 in 'A'..'F') {
            for (s2 in s1..'F') {
                (s2..'F').map { s3 -> Stack(mutableSetOf(Card(Suit.valueOf(s1.toString()), rank), Card(Suit.valueOf(s2.toString()), rank), Card(Suit.valueOf(s3.toString()), rank)))  }
            }
        }
        return possibleStacks
    }

    // Will check if the Stack represented in cards (MutableSet) can be played in the future
    private fun stackPossibleInFuture(): Boolean {
        return (cards.filter { c -> !Board.playedCards.contains(c)} .size) == 3
    }

    // Will try to find if there are any possible stacks that can be played that can beat the current stack
    fun stackCanBeBeatenInFuture() : Boolean {
        if (cards.size != 3) {
            // Stack not complete
            return false
        } else {
            val suits : Set<Suit> = cards.stream().map { c -> c.suit }.toList().toSet()
            val ranks : List<Int> = cards.stream().map { c -> c.rank }.toList().sorted()

            val possibleStacks : MutableList<Stack> = mutableListOf()

            if (suits.size == 1 && Helper.isSequence(ranks)) { // Same Suit & in seq

            }
            else if (ranks.toSet().size == 1) {   // Same Rank

            }
            else if (suits.size == 1) {           // Same Suit

            }
            else if (Helper.isSequence(ranks)) {  // Sequence

            }

            return false
        }
    }

    fun greaterThan (s: Stack) : Boolean {
        if (cards.size != 3) {
            // Dont bother comparing till the full stack is played
            return false
        }
        else if (cards.size == 3) {

            if (s.cards.size == 3) {
                // both stacks are completed, just compare values
                return value() > s.value()
            } else {
                val sSuits: Set<Suit> = cards.stream().map { c -> c.suit }.toList().toSet()
                val sRanks: List<Int> = cards.stream().map { c -> c.rank }.toList().sorted()

                val possibleStacks: MutableList<Stack> = mutableListOf()

                if (s.cards.size == 2) {
                    // find a possible card that can be played in s, which would beat this

                    if (sSuits.size == 1 && Helper.isSequence(sRanks)) { // Same Suit & in Seq
                        if (!Board.playedCards.contains(Card(sSuits.first(), sRanks.max()!! + 1))) {

                            possibleStacks.add(Stack(mutableSetOf(s.cards.first(), s.cards.last(),
                                    Card(sSuits.first(), sRanks.max()!! + 1))))
                        }
                        if (!Board.playedCards.contains(Card(sSuits.first(), sRanks.min()!! - 1))) {

                            possibleStacks.add(Stack(mutableSetOf(s.cards.first(), s.cards.last(),
                                    Card(sSuits.first(), sRanks.max()!! - 1))))
                        }
                    } else if (sRanks.toSet().size == 1) { // Same Rank
                        ('A'..'F').map { s1 -> Card(Suit.valueOf(s1.toString()), sRanks.first()) }
                                .filter { c -> !Board.playedCards.contains(c) } // This will include cards in the stack
                                .map { c -> possibleStacks.add(Stack(mutableSetOf(s.cards.first(), s.cards.last(), c))) }
                    } else if (sSuits.size == 1) { // Same Suit
                        (1..9).map { r -> Card(sSuits.first(), r) }
                                .filter { c -> !Board.playedCards.contains(c) } // This will include cards in the stack
                                .map { c -> possibleStacks.add(Stack(mutableSetOf(s.cards.first(), s.cards.last(), c))) }
                    } else if (Helper.isSequence(sRanks)) { // Seq
                        ('A'..'F').map { s1 -> Card(Suit.valueOf(s1.toString()), sRanks.max()!! + 1) }
                                .filter { c -> !Board.playedCards.contains(c) } // This will include cards in the stack
                                .map { c -> possibleStacks.add(Stack(mutableSetOf(s.cards.first(), s.cards.last(), c))) }

                        ('A'..'F').map { s1 -> Card(Suit.valueOf(s1.toString()), sRanks.min()!! - 1) }
                                .filter { c -> !Board.playedCards.contains(c) } // This will include cards in the stack
                                .map { c -> possibleStacks.add(Stack(mutableSetOf(s.cards.first(), s.cards.last(), c))) }
                    } else {
                        // Use highest available rank card not played as third card and calculate value
                        loop@ for (rankItr in 9 downTo 1) {
                            for (suitItr in ('A'..'F')) {
                                val c = Card(Suit.valueOf(suitItr.toString()), rankItr)
                                if (!Board.playedCards.contains(c)) {
                                    possibleStacks.add(Stack(mutableSetOf(s.cards.first(), s.cards.last(), c)))
                                    break@loop
                                }
                            }
                        }
                    }
                }

                val maxPossibleValue = possibleStacks.map { stck -> stck.value() }
                        .fold(0) { acc, vl -> if (acc > vl) vl else acc }

                return value() > maxPossibleValue
            }
        }
        return false
    }
}

class Stone (private val p1: Player, private val p2: Player) {
    private var claimed: Boolean = false
    private var claimedBy: Player? = null

    private val battleStacks = hashMapOf(p1 to Stack(mutableSetOf()), p2 to Stack(mutableSetOf()))

    fun play(p: Player, c: Card) {
        battleStacks[p]!!.add(c)

        val otherPlayer = if (p == p1) p2 else p1
        if (battleStacks[p]!!.greaterThan(battleStacks[otherPlayer]!!)) {
            claimed = true
            claimedBy = p
        }

    }

    override fun toString(): String {
        var center = "    [o]    "
        if (claimed) {
            if (claimedBy!! == p1) {
                center = " [o]       "
            }
            else if (claimedBy!! == p2) {
                center = "       [o] "
            }
        }
        return "${battleStacks[p1]} $center ${battleStacks[p2]}"
    }


}

