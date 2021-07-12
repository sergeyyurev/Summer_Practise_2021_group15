package gui.listeners

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout
import gui.Drawer
import gui.listeners.DrawerButtonAction
import java.awt.event.ActionEvent

class SortListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {
        mxHierarchicalLayout(drawer.graph).execute(drawer.graph.defaultParent)

        if (e != null)
            drawer.paint()
    }
}