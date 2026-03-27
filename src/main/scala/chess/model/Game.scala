package chess.model

enum GameStatus:
  case Playing, Check, Checkmate, Stalemate, Resigned, Draw

case class Game(board: Board, currentPlayer: Color, status: GameStatus, movedPieces: Set[Position] = Set.empty, lastMove: Option[Move] = None, halfMoveClock: Int = 0):

  def switchPlayer: Game =
    copy(currentPlayer = currentPlayer.opposite)

  /** Apply a move if it passes all validations:
    * 1. Source has a piece of the current player's color
    * 2. Target is not occupied by a friendly piece
    * 3. Move is valid for the piece type (pattern + path clearance + special rules)
    * 4. Move does not leave own king in check
    * After the move, determines new game status (Check, Checkmate, Stalemate, Draw).
    * Handles special moves: castling rook movement, en passant capture, pawn promotion.
    * Checks draw conditions: 50-move rule, insufficient material.
    * Returns None on failure. */
  def applyMove(m: Move): Option[Game] =
    board.cell(m.from) match
      case Some(piece) if piece.color == currentPlayer =>
        val targetFriendly = board.cell(m.to).exists(_.color == currentPlayer)
        if targetFriendly then None
        else if !MoveValidator.isValidMove(m, board, movedPieces, lastMove) then None
        else
          val isPawnMove = piece match { case Piece.Pawn(_) => true; case _ => false }
          val isCapture = board.cell(m.to).isDefined ||
            (isPawnMove && m.from.col != m.to.col && board.cell(m.to).isEmpty) // en passant
          board.move(m).flatMap { newBoard =>
            val fullBoard = MoveValidator.applyMoveEffects(m, board, newBoard)
            // Reject if own king would be in check after the move
            if MoveValidator.isInCheck(fullBoard, currentPlayer) then None
            else
              val opponent = currentPlayer.opposite
              val newMovedPieces = movedPieces + m.from
              val newHalfMoveClock = if isPawnMove || isCapture then 0 else halfMoveClock + 1
              val opponentInCheck = MoveValidator.isInCheck(fullBoard, opponent)
              val opponentHasMoves = MoveValidator.legalMoves(fullBoard, opponent, newMovedPieces, Some(m)).nonEmpty
              val newStatus =
                if opponentInCheck && !opponentHasMoves then GameStatus.Checkmate
                else if !opponentInCheck && !opponentHasMoves then GameStatus.Stalemate
                else if MoveValidator.isInsufficientMaterial(fullBoard) then GameStatus.Draw
                else if newHalfMoveClock >= 100 then GameStatus.Draw
                else if opponentInCheck then GameStatus.Check
                else GameStatus.Playing
              Some(copy(board = fullBoard, currentPlayer = opponent, status = newStatus,
                movedPieces = newMovedPieces, lastMove = Some(m), halfMoveClock = newHalfMoveClock))
          }
      case _ => None

  def resign: Game =
    copy(status = GameStatus.Resigned)

object Game:
  def newGame: Game =
    Game(Board.initial, Color.White, GameStatus.Playing)
