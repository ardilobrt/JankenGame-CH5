package id.co.ardilobrt.jankengame.game.controller

class Controller {
    private val draw = 0
    private val rock = 1
    private val paper = 2
    private val scissor = 3

    // Suit Algorithm
    fun ruleGame(hand1: Int, hand2: Int): Int {
        val result = when {
            hand1 == rock && hand2 == paper || hand1 == paper && hand2 == rock -> paper
            hand1 == paper && hand2 == scissor || hand1 == scissor && hand2 == paper -> scissor
            hand1 == scissor && hand2 == rock || hand1 == rock && hand2 == scissor -> rock
            else -> draw
        }
        return result
    }
}