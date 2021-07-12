package gui.listeners

import gui.strategies.InputMenuCreation
import gui.Drawer
import java.awt.event.ActionEvent


class StopListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {
        drawer.algHandler.refresh()
        drawer.paint()

        drawer.strategyOfMenuCreation = InputMenuCreation()
        drawer.paint()
    }
}