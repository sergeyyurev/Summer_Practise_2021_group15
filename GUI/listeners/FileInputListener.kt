package gui.listeners

import gui.Drawer
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout
import com.mxgraph.model.mxCell
import gui.utils.Reader
import java.awt.event.ActionEvent
import javax.swing.JFileChooser


class FileInputListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {


        val chooser = JFileChooser()
        val ret = chooser.showDialog(null, "Открыть файл")
        val data: Pair<MutableSet<String>, MutableSet<Pair<String, String>>>
        if (ret == JFileChooser.APPROVE_OPTION) {
            data = Reader.readingFromFile(chooser.selectedFile)
        } else return

        val parent = drawer.graph.defaultParent
        val size = drawer.sizeOfVertex
        val style = drawer.styleOfVertex

        drawer.graph.model.beginUpdate()
        try {

            val mapOfVertexes = mutableMapOf<String, mxCell>()

            for (vertex in data.first) {
                val index = drawer.getNextId(vertex)
                val v = drawer.graph.insertVertex(parent, index, index, 0.0, 0.0, size, size, style)
                if (v is mxCell)
                    mapOfVertexes[vertex] = v
            }

            for (edge in data.second) {
                val v1 = mapOfVertexes[edge.first]
                val v2 = mapOfVertexes[edge.second]
                drawer.graph.insertEdge(parent, null, "", v1, v2)
            }

        } finally {
            drawer.graph.model.endUpdate()
        }

        mxHierarchicalLayout(drawer.graph).execute(parent)

        // TODO("Interact with AlgState")
    }
}