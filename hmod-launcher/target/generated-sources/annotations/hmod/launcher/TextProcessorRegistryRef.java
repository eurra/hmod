package hmod.launcher;

import hmod.core.AlgorithmFactory;
import hmod.core.Evaluation;
import hmod.core.MethodBridges;
import hmod.core.Ref;

public interface TextProcessorRegistryRef extends Ref<TextProcessorRegistry> {
    default Evaluation<TextProcessorRegistry> addProcessors(Ref<? extends TextVariableProcessor>... processors) {
        return Evaluation.giveName("TextProcessorRegistry.addProcessors", MethodBridges.eval(TextProcessorRegistry::addProcessors, this, AlgorithmFactory.arrayOf(TextVariableProcessor[].class, processors)));
    }
}
