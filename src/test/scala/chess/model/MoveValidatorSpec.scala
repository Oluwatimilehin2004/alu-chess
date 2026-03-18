package chess.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class MoveValidatorSpec extends AnyWordSpec with Matchers {

  // Helper: empty board with specific pieces
  def boardWith(pieces: (Position, Piece)*): Board =
    pieces.foldLeft(Board.empty) { case (b, (pos, piece)) => b.put(pos, piece) }

  // ---------- Pawn ----------

  "Pawn movement" should {

    "allow white pawn one square forward" in {
      val board = boardWith(Position(1, 4) -> Piece.Pawn(Color.White))
      val move = Move(Position(1, 4), Position(2, 4))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "allow white pawn two squares forward from start row" in {
      val board = boardWith(Position(1, 4) -> Piece.Pawn(Color.White))
      val move = Move(Position(1, 4), Position(3, 4))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "reject white pawn two squares forward from non-start row" in {
      val board = boardWith(Position(2, 4) -> Piece.Pawn(Color.White))
      val move = Move(Position(2, 4), Position(4, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "reject white pawn moving forward into occupied square" in {
      val board = boardWith(
        Position(1, 4) -> Piece.Pawn(Color.White),
        Position(2, 4) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(1, 4), Position(2, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "reject white pawn two squares if path blocked" in {
      val board = boardWith(
        Position(1, 4) -> Piece.Pawn(Color.White),
        Position(2, 4) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(1, 4), Position(3, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "allow white pawn diagonal capture" in {
      val board = boardWith(
        Position(1, 4) -> Piece.Pawn(Color.White),
        Position(2, 5) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(1, 4), Position(2, 5))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "reject white pawn diagonal move without capture" in {
      val board = boardWith(Position(1, 4) -> Piece.Pawn(Color.White))
      val move = Move(Position(1, 4), Position(2, 5))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "reject white pawn moving backward" in {
      val board = boardWith(Position(3, 4) -> Piece.Pawn(Color.White))
      val move = Move(Position(3, 4), Position(2, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "allow black pawn one square forward (downward)" in {
      val board = boardWith(Position(6, 4) -> Piece.Pawn(Color.Black))
      val move = Move(Position(6, 4), Position(5, 4))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "allow black pawn two squares forward from start row" in {
      val board = boardWith(Position(6, 4) -> Piece.Pawn(Color.Black))
      val move = Move(Position(6, 4), Position(4, 4))
      MoveValidator.isValidMove(move, board) shouldBe true
    }
  }

  // ---------- Rook ----------

  "Rook movement" should {

    "allow horizontal move" in {
      val board = boardWith(Position(0, 0) -> Piece.Rook(Color.White))
      val move = Move(Position(0, 0), Position(0, 7))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "allow vertical move" in {
      val board = boardWith(Position(0, 0) -> Piece.Rook(Color.White))
      val move = Move(Position(0, 0), Position(7, 0))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "reject diagonal move" in {
      val board = boardWith(Position(0, 0) -> Piece.Rook(Color.White))
      val move = Move(Position(0, 0), Position(3, 3))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "reject move through occupied square" in {
      val board = boardWith(
        Position(0, 0) -> Piece.Rook(Color.White),
        Position(0, 3) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(0, 0), Position(0, 7))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "allow move to square right before blocking piece" in {
      val board = boardWith(
        Position(0, 0) -> Piece.Rook(Color.White),
        Position(0, 3) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(0, 0), Position(0, 2))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "allow capturing on the blocking square" in {
      val board = boardWith(
        Position(0, 0) -> Piece.Rook(Color.White),
        Position(0, 3) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(0, 0), Position(0, 3))
      MoveValidator.isValidMove(move, board) shouldBe true
    }
  }

  // ---------- Bishop ----------

  "Bishop movement" should {

    "allow diagonal move" in {
      val board = boardWith(Position(0, 0) -> Piece.Bishop(Color.White))
      val move = Move(Position(0, 0), Position(4, 4))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "reject horizontal move" in {
      val board = boardWith(Position(0, 0) -> Piece.Bishop(Color.White))
      val move = Move(Position(0, 0), Position(0, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "reject move through occupied diagonal square" in {
      val board = boardWith(
        Position(0, 0) -> Piece.Bishop(Color.White),
        Position(2, 2) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(0, 0), Position(4, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "allow diagonal capture" in {
      val board = boardWith(
        Position(0, 0) -> Piece.Bishop(Color.White),
        Position(3, 3) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(0, 0), Position(3, 3))
      MoveValidator.isValidMove(move, board) shouldBe true
    }
  }

  // ---------- Queen ----------

  "Queen movement" should {

    "allow horizontal move" in {
      val board = boardWith(Position(3, 3) -> Piece.Queen(Color.White))
      val move = Move(Position(3, 3), Position(3, 7))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "allow diagonal move" in {
      val board = boardWith(Position(3, 3) -> Piece.Queen(Color.White))
      val move = Move(Position(3, 3), Position(6, 6))
      MoveValidator.isValidMove(move, board) shouldBe true
    }

    "reject knight-like move" in {
      val board = boardWith(Position(3, 3) -> Piece.Queen(Color.White))
      val move = Move(Position(3, 3), Position(5, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "reject move through occupied square" in {
      val board = boardWith(
        Position(3, 3) -> Piece.Queen(Color.White),
        Position(3, 5) -> Piece.Pawn(Color.Black)
      )
      val move = Move(Position(3, 3), Position(3, 7))
      MoveValidator.isValidMove(move, board) shouldBe false
    }
  }

  // ---------- King ----------

  "King movement" should {

    "allow one square in any direction" in {
      val board = boardWith(Position(4, 4) -> Piece.King(Color.White))
      val directions = List(
        (1, 0), (-1, 0), (0, 1), (0, -1),
        (1, 1), (1, -1), (-1, 1), (-1, -1)
      )
      directions.foreach { (dr, dc) =>
        val move = Move(Position(4, 4), Position(4 + dr, 4 + dc))
        MoveValidator.isValidMove(move, board) shouldBe true
      }
    }

    "reject move of two squares" in {
      val board = boardWith(Position(4, 4) -> Piece.King(Color.White))
      val move = Move(Position(4, 4), Position(6, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "reject staying in place" in {
      val board = boardWith(Position(4, 4) -> Piece.King(Color.White))
      val move = Move(Position(4, 4), Position(4, 4))
      MoveValidator.isValidMove(move, board) shouldBe false
    }
  }

  // ---------- Knight ----------

  "Knight movement" should {

    "allow all eight L-shaped moves" in {
      val board = boardWith(Position(4, 4) -> Piece.Knight(Color.White))
      val targets = List(
        (6, 5), (6, 3), (2, 5), (2, 3),
        (5, 6), (5, 2), (3, 6), (3, 2)
      )
      targets.foreach { (r, c) =>
        val move = Move(Position(4, 4), Position(r, c))
        MoveValidator.isValidMove(move, board) shouldBe true
      }
    }

    "reject non-L-shaped move" in {
      val board = boardWith(Position(4, 4) -> Piece.Knight(Color.White))
      val move = Move(Position(4, 4), Position(5, 5))
      MoveValidator.isValidMove(move, board) shouldBe false
    }

    "jump over other pieces" in {
      // Surround knight with pieces, should still be able to move
      val board = boardWith(
        Position(4, 4) -> Piece.Knight(Color.White),
        Position(5, 4) -> Piece.Pawn(Color.White),
        Position(3, 4) -> Piece.Pawn(Color.White),
        Position(4, 5) -> Piece.Pawn(Color.White),
        Position(4, 3) -> Piece.Pawn(Color.White)
      )
      val move = Move(Position(4, 4), Position(6, 5))
      MoveValidator.isValidMove(move, board) shouldBe true
    }
  }

  // ---------- Empty square ----------

  "isValidMove" should {

    "return false for empty source square" in {
      val board = Board.empty
      val move = Move(Position(0, 0), Position(1, 0))
      MoveValidator.isValidMove(move, board) shouldBe false
    }
  }
}
