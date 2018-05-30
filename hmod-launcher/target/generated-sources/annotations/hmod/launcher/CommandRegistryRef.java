package hmod.launcher;

import hmod.core.AlgorithmFactory;
import hmod.core.Evaluation;
import hmod.core.MethodBridges;
import hmod.core.Ref;

public interface CommandRegistryRef extends Ref<CommandRegistry> {
    default Evaluation<CommandRegistry> addCommands(Ref<? extends Command>... cmds) {
        return Evaluation.giveName("CommandRegistry.addCommands", MethodBridges.eval(CommandRegistry::addCommands, this, AlgorithmFactory.arrayOf(Command[].class, cmds)));
    }
}
