package chess.aview.gui

import chess.controller.ControllerInterface
import chess.model.{GameStatus, TestPositions, Fen, Move, ChessError}

import scala.swing.*
import scala.swing.event.*
import java.awt.{Color as AwtColor, Font, Dimension, Cursor}
import javax.swing.border.EmptyBorder
import javax.swing.JOptionPane

/** Side panel showing game status, move info, and control buttons. */
class SidePanel(controller: ControllerInterface, onNewGame: () => Unit, onQuit: () => Unit) extends BoxPanel(Orientation.Vertical):

  private val panelWidth = 320
  private val contentWidth = 250
  private val smallGap = 6
  private val sectionGap = 14

  background = new AwtColor(38, 36, 33)
  preferredSize = new Dimension(panelWidth, 900)
  minimumSize = new Dimension(panelWidth, 200)
  border = new EmptyBorder(20, 16, 20, 16)

  private def centerAlign(component: Component): Unit =
    component.xLayoutAlignment = 0.5

  // --- Title ---
  private val titleLabel = new Label("alu-chess"):
    font = new Font("SansSerif", Font.BOLD, 22)
    foreground = new AwtColor(230, 230, 230)
    horizontalAlignment = Alignment.Center
  centerAlign(titleLabel)

  // --- Status ---
  private val statusLabel = new Label(""):
    font = new Font("SansSerif", Font.PLAIN, 15)
    foreground = new AwtColor(200, 200, 200)
    horizontalAlignment = Alignment.Center
  centerAlign(statusLabel)

  // --- Current player indicator ---
  private val playerLabel = new Label(""):
    font = new Font("SansSerif", Font.BOLD, 14)
    foreground = new AwtColor(180, 180, 180)
    horizontalAlignment = Alignment.Center
  centerAlign(playerLabel)

  // --- Move counter ---
  private val moveLabel = new Label(""):
    font = new Font("SansSerif", Font.PLAIN, 12)
    foreground = new AwtColor(140, 140, 140)
    horizontalAlignment = Alignment.Center
  centerAlign(moveLabel)

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
    btn.preferredSize = new Dimension(contentWidth, 34)
    btn.maximumSize = new Dimension(contentWidth, 34)
    btn.listenTo(btn)
    btn.reactions += { case ButtonClicked(_) => onClick() }
    centerAlign(btn)
    btn

  private def styleTextInput(input: TextComponent): Unit =
    input.font = new Font("Monospaced", Font.PLAIN, 12)
    input.foreground = new AwtColor(230, 230, 230)
    input.background = new AwtColor(55, 53, 50)
    input.caret.color = new AwtColor(230, 230, 230)
    input.border = new EmptyBorder(6, 8, 6, 8)
    centerAlign(input)

  private def showError(message: String): Unit =
    JOptionPane.showMessageDialog(peer, message, "Fehler", JOptionPane.ERROR_MESSAGE)

  private def loadFenAndReport(fen: String): Unit =
    controller.loadFenResult(fen) match
      case Left(err)  => showError(err.message)
      case Right(_)   => fenInputField.text = Fen.toFen(controller.game)

  private def parseCoordinateToken(token: String): Either[ChessError, Move] =
    val stripped = token.trim.replaceAll("[+#?!]+$", "")
    if stripped.matches("^[a-h][1-8][a-h][1-8]$") then Move.fromStringE(stripped)
    else if stripped.matches("^[a-h][1-8][a-h][1-8][qrbnQRBN]$") then
      val from = stripped.substring(0, 2)
      val to = stripped.substring(2, 4)
      val promo = stripped.substring(4, 5).toUpperCase
      Move.fromStringE(s"$from $to $promo")
    else Left(ChessError.InvalidMoveFormat(token))

  private def extractPgnTokens(text: String): Vector[String] =
    val withoutComments = text
      .replaceAll("\\{[^}]*\\}", " ")
      .replaceAll("\\([^)]*\\)", " ")
      .replaceAll("\\d+\\.(\\.\\.)?", " ")
    withoutComments
      .split("\\s+")
      .toVector
      .map(_.trim)
      .filter(_.nonEmpty)
      .filterNot(t => t == "1-0" || t == "0-1" || t == "1/2-1/2" || t == "*")

  private val newGameButton = styledButton("Neues Spiel", onNewGame)
  private val quitButton = styledButton("Beenden", onQuit)

  // --- FEN Input ---
  private val fenInputLabel = new Label("FEN"):
    font = new Font("SansSerif", Font.BOLD, 13)
    foreground = new AwtColor(180, 180, 180)
  centerAlign(fenInputLabel)

  private val fenInputField = new TextField:
    columns = 24
    maximumSize = new Dimension(contentWidth, 32)
    preferredSize = new Dimension(contentWidth, 32)
  styleTextInput(fenInputField)

  private val fillFenButton = styledButton("Aktuelle FEN", () => fenInputField.text = Fen.toFen(controller.game))
  private val loadFenButton = styledButton("FEN laden", () =>
    val fen = fenInputField.text.trim
    if fen.isEmpty then showError("Bitte einen FEN-String eingeben.")
    else loadFenAndReport(fen)
  )

  // --- PGN Input ---
  private val pgnInputLabel = new Label("PGN"):
    font = new Font("SansSerif", Font.BOLD, 13)
    foreground = new AwtColor(180, 180, 180)
  centerAlign(pgnInputLabel)

  private val pgnHintLabel = new Label("Akzeptiert Koordinatenzuege, z. B. e2e4 e7e5"):
    font = new Font("SansSerif", Font.ITALIC, 11)
    foreground = new AwtColor(150, 150, 150)
    horizontalAlignment = Alignment.Center
  centerAlign(pgnHintLabel)

  private val pgnInputArea = new TextArea:
    rows = 5
    lineWrap = true
    wordWrap = true
  styleTextInput(pgnInputArea)

  private val pgnScrollPane = new ScrollPane(pgnInputArea):
    preferredSize = new Dimension(contentWidth, 110)
    maximumSize = new Dimension(contentWidth, 110)
  centerAlign(pgnScrollPane)

  private val applyPgnButton = styledButton("PGN anwenden", () =>
    val raw = pgnInputArea.text.trim
    if raw.isEmpty then showError("Bitte PGN-Text eingeben.")
    else
      val tokens = extractPgnTokens(raw)
      if tokens.isEmpty then showError("Keine Zug-Tokens im PGN gefunden.")
      else
        val applyResult = tokens.zipWithIndex.foldLeft[Either[String, Unit]](Right(())) {
          case (Left(err), _) => Left(err)
          case (Right(_), (token, idx)) =>
            for
              move <- parseCoordinateToken(token).left.map(err => s"Zug ${idx + 1} ('$token'): ${err.message}")
              _ <- controller.doMoveResult(move).left.map(err => s"Zug ${idx + 1} ('$token'): ${err.message}")
            yield ()
        }
        applyResult.left.foreach(showError)
  )

  // --- Test Positions ---
  private val testPositionLabel = new Label("Teststellungen"):
    font = new Font("SansSerif", Font.BOLD, 13)
    foreground = new AwtColor(180, 180, 180)
    horizontalAlignment = Alignment.Center
  centerAlign(testPositionLabel)

  private val selectableTestPositions = TestPositions.positions.filter(_.fen.nonEmpty)

  private val positionCombo = new ComboBox(selectableTestPositions.map(_.name)):
    font = new Font("SansSerif", Font.PLAIN, 12)
    preferredSize = new Dimension(contentWidth, 30)
    maximumSize = new Dimension(contentWidth, 30)
  centerAlign(positionCombo)

  private val positionDescLabel = new Label(""):
    font = new Font("SansSerif", Font.ITALIC, 11)
    foreground = new AwtColor(160, 160, 160)
    horizontalAlignment = Alignment.Center
    preferredSize = new Dimension(contentWidth, 36)
    maximumSize = new Dimension(contentWidth, 44)
  centerAlign(positionDescLabel)

  private val loadPositionButton = styledButton("Stellung laden", () => loadSelectedPosition())

  private def loadSelectedPosition(): Unit =
    val idx = positionCombo.selection.index
    if idx < 0 || idx >= selectableTestPositions.size then return
    val tp = selectableTestPositions(idx)
    loadFenAndReport(tp.fen)

  listenTo(positionCombo.selection)
  reactions += {
    case SelectionChanged(`positionCombo`) =>
      val idx = positionCombo.selection.index
      if idx >= 0 && idx < selectableTestPositions.size then
        positionDescLabel.text = s"<html><center>${selectableTestPositions(idx).description}</center></html>"
  }

  // Layout
  contents += titleLabel
  contents += Swing.VStrut(sectionGap)
  contents += new Separator
  contents += Swing.VStrut(sectionGap)
  contents += playerLabel
  contents += Swing.VStrut(smallGap)
  contents += statusLabel
  contents += Swing.VStrut(smallGap)
  contents += moveLabel
  contents += Swing.VStrut(sectionGap)
  contents += new Separator
  contents += Swing.VStrut(sectionGap)
  contents += newGameButton
  contents += Swing.VStrut(smallGap)
  contents += quitButton
  contents += Swing.VStrut(sectionGap)
  contents += new Separator
  contents += Swing.VStrut(sectionGap)
  contents += fenInputLabel
  contents += Swing.VStrut(smallGap)
  contents += fenInputField
  contents += Swing.VStrut(smallGap)
  contents += fillFenButton
  contents += Swing.VStrut(smallGap)
  contents += loadFenButton
  contents += Swing.VStrut(sectionGap)
  contents += new Separator
  contents += Swing.VStrut(sectionGap)
  contents += testPositionLabel
  contents += Swing.VStrut(smallGap)
  contents += positionCombo
  contents += Swing.VStrut(4)
  contents += positionDescLabel
  contents += Swing.VStrut(smallGap)
  contents += loadPositionButton
  contents += Swing.VStrut(sectionGap)
  contents += new Separator
  contents += Swing.VStrut(sectionGap)
  contents += pgnInputLabel
  contents += Swing.VStrut(4)
  contents += pgnHintLabel
  contents += Swing.VStrut(smallGap)
  contents += pgnScrollPane
  contents += Swing.VStrut(smallGap)
  contents += applyPgnButton
  contents += Swing.VStrut(10)
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

    val lastMoveStr = game.lastMove.map(_.toString).getOrElse("–")
    moveLabel.text = s"Letzter Zug: $lastMoveStr"

    if !fenInputField.hasFocus then
      fenInputField.text = Fen.toFen(game)

    repaint()
