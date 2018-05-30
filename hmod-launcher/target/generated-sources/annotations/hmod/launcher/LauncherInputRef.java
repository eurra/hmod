package hmod.launcher;

import hmod.core.MethodBridges;
import hmod.core.Ref;
import hmod.core.Statement;

interface LauncherInputRef extends Ref<LauncherInput> {
    default Statement readScriptCommand(Ref<? extends String> script) {
        return Statement.giveName("LauncherInput.readScriptCommand", MethodBridges.run(LauncherInput::readScriptCommand, this, script));
    }

    default Statement readConsoleCommand() {
        return Statement.giveName("LauncherInput.readConsoleCommand", MethodBridges.run(LauncherInput::readConsoleCommand, this));
    }
}
