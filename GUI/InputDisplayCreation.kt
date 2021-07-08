import com.mxgraph.swing.mxGraphComponent
import javax.swing.JButton
import javax.swing.JPanel


class InputMenuCreation : DisplayCreationStrategy {
    override fun createMenu(drawer: Drawer): JPanel {
        val panel = getBaseMenu()

        val start = JButton("Старт").apply { addActionListener(StartListener(drawer)) }
        val sort = JButton("Упорядочить").apply { addActionListener(SortListener(drawer)) }
        val read = JButton("Добавить из файла").apply { addActionListener(FileInputListener(drawer)) }
        val discharge = JButton("Сброс").apply { addActionListener(DischargeListener(drawer)) }

        panel.add(start)
        panel.add(sort)
        panel.add(read)
        panel.add(discharge)

        return panel
    }

    override fun createGraphComponent(drawer: Drawer): mxGraphComponent {
        val component = mxGraphComponent(drawer.graph)
        component.graphControl.addMouseListener(VertexRemoveListener(drawer, component))
        component.graphControl.addMouseListener(VertexInsertListener(drawer, component))
        return component
    }
}
