package gui.listeners

import gui.strategies.WorkingMenuCreation
import gui.Drawer
import java.awt.event.ActionEvent


class StartListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {
        drawer.strategyOfMenuCreation = WorkingMenuCreation()
        drawer.paint()
    }
}