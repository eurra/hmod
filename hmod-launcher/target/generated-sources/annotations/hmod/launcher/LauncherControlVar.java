package hmod.launcher;

import hmod.core.MethodBridges;
import hmod.core.Ref;
import hmod.core.SetVarStatement;
import hmod.core.SimpleVar;

public class LauncherControlVar extends SimpleVar<LauncherControl> implements LauncherControlRef {
    public LauncherControlVar() {
    }

    public LauncherControlVar(String name) {
        super(name);
    }

    public SetVarStatement<LauncherControl> setNew(Ref<? extends Boolean> debugEnabled, Ref<? extends Boolean> threadingEnabled, Ref<? extends String> defBatchFolder, Ref<? extends String> defOutputFolder) {
        return MethodBridges.set(this, LauncherControl::new, debugEnabled, threadingEnabled, defBatchFolder, defOutputFolder);
    }
}
