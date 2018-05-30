package hmod.launcher;

import hmod.core.MethodBridges;
import hmod.core.Ref;
import hmod.core.SetVarStatement;
import hmod.core.SimpleVar;

public class RunnerRegistryVar extends SimpleVar<RunnerRegistry> implements RunnerRegistryRef {
    public RunnerRegistryVar() {
    }

    public RunnerRegistryVar(String name) {
        super(name);
    }

    public SetVarStatement<RunnerRegistry> setNew(Ref<? extends String> defaultId) {
        return MethodBridges.set(this, RunnerRegistry::new, defaultId);
    }
}
