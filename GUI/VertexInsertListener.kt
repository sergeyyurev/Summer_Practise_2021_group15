import com.mxgraph.swing.mxGraphComponent
import java.awt.event.MouseEvent

class VertexInsertListener(drawer: Drawer, component: mxGraphComponent) : DrawerMouseAction(drawer, component) {
    override fun mousePressed(e: MouseEvent) {
        val parent = drawer.graph.defaultParent
        val isCorrectEvent = e.clickCount == 2 && !isShiftDown(e)
        val isThereCell = component.getCellAt(e.x, e.y) != null
        if (!isCorrectEvent || isThereCell) return

        val sizeOfVertex = drawer.sizeOfVertex
        val style = drawer.styleOfVertex

        val x = e.x.toDouble() - sizeOfVertex / 2
        val y = e.y.toDouble() - sizeOfVertex / 2
        val isCorrectCoordinates = (x >= 0) && (y >= 0)
        if (!isCorrectCoordinates) return

        drawer.graph.model.beginUpdate()
        try {
            val index = drawer.getNextId()
            drawer.graph.insertVertex(parent, index, index, x, y, sizeOfVertex, sizeOfVertex, style)
        } finally {
            drawer.graph.model.endUpdate()
        }

        // TODO("Interact with AlgState")
    }
}