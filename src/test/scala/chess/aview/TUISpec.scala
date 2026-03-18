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

    "process unknown input and return true (continue)" in {
      val controller = Controller()
      val tui = TUI(controller)
      tui.processInput("xyz") shouldBe true
    }
  }
}
