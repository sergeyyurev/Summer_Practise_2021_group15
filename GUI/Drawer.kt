package gui

import gui.strategies.DisplayCreationStrategy
import gui.strategies.InputMenuCreation
import algorithm.AlgHandler
import com.mxgraph.model.mxCell
import com.mxgraph.view.mxGraph
import gui.listeners.AddCellListener
import gui.listeners.RemoveCellListener
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*


class Drawer : JFrame("Nobody expects the spanish inquisition") {
    var algHandler = AlgHandler<String>()

    val graph: mxGraph
    var strategyOfMenuCreation: DisplayCreationStrategy = InputMenuCreation()
    val sizeOfVertex = 35.0
    val styleOfVertex = "rounded=1"
    private var idOfNextVertex = 'A'

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        preferredSize = Dimension(720, 480)
        isResizable = false
        setSize(720, 480)

        graph = object : mxGraph() {
            override fun isCellSelectable(cell: Any): Boolean {
                val isEdge = cell is mxCell && cell.isEdge
                return if (isEdge) false else super.isCellSelectable(cell)
            }
        }

        setupGraphSettings()
        paint()
    }

    private fun setupGraphSettings() {
        graph.addListener("cellsAdded", AddCellListener(this))
        graph.addListener("cellsRemoved", RemoveCellListener(this))

        graph.isMultigraph = false
        graph.isAllowLoops = false
        graph.isPortsEnabled = false
        graph.isAllowDanglingEdges = false
        graph.isEdgeLabelsMovable = false
        graph.isCellsEditable = false
        graph.isCellsResizable = false
    }

    fun getVertexById(id: String): mxCell? {
        val cells = graph.getChildCells(graph.defaultParent)
        for (cell in cells) {
            if (cell is mxCell) {
                if (cell.id == id) return cell
            }
        }
        return null
    }

    fun getNextId(id: String? = null): String {
        if (id != null)
            getVertexById(id) ?: return id

        while (getVertexById(idOfNextVertex.toString()) != null)
            idOfNextVertex++

        return idOfNextVertex.toString()
    }

    fun setNextId(id: Char) {
        idOfNextVertex = minOf(idOfNextVertex, id)
    }

    private fun createTopMenu(): JPanel {
        val layout = BorderLayout()
        val panel = JPanel(layout)
        panel.add(strategyOfMenuCreation.createMenu(this), BorderLayout.WEST)
        return panel
    }

    private fun createLoggingPane(): JScrollPane {
        val text = JTextArea(algHandler.logs, 20, 20)
        text.isEditable = false
        val scroll = JScrollPane(text)

        scroll.verticalScrollBar.value = scroll.verticalScrollBar.maximum
        return scroll
    }

    fun paint() {
        contentPane.removeAll()
        val panel = JPanel(BorderLayout())
        panel.add(createTopMenu(), BorderLayout.NORTH)
        panel.add(createLoggingPane(), BorderLayout.WEST)
        panel.add(strategyOfMenuCreation.createGraphComponent(this), BorderLayout.CENTER)
        contentPane.add(panel)
        pack()
    }
}


fun main() {
    System.setProperty("java.awt.headless", "false")
    Drawer()
}
