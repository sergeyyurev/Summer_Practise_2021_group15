import com.mxgraph.model.mxCell
import com.mxgraph.view.mxGraph
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*


class Reader {
    // Класс представляет Mock файлового ввода

    companion object {
        @JvmStatic
        fun readFromFile(): Pair<ArrayList<String>, ArrayList<Pair<String, String>>> {
            val vertexes = ArrayList<String>()
            vertexes.add("A")
            vertexes.add("B")
            vertexes.add("C")
            vertexes.add("D")

            val edges = ArrayList<Pair<String, String>>()
            edges.add(Pair("A", "B"))
            edges.add(Pair("B", "C"))
            edges.add(Pair("C", "D"))
            edges.add(Pair("D", "C"))
            edges.add(Pair("C", "A"))

            return Pair(vertexes, edges)
        }
    }
}


class Drawer : JFrame("Nobody expects the spanish inquisition") {
    val graph: mxGraph
    var strategyOfMenuCreation: DisplayCreationStrategy = InputMenuCreation()

    val sizeOfVertex = 35.0
    val styleOfVertex = "rounded=1"
    private var idOfNextVertex = 'A'

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        preferredSize = Dimension(720, 480)
        setSize(720, 480)

        graph = object : mxGraph() {
            override fun isCellSelectable(cell: Any): Boolean {
                val isEdge = cell is mxCell && cell.isEdge
                return if (isEdge) false else super.isCellSelectable(cell)
            }
        }
        setupGraphAllows()
        paint()
    }

    private fun setupGraphAllows() {
        graph.isMultigraph = false
        graph.isAllowLoops = false
        graph.isPortsEnabled = false
        graph.isAllowDanglingEdges = false
        graph.isEdgeLabelsMovable = false
        graph.isCellsEditable = false
        graph.isCellsResizable = false
    }

    private fun getVertexById(id: String): mxCell? {
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
        val text = JTextArea("> Intermediate\ninformation and logging", 20, 20)
        text.isEditable = false
        return JScrollPane(text)
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
