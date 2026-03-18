package chess.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class MoveSpec extends AnyWordSpec with Matchers {

  "A Move" should {

    "display as algebraic notation" in {
      Move(Position(1, 4), Position(3, 4)).toString shouldBe "e2→e4"
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

    "return None for invalid input" in {
      Move.fromString("") shouldBe None
      Move.fromString("e2") shouldBe None
      Move.fromString("e2 z9") shouldBe None
      Move.fromString("hello world foo") shouldBe None
    }
  }
}
