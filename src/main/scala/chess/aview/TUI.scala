package chess.aview

import chess.controller.ControllerInterface
import chess.util.Observer
import scala.io.StdIn

class TUI(controller: ControllerInterface) extends Observer:
  controller.add(this)

  override def update(): Unit =
    println(controller.boardToString)

  def inputLoop(): Unit =
    println("alu-chess – Eingabe ('n' = neues Spiel, 'q' = beenden):")
    update()
    var running = true
    while running do
      val input = StdIn.readLine("> ")
      running = processInput(input)

  def processInput(input: String): Boolean =
    input.trim match
      case "q" =>
        println("Auf Wiedersehen!")
        false
      case "n" =>
        controller.newGame()
        true
      case "" =>
        true
      case _ =>
        println(s"Unbekannter Befehl: '$input'. Versuche 'n' oder 'q'.")
        true
