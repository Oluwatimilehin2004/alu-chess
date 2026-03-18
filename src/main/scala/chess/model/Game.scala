package chess.model

case class Game(board: Board, currentPlayer: Color):

  def switchPlayer: Game =
    copy(currentPlayer = currentPlayer.opposite)

object Game:
  def newGame: Game =
    Game(Board.initial, Color.White)
