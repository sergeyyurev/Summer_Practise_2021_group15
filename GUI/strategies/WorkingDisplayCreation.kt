package gui.strategies

import algorithm.CurrentStage
import com.mxgraph.swing.mxGraphComponent
import gui.Drawer
import gui.listeners.*
import javax.swing.JButton
import javax.swing.JPanel


abstract class PaintNodesStrategy(val drawer: Drawer) {
    abstract fun paint()
}


class CompletedGraph(drawer: Drawer) : PaintNodesStrategy(drawer) {

    private val colors = arrayOf("green", "blue", "black", "white", "orange", "yellow", "navy", "purple")

    override fun paint() {
        val parent = drawer.graph.defaultParent
        val connectedComponents = drawer.algHandler.getConnectComponents()

        for (indexOfComponent in connectedComponents.indices) {
            val styleOfVertex = "rounded=1;fillColor=${colors[indexOfComponent % colors.size]}"

            for (node in connectedComponents[indexOfComponent]) {
                drawer.graph.model.beginUpdate()
                try {
                    drawer.graph.insertVertex(parent, node, node, 0.0, 0.0, drawer.sizeOfVertex, drawer.sizeOfVertex, styleOfVertex)
                } finally {
                    drawer.graph.model.endUpdate()
                }
            }
        }
    }
}


class DefaultGraph(drawer: Drawer) : PaintNodesStrategy(drawer) {

    private val nodeToHandle = drawer.algHandler.getStackHead()
    private val handledNodes = drawer.algHandler.getHandeledNodes()
    private val nodes = drawer.algHandler.getNodes()

    override fun paint() {
        val parent = drawer.graph.defaultParent
        for (node in nodes) {
            drawer.graph.model.beginUpdate()
            var styleOfVertex = drawer.styleOfVertex

            if (node == nodeToHandle)
                styleOfVertex = "rounded=1;fillColor=red"
            else if (node in handledNodes)
                styleOfVertex = "rounded=1;fillColor=green"

            try {
                drawer.graph.insertVertex(parent, node, node, 0.0, 0.0, drawer.sizeOfVertex, drawer.sizeOfVertex, styleOfVertex)
            } finally {
                drawer.graph.model.endUpdate()
            }
        }
    }
}


class WorkingMenuCreation : DisplayCreationStrategy {

    override fun createMenu(drawer: Drawer): JPanel {
        val panel = getBaseMenu()

        val back = JButton("Назад").apply { addActionListener(BackListener(drawer)) }
        val forward = JButton("Вперед").apply { addActionListener(ForwardListener(drawer)) }
        val refresh = JButton("Перезапуск").apply { addActionListener(RefreshListener(drawer)) }
        val stop = JButton("Стоп").apply { addActionListener(StopListener(drawer)) }

        if (drawer.algHandler.getCurrentStage() == CurrentStage.COMPLETED)
            forward.isEnabled = false

        if (drawer.algHandler.getCurrentStage() == CurrentStage.INITIALISATION)
            back.isEnabled = false

        panel.add(back)
        panel.add(forward)
        panel.add(refresh)
        panel.add(stop)
        return panel
    }

    override fun createGraphComponent(drawer: Drawer): mxGraphComponent {
        val component = mxGraphComponent(drawer.graph)
        val edges = drawer.algHandler.getEdges()

        val paintNodesStrategy = if (drawer.algHandler.getCurrentStage() == CurrentStage.COMPLETED)
            CompletedGraph(drawer) else DefaultGraph(drawer)

        DischargeListener(drawer).actionPerformed(null)

        val parent = drawer.graph.defaultParent
        paintNodesStrategy.paint()

        for (edge in edges) {

            drawer.graph.model.beginUpdate()
            try {
                val v1 = drawer.getVertexById(edge.first)
                val v2 = drawer.getVertexById(edge.second)
                drawer.graph.insertEdge(parent, null, "", v1, v2)
            } finally {
                drawer.graph.model.endUpdate()
            }

        }

        SortListener(drawer).actionPerformed(null)
        component.isEnabled = false
        return component
    }
}