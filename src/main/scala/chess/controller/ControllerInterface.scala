package chess.controller

import chess.model.Game
import chess.util.Observer

trait ControllerInterface:
  def game: Game
  def newGame(): Unit
  def quit(): Unit
  def boardToString: String
  def add(observer: Observer): Unit
  def remove(observer: Observer): Unit
