package hmod.launcher;

import hmod.core.MethodBridges;
import hmod.core.Ref;
import hmod.core.SetVarStatement;
import hmod.core.SimpleVar;

public class InterfaceRegistryVar extends SimpleVar<InterfaceRegistry> implements InterfaceRegistryRef {
    public InterfaceRegistryVar() {
    }

    public InterfaceRegistryVar(String name) {
        super(name);
    }

    public SetVarStatement<InterfaceRegistry> setNew(Ref<? extends String> defaultId) {
        return MethodBridges.set(this, InterfaceRegistry::new, defaultId);
    }
}
