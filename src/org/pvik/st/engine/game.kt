package org.pvik.st.engine

import java.lang.System.exit

class Game {

    private val player : Array<Player> = Array(2) { i -> Player(i.toString())}
    //private val player = hashMapOf()

    fun newGame(pl1: String, pl2: String) {
        Deck.new()

        player[0] = Player(pl1)
        player[1] = Player(pl2)

        Board.init(player[0], player[1])

        println("Starting New Game")
        println("[ ${player[0].name} ] vs [ ${player[1].name} ]")

        gameLoop(0)
    }

    private fun gameLoop(p: Int) {

        println("===================")
        println("${player[p].name} 's turn\n")

        println(Board)

        println("${player[p].name}'s Hand: ${player[p].handStr()}")

        val c : Card = getCardInput(player[p])

        val sp = getStonePositionInput()

        try {
            Board.play(player[p], c, sp)
        }
        catch (e : Exception) {
            println ("Invalid Play ${e.message}")
            gameLoop(p)
        }

        gameLoop((p+1) %2)
    }

    private fun getCardInput(p : Player) : Card {
        print("Play card: ")
        var ip: String
        var valid = false

        do {
            ip = readLine()!!

            if (ip == "q") {
                println("Exiting...")
                exit(0)
            }

            try {
                if (p.hasCard(Card.fromString(ip)))
                    valid = true
                else
                    println("Player doesnt have that card in hand")
            } catch(e: Exception) {
                println("Invalid Input")
            }

        } while (!valid)

        return Card.fromString(ip)
    }

    private fun getStonePositionInput() : Int {
        print("Play at: ")
        var ip: String

        var valid = false
        do {
            ip = readLine()!!

            if (ip == "q") {
                println("Exiting...")
                exit(0)
            }

            if (ip.length == 1 && ip[0].isDigit() && ip[0] != '0') {
                valid = true
            }
            else {
                println("Invalid Input")
            }
        } while (!valid)

        return (Integer.parseInt(ip[0].toString()) - 1)
    }
}