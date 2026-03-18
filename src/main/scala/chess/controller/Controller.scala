package chess.controller

import chess.model.{Game, Board, Color}
import chess.util.{Observable, Observer}

class Controller extends ControllerInterface with Observable:
  private var _game: Game = Game.newGame

  override def game: Game = _game

  override def newGame(): Unit =
    _game = Game.newGame
    notifyObservers()

  override def quit(): Unit =
    sys.exit(0)

  override def boardToString: String =
    _game.board.toString
