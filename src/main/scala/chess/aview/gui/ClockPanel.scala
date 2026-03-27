package chess.aview.gui

import chess.controller.ControllerInterface
import chess.model.{Color, ChessClock}

import scala.swing.*
import java.awt.{Color as AwtColor, Font, Dimension, Graphics2D, RenderingHints}
import javax.swing.border.EmptyBorder
import javax.swing.BorderFactory

/** Chess clock display showing remaining time for both players. */
class ClockPanel(controller: ControllerInterface) extends BoxPanel(Orientation.Vertical):

  private val panelBg      = new AwtColor(38, 36, 33)
  private val activeTimeBg = new AwtColor(240, 240, 240)
  private val activeTimeFg = new AwtColor(20, 20, 20)
  private val idleTimeBg   = new AwtColor(55, 53, 50)
  private val idleTimeFg   = new AwtColor(180, 180, 180)
  private val lowTimeBg    = new AwtColor(200, 60, 60)
  private val lowTimeFg    = new AwtColor(255, 255, 255)
  private val expiredBg    = new AwtColor(160, 40, 40)

  private val timeFont     = new Font("Monospaced", Font.BOLD, 28)
  private val labelFont    = new Font("SansSerif", Font.PLAIN, 11)

  background = panelBg

  private val blackTimeLabel = createTimeDisplay()
  private val whiteTimeLabel = createTimeDisplay()

  private val blackNameLabel = new Label("Schwarz"):
    font = labelFont
    foreground = new AwtColor(180, 180, 180)
    horizontalAlignment = Alignment.Center
  blackNameLabel.xLayoutAlignment = 0.5

  private val whiteNameLabel = new Label("Weiß"):
    font = labelFont
    foreground = new AwtColor(180, 180, 180)
    horizontalAlignment = Alignment.Center
  whiteNameLabel.xLayoutAlignment = 0.5

  contents += blackNameLabel
  contents += Swing.VStrut(2)
  contents += blackTimeLabel
  contents += Swing.VStrut(10)
  contents += whiteNameLabel
  contents += Swing.VStrut(2)
  contents += whiteTimeLabel

  private def createTimeDisplay(): Label =
    val label = new Label("--:--"):
      font = timeFont
      foreground = idleTimeFg
      background = idleTimeBg
      opaque = true
      horizontalAlignment = Alignment.Center
      preferredSize = new Dimension(180, 46)
      maximumSize = new Dimension(250, 46)
      border = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new AwtColor(70, 68, 65), 1),
        BorderFactory.createEmptyBorder(4, 12, 4, 12)
      )
    label.xLayoutAlignment = 0.5
    label

  def refresh(): Unit =
    controller.clock match
      case Some(clock) =>
        updateTimeLabel(whiteTimeLabel, clock, Color.White)
        updateTimeLabel(blackTimeLabel, clock, Color.Black)
        visible = true
      case None =>
        visible = false

    revalidate()
    repaint()

  private def updateTimeLabel(label: Label, clock: ChessClock, color: Color): Unit =
    val remaining = clock.remainingMs(color)
    label.text = ChessClock.formatTime(remaining)

    val isActive = clock.activeColor.contains(color)
    val isLow = remaining < 30_000 && remaining > 0
    val isExpired = remaining <= 0

    if isExpired then
      label.background = expiredBg
      label.foreground = lowTimeFg
    else if isActive && isLow then
      label.background = lowTimeBg
      label.foreground = lowTimeFg
    else if isActive then
      label.background = activeTimeBg
      label.foreground = activeTimeFg
    else
      label.background = idleTimeBg
      label.foreground = idleTimeFg
