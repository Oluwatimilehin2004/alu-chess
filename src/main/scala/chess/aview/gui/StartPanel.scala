package chess.aview.gui

import chess.controller.ControllerInterface
import chess.model.TimeControl

import scala.swing.*
import java.awt.{Color as AwtColor, Font, Dimension, Cursor, GridLayout}
import java.awt.event.{MouseAdapter, MouseEvent}
import javax.swing.BorderFactory
import javax.swing.border.EmptyBorder

/** Lichess-inspired start/home screen.
  * Displays time-control cards the player can click to launch a game,
  * plus an "Ohne Uhr spielen" option for an untimed game.
  * `onStart(tc)` is called when the user selects a time control.
  */
class StartPanel(controller: ControllerInterface, onStart: Option[TimeControl] => Unit)
    extends BoxPanel(Orientation.Vertical):

  private val bg        = new AwtColor(38, 36, 33)
  private val cardBg    = new AwtColor(48, 46, 43)
  private val cardHover = new AwtColor(62, 60, 57)
  private val cardBorder= new AwtColor(65, 63, 60)
  private val titleFg   = new AwtColor(240, 240, 240)
  private val subtitleFg= new AwtColor(160, 160, 160)
  private val playBtnBg = new AwtColor(186, 202, 68)  // lichess yellow-green
  private val playBtnFg = new AwtColor(20, 20, 20)

  background = bg
  border = new EmptyBorder(40, 40, 40, 40)

  // --- Hero section ---
  private val heroIcon = new Label("♟"):
    font = new Font("Segoe UI Symbol", Font.PLAIN, 72)
    foreground = new AwtColor(186, 202, 68)
    horizontalAlignment = Alignment.Center
  heroIcon.xLayoutAlignment = 0.5

  private val heroTitle = new Label("alu-chess"):
    font = new Font("SansSerif", Font.BOLD, 40)
    foreground = titleFg
    horizontalAlignment = Alignment.Center
  heroTitle.xLayoutAlignment = 0.5

  private val heroSubtitle = new Label("Wähle ein Zeitformat und starte eine Partie"):
    font = new Font("SansSerif", Font.PLAIN, 15)
    foreground = subtitleFg
    horizontalAlignment = Alignment.Center
  heroSubtitle.xLayoutAlignment = 0.5

  // --- Time-control categories ---
  private case class Category(name: String, icon: String, color: AwtColor, presets: Vector[TimeControl])

  private val categories = Vector(
    Category("Bullet",     "⚡",  new AwtColor(210, 100, 60),  Vector(TimeControl.Bullet1_0, TimeControl.Bullet2_1)),
    Category("Blitz",      "🔥",  new AwtColor(186, 202, 68),  Vector(TimeControl.Blitz3_0, TimeControl.Blitz3_2, TimeControl.Blitz5_0, TimeControl.Blitz5_3)),
    Category("Rapid",      "⏱",  new AwtColor(100, 170, 220), Vector(TimeControl.Rapid10_0, TimeControl.Rapid10_5, TimeControl.Rapid15_10)),
    Category("Classical",  "🎓",  new AwtColor(160, 140, 200), Vector(TimeControl.Classical30_0)),
  )

  // --- Helper: time-control card ---
  private def tcCard(tc: TimeControl, accentColor: AwtColor): Panel =
    val initialMin = tc.initialTimeMs / 60_000L
    val initialSec = (tc.initialTimeMs / 1_000L) % 60L
    val incSec     = tc.incrementMs / 1_000L
    // Main label e.g. "1+0" or "15+10"
    val labelText = if initialMin > 0 then s"$initialMin+$incSec" else s"${initialSec}s+$incSec"
    // Sub-label: time unit
    val descText  = if initialMin > 0 then "min" else "sec"
    val card = new Panel:
      background = cardBg
      opaque = true
      preferredSize = new Dimension(100, 56)
      maximumSize  = new Dimension(100, 56)
      minimumSize  = new Dimension(80, 48)
      cursor = new Cursor(Cursor.HAND_CURSOR)
      border = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(cardBorder, 1, true),
        BorderFactory.createEmptyBorder(6, 8, 6, 8)
      )
      peer.setLayout(new java.awt.BorderLayout)
      peer.add(new Label(labelText) {
        font = new Font("SansSerif", Font.BOLD, 14)
        foreground = accentColor
        horizontalAlignment = Alignment.Center
      }.peer, java.awt.BorderLayout.CENTER)
      val descRow = new Label(descText):
        font = new Font("SansSerif", Font.PLAIN, 10)
        foreground = subtitleFg
        horizontalAlignment = Alignment.Center
      peer.add(descRow.peer, java.awt.BorderLayout.SOUTH)

    card.peer.addMouseListener(new MouseAdapter:
      override def mouseEntered(e: MouseEvent): Unit =
        card.background = cardHover
        card.repaint()
      override def mouseExited(e: MouseEvent): Unit =
        card.background = cardBg
        card.repaint()
      override def mouseClicked(e: MouseEvent): Unit =
        onStart(Some(tc))
    )
    card

  // --- Helper: category section ---
  private def categorySection(cat: Category): BoxPanel =
    val section = new BoxPanel(Orientation.Vertical):
      background = bg
      opaque = true

    val headerRow = new BoxPanel(Orientation.Horizontal):
      background = bg
      opaque = true
      contents += new Label(s"${cat.icon}  ${cat.name}"):
        font = new Font("SansSerif", Font.BOLD, 14)
        foreground = cat.color
        horizontalAlignment = Alignment.Left
      contents += Swing.HGlue

    headerRow.xLayoutAlignment = 0.0

    val cardsRow = new Panel:
      background = bg
      opaque = true
      peer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4))
      peer.setBackground(bg)
      for tc <- cat.presets do
        peer.add(tcCard(tc, cat.color).peer)

    section.contents += headerRow
    section.contents += Swing.VStrut(4)
    section.contents += Component.wrap(cardsRow.peer)
    section

  // --- "No clock" button ---
  private val noClockButton = new Button("Ohne Uhr spielen"):
    font = new Font("SansSerif", Font.BOLD, 14)
    foreground = playBtnFg
    background = playBtnBg
    opaque = true
    borderPainted = false
    focusPainted = false
    cursor = new Cursor(Cursor.HAND_CURSOR)
    preferredSize = new Dimension(200, 42)
    maximumSize  = new Dimension(200, 42)
  noClockButton.xLayoutAlignment = 0.5
  listenTo(noClockButton)
  reactions += { case event.ButtonClicked(`noClockButton`) => onStart(None) }

  // --- Assemble ---
  contents += heroIcon
  contents += Swing.VStrut(8)
  contents += heroTitle
  contents += Swing.VStrut(6)
  contents += heroSubtitle
  contents += Swing.VStrut(32)
  for cat <- categories do
    contents += categorySection(cat)
    contents += Swing.VStrut(20)
  contents += Swing.VStrut(8)
  contents += noClockButton
  contents += Swing.VGlue
