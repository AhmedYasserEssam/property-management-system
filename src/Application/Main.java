package Application;

import java.awt.GraphicsEnvironment;

import PresentationLayer.UI.MainDashboardUI;

public class Main {

    public static void main(String[] args) {
        ApplicationFactory factory = new ApplicationFactory();

        if (GraphicsEnvironment.isHeadless()) {
            MainDashboardUI dashboardUI = factory.createMainDashboardUI();
            dashboardUI.openDashboard();
            return;
        }

        factory.createDesktopDashboardUI().open();
    }
}
