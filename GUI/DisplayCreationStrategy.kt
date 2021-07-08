import com.mxgraph.swing.mxGraphComponent
import java.awt.FlowLayout
import javax.swing.JPanel

interface DisplayCreationStrategy {
    fun getBaseMenu(): JPanel {
        val layout = FlowLayout(FlowLayout.LEFT)
        return JPanel(layout)
    }

    fun createMenu(drawer: Drawer): JPanel
    fun createGraphComponent(drawer: Drawer): mxGraphComponent
}