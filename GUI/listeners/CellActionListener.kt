package gui.listeners

import gui.Drawer
import algorithm.CurrentStage
import com.mxgraph.model.mxCell
import com.mxgraph.util.mxEventObject
import com.mxgraph.util.mxEventSource

class AddCellListener(val drawer: Drawer) : mxEventSource.mxIEventListener {
    override fun invoke(sender: Any, event: mxEventObject) {

        if (drawer.algHandler.getCurrentStage() != CurrentStage.INITIALISATION)
            return

        val cells = event.getProperty("cells") as Array<*>

        for (cell in cells) {
            val element = cell as mxCell
            if (element.isVertex) {
                drawer.algHandler.addNode(element.id)
            } else if (element.isEdge) {
                drawer.algHandler.addEdge(element.source.id, element.target.id)
            }
        }

    }
}


class RemoveCellListener(val drawer: Drawer) : mxEventSource.mxIEventListener {
    override fun invoke(sender: Any, event: mxEventObject) {

        if (drawer.algHandler.getCurrentStage() != CurrentStage.INITIALISATION)
            return

        val cells = event.getProperty("cells") as Array<*>

        for (cell in cells) {
            val element = cell as mxCell
            if (element.isVertex) {
                drawer.algHandler.removeNode(element.id)
                drawer.setNextId(cell.id[0])
            } else if (element.isEdge && element.source != null && element.target != null) {
                drawer.algHandler.removeEdge(element.source.id, element.target.id)
            }
        }

    }
}
