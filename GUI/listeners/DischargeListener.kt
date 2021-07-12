package gui.listeners

import gui.Drawer
import java.awt.event.ActionEvent

class DischargeListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {
        val parent = drawer.graph.defaultParent
        drawer.graph.model.beginUpdate()
        try {
            drawer.graph.removeCells(drawer.graph.getChildCells(parent))
        } finally {
            drawer.graph.model.endUpdate()
        }

        if (e != null)
            drawer.paint()
    }
}