package hmod.launcher;

import hmod.core.MethodBridges;
import hmod.core.Ref;
import hmod.core.SetVarStatement;
import hmod.core.SimpleVar;

class LauncherInputVar extends SimpleVar<LauncherInput> implements LauncherInputRef {
    public LauncherInputVar() {
    }

    public LauncherInputVar(String name) {
        super(name);
    }

    public SetVarStatement<LauncherInput> setNew(Ref<? extends LauncherControl> lh, Ref<? extends CommandParser> cph, Ref<? extends CommandRegistry> cr) {
        return MethodBridges.set(this, LauncherInput::new, lh, cph, cr);
    }
}
