package chess.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class BoardSpec extends AnyWordSpec with Matchers {

  "An empty Board" should {

    val board = Board.empty

    "have size 8" in {
      board.size shouldBe 8
    }

    "have 8 rows" in {
      board.cells should have size 8
    }

    "have 8 columns per row" in {
      board.cells.foreach { row =>
        row should have size 8
      }
    }

    "have no pieces on any cell" in {
      for
        r <- 0 until 8
        c <- 0 until 8
      do board.cell(r, c) shouldBe None
    }
  }

  "An initial Board" should {

    val board = Board.initial

    "have white rooks on a1 and h1" in {
      board.cell(0, 0) shouldBe Some(Piece.Rook(Color.White))
      board.cell(0, 7) shouldBe Some(Piece.Rook(Color.White))
    }

    "have white king on e1" in {
      board.cell(0, 4) shouldBe Some(Piece.King(Color.White))
    }

    "have black king on e8" in {
      board.cell(7, 4) shouldBe Some(Piece.King(Color.Black))
    }

    "have white pawns on row 2" in {
      for c <- 0 until 8 do
        board.cell(1, c) shouldBe Some(Piece.Pawn(Color.White))
    }

    "have black pawns on row 7" in {
      for c <- 0 until 8 do
        board.cell(6, c) shouldBe Some(Piece.Pawn(Color.Black))
    }

    "have empty cells in the middle" in {
      for
        r <- 2 until 6
        c <- 0 until 8
      do board.cell(r, c) shouldBe None
    }
  }

  "Board.put" should {

    "place a piece on a valid cell" in {
      val board = Board.empty.put(3, 4, Piece.Queen(Color.White))
      board.cell(3, 4) shouldBe Some(Piece.Queen(Color.White))
    }

    "leave the board unchanged for invalid coordinates" in {
      val board = Board.empty.put(-1, 0, Piece.King(Color.White))
      board shouldBe Board.empty
    }
  }

  "Board.clear" should {

    "remove a piece from a valid cell" in {
      val board = Board.initial.clear(0, 0)
      board.cell(0, 0) shouldBe None
    }

    "leave the board unchanged for invalid coordinates" in {
      val board = Board.empty.clear(8, 8)
      board shouldBe Board.empty
    }
  }

  "Board.isValid" should {

    "return true for valid coordinates" in {
      Board.empty.isValid(0, 0) shouldBe true
      Board.empty.isValid(7, 7) shouldBe true
    }

    "return false for out-of-bounds coordinates" in {
      Board.empty.isValid(-1, 0) shouldBe false
      Board.empty.isValid(0, 8) shouldBe false
      Board.empty.isValid(8, 0) shouldBe false
    }
  }

  "Board.toString" should {

    "contain column labels" in {
      val s = Board.empty.toString
      s should include("a")
      s should include("h")
    }

    "contain row numbers" in {
      val s = Board.empty.toString
      s should include("1")
      s should include("8")
    }
  }
}
