package gui.listeners

import gui.Drawer
import java.awt.event.ActionEvent


class BackListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {
        drawer.algHandler.restorePrevState()
        drawer.paint()
    }
}