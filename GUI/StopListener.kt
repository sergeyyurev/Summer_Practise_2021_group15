import java.awt.event.ActionEvent

class StopListener(drawer: Drawer) : DrawerButtonAction(drawer) {
    override fun actionPerformed(e: ActionEvent?) {
        drawer.strategyOfMenuCreation = InputMenuCreation()
        drawer.paint()

        // TODO("AlgHandler interact")
    }

}