
package hmod.solvers.hh.models.adapter;

import static hmod.core.FlowchartFactory.block;
import hmod.core.Routine;
import hmod.core.ValidableRoutine;
import hmod.solvers.hh.HHSolutionHandler;
import hmod.solvers.hh.models.selection.DomainBarrier;
import hmod.solvers.hh.models.soltrack.SolutionTrackingProcesses;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Processable;

/**
 *
 * @author Enrique Urra C.
 */
public final class AdapterBarrier
{  
    @LoadsComponent(AdapterComponents.class)
    public static void loadComponents(ComponentRegister cr)
    {
        cr.provide(new AdapterComponents());
    }
    
    @LoadsComponent({ AdapterBarrier.class })
    public static void loadBarrier(ComponentRegister cr,
                                   AdapterComponents ac,
                                   HHSolutionHandler sh,
                                   SolutionTrackingProcesses stp,
                                   DomainBarrier db)
    {
        AdapterBarrier ab = cr.provide(new AdapterBarrier(ac, sh, stp));  
        
        db.callHeuristic().apply((parent) -> block (
            ab.downwardsTransfer,
            parent,
            ab.upwardsTransfer
        ));
    }
    
    private final ValidableRoutine downwardsTransfer = new ValidableRoutine("AdapterBarrier.downwardsTransfer");
    private final ValidableRoutine upwardsTransfer = new ValidableRoutine("AdapterBarrier.upwardsTransfer");

    private AdapterBarrier(AdapterComponents ac,
                           HHSolutionHandler sh,
                           SolutionTrackingProcesses stp)
    {
        AdapterSolutionHandler ash = new AdapterSolutionHandler();
        AdapterOperators ao = new AdapterOperators(sh, ash);
        
        downwardsTransfer.append(block(
            ao.decode(ac.getDecoder()),
            ao.download(ac.getDownloader())
        ));
        
        upwardsTransfer.append(block(
            ao.upload(ac.getUploader()),
            ao.encode(ac.getEncoder()),
            stp::printOutputSolutionIfIsBest
        ));
    }

    public Routine downwardsTransfer() { return downwardsTransfer; }
    public Routine upwardsTransfer() { return upwardsTransfer; }
    
    @Processable
    private void validate()
    {
        downwardsTransfer.validate();
        upwardsTransfer.validate();
    }
}
