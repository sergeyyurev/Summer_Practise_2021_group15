import com.mxgraph.swing.mxGraphComponent
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

abstract class DrawerMouseAction(val drawer: Drawer, val component: mxGraphComponent) : MouseAdapter() {
    protected fun isShiftDown(e: MouseEvent): Boolean {
        return (e.modifiersEx and KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK
    }
}
