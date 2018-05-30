package hmod.launcher;

import hmod.core.AlgorithmFactory;
import hmod.core.Evaluation;
import hmod.core.MethodBridges;
import hmod.core.Ref;

public interface RunnerRegistryRef extends Ref<RunnerRegistry> {
    default Evaluation<RunnerRegistry> addFactories(Ref<? extends AlgorithmRunnerFactory>... factories) {
        return Evaluation.giveName("RunnerRegistry.addFactories", MethodBridges.eval(RunnerRegistry::addFactories, this, AlgorithmFactory.arrayOf(AlgorithmRunnerFactory[].class, factories)));
    }
}
