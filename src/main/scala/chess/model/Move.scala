package chess.model

/** A move from one position to another, with optional promotion type. No validation — just data. */
case class Move(from: Position, to: Position, promotion: Option[Char] = None):

  override def toString: String =
    val promoStr = promotion.map(p => s"=$p").getOrElse("")
    s"$from→$to$promoStr"

object Move:
  /** Parse a move string like "e2 e4", "e7 e8 Q" (promotion), or "e2e4". */
  def fromString(s: String): Option[Move] =
    val parts = s.trim.toLowerCase.split("\\s+")
    parts match
      case Array(f, t, p) if p.length == 1 =>
        val promoChar = p.charAt(0).toUpper
        if "QRBN".contains(promoChar) then
          for
            from <- Position.fromString(f)
            to   <- Position.fromString(t)
          yield Move(from, to, Some(promoChar))
        else None
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
