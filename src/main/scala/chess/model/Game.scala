package chess.model

enum GameStatus:
  case Playing, Resigned

case class Game(board: Board, currentPlayer: Color, status: GameStatus):

  def switchPlayer: Game =
    copy(currentPlayer = currentPlayer.opposite)

  /** Apply a move if it passes all validations:
    * 1. Source has a piece of the current player's color
    * 2. Target is not occupied by a friendly piece
    * 3. Move is valid for the piece type (pattern + path clearance)
    * Returns None on failure. */
  def applyMove(m: Move): Option[Game] =
    board.cell(m.from) match
      case Some(piece) if piece.color == currentPlayer =>
        val targetFriendly = board.cell(m.to).exists(_.color == currentPlayer)
        if targetFriendly then None
        else if !MoveValidator.isValidMove(m, board) then None
        else board.move(m).map(newBoard => copy(board = newBoard).switchPlayer)
      case _ => None

  def resign: Game =
    copy(status = GameStatus.Resigned)

object Game:
  def newGame: Game =
    Game(Board.initial, Color.White, GameStatus.Playing)
