package chess.aview.gui

import chess.controller.ControllerInterface
import chess.model.GameStatus

import scala.swing.*
import scala.swing.event.*
import java.awt.{Color as AwtColor, Font, Dimension, Cursor}
import javax.swing.border.EmptyBorder

/** Side panel showing game status, move info, and control buttons. */
class SidePanel(controller: ControllerInterface, onNewGame: () => Unit, onQuit: () => Unit) extends BoxPanel(Orientation.Vertical):

  background = new AwtColor(38, 36, 33)
  preferredSize = new Dimension(220, 0)
  border = new EmptyBorder(20, 16, 20, 16)

  // --- Title ---
  private val titleLabel = new Label("alu-chess"):
    font = new Font("SansSerif", Font.BOLD, 22)
    foreground = new AwtColor(230, 230, 230)
    horizontalAlignment = Alignment.Center

  // --- Status ---
  private val statusLabel = new Label(""):
    font = new Font("SansSerif", Font.PLAIN, 15)
    foreground = new AwtColor(200, 200, 200)
    horizontalAlignment = Alignment.Center

  // --- Current player indicator ---
  private val playerLabel = new Label(""):
    font = new Font("SansSerif", Font.BOLD, 14)
    foreground = new AwtColor(180, 180, 180)
    horizontalAlignment = Alignment.Center

  // --- Move counter ---
  private val moveLabel = new Label(""):
    font = new Font("SansSerif", Font.PLAIN, 12)
    foreground = new AwtColor(140, 140, 140)
    horizontalAlignment = Alignment.Center

  // --- Buttons ---
  private def styledButton(text: String, onClick: () => Unit): Button =
    val btn = new Button(text)
    btn.font = new Font("SansSerif", Font.BOLD, 13)
    btn.foreground = new AwtColor(230, 230, 230)
    btn.background = new AwtColor(68, 66, 63)
    btn.opaque = true
    btn.borderPainted = false
    btn.focusPainted = false
    btn.cursor = new Cursor(Cursor.HAND_CURSOR)
    btn.preferredSize = new Dimension(180, 38)
    btn.maximumSize = new Dimension(180, 38)
    btn.listenTo(btn)
    btn.reactions += { case ButtonClicked(_) => onClick() }
    btn

  private val newGameButton = styledButton("Neues Spiel", onNewGame)
  private val quitButton = styledButton("Beenden", onQuit)

  // Layout
  contents += titleLabel
  contents += Swing.VStrut(20)
  contents += new Separator
  contents += Swing.VStrut(15)
  contents += playerLabel
  contents += Swing.VStrut(8)
  contents += statusLabel
  contents += Swing.VStrut(8)
  contents += moveLabel
  contents += Swing.VStrut(20)
  contents += new Separator
  contents += Swing.VStrut(15)
  contents += newGameButton
  contents += Swing.VStrut(8)
  contents += quitButton
  contents += Swing.VGlue

  def refresh(): Unit =
    val game = controller.game
    val isWhite = game.currentPlayer == chess.model.Color.White

    playerLabel.text = if isWhite then "⬜ Weiß" else "⬛ Schwarz"

    statusLabel.text = game.status match
      case GameStatus.Playing   => "am Zug"
      case GameStatus.Check     => "Schach!"
      case GameStatus.Checkmate => s"Schachmatt!"
      case GameStatus.Stalemate => "Patt – Remis"
      case GameStatus.Resigned  => "Aufgegeben"
      case GameStatus.Draw      => "Remis"

    statusLabel.foreground = game.status match
      case GameStatus.Check     => new AwtColor(255, 180, 70)
      case GameStatus.Checkmate => new AwtColor(235, 97, 80)
      case GameStatus.Stalemate | GameStatus.Draw => new AwtColor(180, 180, 100)
      case _ => new AwtColor(200, 200, 200)

    val moveNumber = game.halfMoveClock // approximate display
    val lastMoveStr = game.lastMove.map(_.toString).getOrElse("–")
    moveLabel.text = s"Letzter Zug: $lastMoveStr"

    repaint()
