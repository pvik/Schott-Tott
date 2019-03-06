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

class Stack {
    private val cards : MutableSet<Card> = mutableSetOf()

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
        return "$s|"
    }

    private fun isSequence(l : List<Int>) : Boolean {

        for (i in 1 .. l.size) {
            if ((l[i-1] + 1) != l[i])
                return false
        }
        return true
    }

    fun value(): Int {

        var sum : Int = cards.fold(0) { v, c -> v + c.rank  }

        if (cards.size == 3) {

            val s : Set<Suit> = cards.stream().map { c -> c.suit }.toList().toSet()
            val r : List<Int> = cards.stream().map { c -> c.rank }.toList().sorted()

            if (s.size == 1 && isSequence(r)) // Same Suit & in seq
                sum += 300
            else if (r.toSet().size == 1)     // Same Rank
                sum += 200
            else if (s.size == 1)             // Same Suit
                sum += 100
            else if (isSequence(r))           // Sequence
                sum += 50
        }

        return sum
    }

    fun greaterThan (s: Stack) : Boolean {

        return false
    }
}

class Stone (val p1: Player, val p2: Player) {
    private var claimed: Boolean = false
    private var claimedBy: Player? = null

    private val battleStacks = hashMapOf(p1 to Stack(), p2 to Stack())

    fun play(p: Player, c: Card) {
        battleStacks[p]!!.add(c)

        val otherPlayer : Player = if (p == p1) p2 else p1
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

