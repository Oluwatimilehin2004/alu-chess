package chess.aview.gui

import chess.controller.ControllerInterface
import chess.util.Observer

import scala.swing.*
import java.awt.{Color as AwtColor, Dimension}
import javax.swing.{UIManager, WindowConstants}

/** Main GUI window – lichess-inspired modern dark theme.
  * Registers as Observer on the Controller, same pattern as TUI. */
class SwingGUI(controller: ControllerInterface) extends Frame with Observer:
  controller.add(this)

  // --- Look & Feel ---
  try
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName)
  catch case _: Exception => () // fallback to default

  title = "alu-chess"
  resizable = false

  private val boardPanel = BoardPanel(controller)
  private val sidePanel = SidePanel(
    controller,
    onNewGame = () => controller.newGame(),
    onQuit = () => { dispose(); controller.quit() }
  )

  private val sideScroll = new ScrollPane(sidePanel):
    horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
    verticalScrollBarPolicy = ScrollPane.BarPolicy.AsNeeded
    border = Swing.EmptyBorder(0, 0, 0, 0)
    preferredSize = new Dimension(332, 0)

  contents = new BorderPanel:
    background = new AwtColor(38, 36, 33)
    layout(boardPanel) = BorderPanel.Position.Center
    layout(sideScroll) = BorderPanel.Position.East

  peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  pack()
  centerOnScreen()
  visible = true

  // Initial display
  sidePanel.refresh()

  override def update(): Unit =
    boardPanel.refresh()
    sidePanel.refresh()
