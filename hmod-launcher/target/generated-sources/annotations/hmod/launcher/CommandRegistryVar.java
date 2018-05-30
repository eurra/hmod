package hmod.launcher;

import hmod.core.MethodBridges;
import hmod.core.SetVarStatement;
import hmod.core.SimpleVar;

public class CommandRegistryVar extends SimpleVar<CommandRegistry> implements CommandRegistryRef {
    public CommandRegistryVar() {
    }

    public CommandRegistryVar(String name) {
        super(name);
    }

    public SetVarStatement<CommandRegistry> setNew() {
        return MethodBridges.set(this, CommandRegistry::new);
    }
}
