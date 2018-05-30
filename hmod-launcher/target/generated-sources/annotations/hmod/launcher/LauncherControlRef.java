package hmod.launcher;

import hmod.core.Evaluation;
import hmod.core.MethodBridges;
import hmod.core.Ref;
import hmod.core.Statement;

public interface LauncherControlRef extends Ref<LauncherControl> {
    default Statement enableDebugging() {
        return Statement.giveName("LauncherControl.enableDebugging", MethodBridges.run(LauncherControl::enableDebugging, this));
    }

    default Evaluation<Boolean> isRunning() {
        return Evaluation.giveName("LauncherControl.isRunning", MethodBridges.eval(LauncherControl::isRunning, this));
    }
}
