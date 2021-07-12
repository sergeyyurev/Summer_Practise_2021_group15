package gui.listeners

import gui.Drawer
import gui.listeners.DrawerButtonAction
import java.awt.event.ActionEvent


class ForwardListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {
        drawer.algHandler.doAlgStep()
        drawer.paint()
    }
}