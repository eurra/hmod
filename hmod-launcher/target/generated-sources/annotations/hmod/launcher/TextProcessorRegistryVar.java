package hmod.launcher;

import hmod.core.MethodBridges;
import hmod.core.SetVarStatement;
import hmod.core.SimpleVar;

public class TextProcessorRegistryVar extends SimpleVar<TextProcessorRegistry> implements TextProcessorRegistryRef {
    public TextProcessorRegistryVar() {
    }

    public TextProcessorRegistryVar(String name) {
        super(name);
    }

    public SetVarStatement<TextProcessorRegistry> setNew() {
        return MethodBridges.set(this, TextProcessorRegistry::new);
    }
}
