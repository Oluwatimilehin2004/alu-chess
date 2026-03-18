package chess.aview.gui

import chess.model.{Color, Piece}

import scala.swing.*
import scala.swing.event.*
import java.awt.{Color as AwtColor, Font, Dimension, FlowLayout}
import javax.swing.{JOptionPane, UIManager}

/** Dialog for pawn promotion piece selection. */
object PromotionDialog:

  def show(parent: Component, color: Color): Option[Char] =
    val pieces = Seq(
      ('Q', "Dame", if color == Color.White then "\u2655" else "\u265B"),
      ('R', "Turm", if color == Color.White then "\u2656" else "\u265C"),
      ('B', "Läufer", if color == Color.White then "\u2657" else "\u265D"),
      ('N', "Springer", if color == Color.White then "\u2658" else "\u265E")
    )

    val options = pieces.map(_._2).toArray[Object]
    val result = JOptionPane.showOptionDialog(
      parent.peer,
      "Bauernumwandlung – Figur wählen:",
      "Promotion",
      JOptionPane.DEFAULT_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      null,
      options,
      options(0)
    )

    if result >= 0 && result < pieces.length then
      Some(pieces(result)._1)
    else
      Some('Q') // default to queen if dialog closed
