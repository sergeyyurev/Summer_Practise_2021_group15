import com.mxgraph.swing.mxGraphComponent
import javax.swing.JButton
import javax.swing.JPanel


class WorkingMenuCreation : DisplayCreationStrategy {
    override fun createMenu(drawer: Drawer): JPanel {
        val panel = getBaseMenu()

        val back = JButton("Назад")
        val forward = JButton("Вперед")
        val refresh = JButton("Перезапуск")
        val stop = JButton("Стоп").apply { addActionListener(StopListener(drawer)) }

        panel.add(back)
        panel.add(forward)
        panel.add(refresh)
        panel.add(stop)
        return panel
    }

    override fun createGraphComponent(drawer: Drawer): mxGraphComponent {
        val component = mxGraphComponent(drawer.graph)
        component.isEnabled = false
        return component
    }
}