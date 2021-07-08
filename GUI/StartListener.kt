import java.awt.event.ActionEvent
import com.mxgraph.model.mxCell
import javax.swing.JOptionPane


class StartListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {

        val cells = drawer.graph.getChildCells(drawer.graph.defaultParent)
        for (cell in cells) {
            if (cell !is mxCell || !cell.isVertex) continue

            if (cell.edgeCount == 0) {
                JOptionPane.showMessageDialog(drawer, "В графе присутствуют одинокие вершины!")
                return
            }
        }

        drawer.strategyOfMenuCreation = WorkingMenuCreation()
        drawer.paint()
    }
}