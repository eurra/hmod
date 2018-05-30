package hmod.launcher;

import hmod.core.MethodBridges;
import hmod.core.Ref;
import hmod.core.Statement;

class LauncherProcessingOps {
    private static LauncherProcessingOps instance;

    private LauncherProcessingOps() {
    }

    public Statement initLauncher(Ref<? extends LauncherControl> lh, Ref<? extends RandomSeed> rh) {
        return Statement.giveName("LauncherProcessing.initLauncher", MethodBridges.run(LauncherProcessing::initLauncher, lh, rh));
    }

    public Statement showInterface() {
        return Statement.giveName("LauncherProcessing.showInterface", MethodBridges.run(LauncherProcessing::showInterface));
    }

    public Statement finishLauncher() {
        return Statement.giveName("LauncherProcessing.finishLauncher", MethodBridges.run(LauncherProcessing::finishLauncher));
    }

    public static LauncherProcessingOps getInstance() {
        if(instance == null) {
            instance = new LauncherProcessingOps();
        }
        return instance;
    }
}
