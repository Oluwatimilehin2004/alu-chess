package chess.model

/** A move from one position to another. No validation — just data. */
case class Move(from: Position, to: Position):

  override def toString: String = s"$from→$to"

object Move:
  /** Parse a move string like "e2 e4" or "e2e4". */
  def fromString(s: String): Option[Move] =
    val parts = s.trim.toLowerCase.split("\\s+")
    parts match
      case Array(f, t) =>
        for
          from <- Position.fromString(f)
          to   <- Position.fromString(t)
        yield Move(from, to)
      case Array(ft) if ft.length == 4 =>
        for
          from <- Position.fromString(ft.substring(0, 2))
          to   <- Position.fromString(ft.substring(2, 4))
        yield Move(from, to)
      case _ => None
