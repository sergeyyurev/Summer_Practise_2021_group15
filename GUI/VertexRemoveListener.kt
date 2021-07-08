import com.mxgraph.model.mxCell
import com.mxgraph.swing.mxGraphComponent
import java.awt.event.MouseEvent

class VertexRemoveListener(drawer: Drawer, component: mxGraphComponent) : DrawerMouseAction(drawer, component) {
    override fun mousePressed(e: MouseEvent) {
        val isCorrectEvent = e.clickCount == 2 && isShiftDown(e)
        val isThereCell = component.getCellAt(e.x, e.y) != null
        if (!isCorrectEvent || !isThereCell) return

        val cell: Any = component.getCellAt(e.x, e.y) ?: return
        if (cell !is mxCell) return

        // Запишем ID вершины, чтобы заместить им добавление следующей
        if (cell.isVertex)
            drawer.setNextId(cell.id[0])

        drawer.graph.model.beginUpdate()
        try {
            drawer.graph.removeCells(arrayOf(cell))
        } finally {
            drawer.graph.model.endUpdate()
        }

        // TODO("Interact with AlgState")
    }
}