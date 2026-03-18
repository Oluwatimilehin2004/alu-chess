package chess.controller

import chess.model.{Game, Color, Board}
import chess.util.Observer
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" should {

    "start with an initial game" in {
      val controller = Controller()
      controller.game.board shouldBe Board.initial
      controller.game.currentPlayer shouldBe Color.White
    }

    "notify observers on newGame" in {
      val controller = Controller()
      var notified = false
      val observer = new Observer:
        override def update(): Unit = notified = true
      controller.add(observer)
      controller.newGame()
      notified shouldBe true
    }

    "return a board string" in {
      val controller = Controller()
      val s = controller.boardToString
      s should include("K")
      s should include("k")
    }
  }
}
