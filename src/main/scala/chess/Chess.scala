package chess

import chess.controller.Controller
import chess.aview.TUI

@main def aluChess(): Unit =
  val controller = Controller()
  val tui = TUI(controller)
  tui.inputLoop()
