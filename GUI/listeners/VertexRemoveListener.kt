package gui.listeners

import com.mxgraph.model.mxCell
import com.mxgraph.swing.mxGraphComponent
import gui.Drawer
import gui.listeners.DrawerMouseAction
import java.awt.event.MouseEvent

class VertexRemoveListener(drawer: Drawer, component: mxGraphComponent) : DrawerMouseAction(drawer, component) {
    override fun mousePressed(e: MouseEvent) {
        val isCorrectEvent = e.clickCount == 2 && isShiftDown(e)
        val isThereCell = component.getCellAt(e.x, e.y) != null
        if (!isCorrectEvent || !isThereCell) return

        val cell: Any = component.getCellAt(e.x, e.y) ?: return
        if (cell !is mxCell) return

        drawer.graph.model.beginUpdate()
        try {
            drawer.graph.removeCells(arrayOf(cell))
        } finally {
            drawer.graph.model.endUpdate()
        }
    }
}