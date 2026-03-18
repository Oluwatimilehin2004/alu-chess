package chess.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PositionSpec extends AnyWordSpec with Matchers {

  "A Position" should {

    "be valid for coordinates 0-7" in {
      Position(0, 0).isValid shouldBe true
      Position(7, 7).isValid shouldBe true
      Position(3, 4).isValid shouldBe true
    }

    "be invalid for out-of-bounds coordinates" in {
      Position(-1, 0).isValid shouldBe false
      Position(0, 8).isValid shouldBe false
      Position(8, 0).isValid shouldBe false
      Position(-1, -1).isValid shouldBe false
    }

    "display as algebraic notation" in {
      Position(0, 0).toString shouldBe "a1"
      Position(7, 7).toString shouldBe "h8"
      Position(1, 4).toString shouldBe "e2"
      Position(3, 3).toString shouldBe "d4"
    }

    "display as fallback for invalid positions" in {
      Position(-1, 0).toString should startWith("(?")
    }
  }

  "Position.fromString" should {

    "parse valid algebraic notation" in {
      Position.fromString("a1") shouldBe Some(Position(0, 0))
      Position.fromString("h8") shouldBe Some(Position(7, 7))
      Position.fromString("e2") shouldBe Some(Position(1, 4))
      Position.fromString("E2") shouldBe Some(Position(1, 4))
      Position.fromString(" e2 ") shouldBe Some(Position(1, 4))
    }

    "return None for invalid input" in {
      Position.fromString("") shouldBe None
      Position.fromString("z9") shouldBe None
      Position.fromString("a0") shouldBe None
      Position.fromString("i1") shouldBe None
      Position.fromString("abc") shouldBe None
      Position.fromString("1a") shouldBe None
    }
  }
}
