
package hmod.solvers.hh.models.adapter;

import hmod.core.Statement;
import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.HHSolutionHandler;

/**
 *
 * @author Enrique Urra C.
 */
class AdapterOperators
{
    private final HHSolutionHandler solutionData;
    private final AdapterSolutionHandler baSolutionData;

    public AdapterOperators(HHSolutionHandler solutionData, AdapterSolutionHandler baSolutionData)
    {
        this.solutionData = solutionData;
        this.baSolutionData = baSolutionData;
    }
    
    public Statement decode(HHSolutionDecoder decoder)
    {
        return () -> {
            HHSolution input = solutionData.getInputSolution();

            if(input != null)
            {
                Object decodedInput = decoder.decode(input);
                baSolutionData.storeDecodedSolution(decodedInput);
            }
        };
    }
    
    public Statement encode(HHSolutionEncoder encoder)
    {
        return () -> {
            Object output = baSolutionData.retrieveSolutionToEncode();
            HHSolution encodedOutput = encoder.encode(output);
            solutionData.setOutputSolution(encodedOutput);
        };
    }
    
    public Statement download(HHSolutionDownloader downloader)
    {
        return () -> {
            Object input = baSolutionData.retrieveDecodedSolution();

            if(input != null)
                downloader.download(input);
        };
    }
    
    public Statement upload(HHSolutionUploader uploader)
    {
        return () -> {
            Object output = uploader.upload();
            baSolutionData.storeSolutionToEncode(output);
        };
    }
}
