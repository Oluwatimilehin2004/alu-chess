package chess.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameSpec extends AnyWordSpec with Matchers {

  "A new Game" should {

    val game = Game.newGame

    "start with white as current player" in {
      game.currentPlayer shouldBe Color.White
    }

    "have an initial board" in {
      game.board shouldBe Board.initial
    }
  }

  "switchPlayer" should {

    "toggle from white to black" in {
      val game = Game.newGame.switchPlayer
      game.currentPlayer shouldBe Color.Black
    }

    "toggle from black back to white" in {
      val game = Game.newGame.switchPlayer.switchPlayer
      game.currentPlayer shouldBe Color.White
    }
  }
}
