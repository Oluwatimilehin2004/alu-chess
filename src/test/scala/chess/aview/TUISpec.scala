package chess.aview

import chess.controller.{Controller, ControllerInterface}
import chess.util.Observer
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TUISpec extends AnyWordSpec with Matchers {

  "A TUI" should {

    "process 'q' and return false (stop)" in {
      val controller = Controller()
      val tui = TUI(controller)
      tui.processInput("q") shouldBe false
    }

    "process 'n' and return true (continue)" in {
      val controller = Controller()
      val tui = TUI(controller)
      tui.processInput("n") shouldBe true
    }

    "process empty input and return true (continue)" in {
      val controller = Controller()
      val tui = TUI(controller)
      tui.processInput("") shouldBe true
    }

    "process a valid move like 'e2 e4'" in {
      val controller = Controller()
      val tui = TUI(controller)
      tui.processInput("e2 e4") shouldBe true
      controller.game.board.cell(chess.model.Position(3, 4)) shouldBe Some(chess.model.Piece.Pawn(chess.model.Color.White))
    }

    "process an invalid move and return true (continue)" in {
      val controller = Controller()
      val tui = TUI(controller)
      tui.processInput("e5 e6") shouldBe true // empty square, move rejected
    }

    "process unparseable input and return true (continue)" in {
      val controller = Controller()
      val tui = TUI(controller)
      tui.processInput("xyz") shouldBe true
    }
  }
}
