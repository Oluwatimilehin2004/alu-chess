package chess.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class MoveSpec extends AnyWordSpec with Matchers {

  "A Move" should {

    "display as algebraic notation" in {
      Move(Position(1, 4), Position(3, 4)).toString shouldBe "e2→e4"
    }

    "display with promotion suffix" in {
      Move(Position(6, 4), Position(7, 4), Some('Q')).toString shouldBe "e7→e8=Q"
    }
  }

  "Move.fromString" should {

    "parse 'e2 e4' format" in {
      Move.fromString("e2 e4") shouldBe Some(Move(Position(1, 4), Position(3, 4)))
    }

    "parse 'e2e4' format (no space)" in {
      Move.fromString("e2e4") shouldBe Some(Move(Position(1, 4), Position(3, 4)))
    }

    "be case insensitive" in {
      Move.fromString("E2 E4") shouldBe Some(Move(Position(1, 4), Position(3, 4)))
    }

    "handle extra whitespace" in {
      Move.fromString("  e2   e4  ") shouldBe Some(Move(Position(1, 4), Position(3, 4)))
    }

    "parse promotion format 'e7 e8 Q'" in {
      Move.fromString("e7 e8 Q") shouldBe Some(Move(Position(6, 4), Position(7, 4), Some('Q')))
    }

    "parse promotion case-insensitively" in {
      Move.fromString("e7 e8 n") shouldBe Some(Move(Position(6, 4), Position(7, 4), Some('N')))
    }

    "parse all promotion types" in {
      Move.fromString("a7 a8 R") shouldBe Some(Move(Position(6, 0), Position(7, 0), Some('R')))
      Move.fromString("a7 a8 B") shouldBe Some(Move(Position(6, 0), Position(7, 0), Some('B')))
      Move.fromString("a7 a8 N") shouldBe Some(Move(Position(6, 0), Position(7, 0), Some('N')))
    }

    "reject invalid promotion character" in {
      Move.fromString("e7 e8 X") shouldBe None
    }

    "return None for invalid input" in {
      Move.fromString("") shouldBe None
      Move.fromString("e2") shouldBe None
      Move.fromString("e2 z9") shouldBe None
      Move.fromString("hello world foo") shouldBe None
    }
  }
}
