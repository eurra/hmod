package hmod.launcher;

import hmod.core.AlgorithmFactory;
import hmod.core.Evaluation;
import hmod.core.MethodBridges;
import hmod.core.Ref;

public interface InterfaceRegistryRef extends Ref<InterfaceRegistry> {
    default Evaluation<InterfaceRegistry> addFactories(Ref<? extends AlgorithmInterfaceFactory>... factories) {
        return Evaluation.giveName("InterfaceRegistry.addFactories", MethodBridges.eval(InterfaceRegistry::addFactories, this, AlgorithmFactory.arrayOf(AlgorithmInterfaceFactory[].class, factories)));
    }
}
