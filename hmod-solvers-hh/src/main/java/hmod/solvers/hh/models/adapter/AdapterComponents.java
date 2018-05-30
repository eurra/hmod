
package hmod.solvers.hh.models.adapter;

import hmod.solvers.hh.HHSolution;
import optefx.loader.Processable;

/**
 *
 * @author Enrique Urra C.
 */
public class AdapterComponents<T extends HHSolution, K>
{   
    private HHSolutionDecoder<T, K> decoder;
    private HHSolutionEncoder<T, K> encoder;
    private HHSolutionDownloader<K> downloader;
    private HHSolutionUploader<K> uploader;

    AdapterComponents()
    {
    }

    public void setDecoder(HHSolutionDecoder<T, K> decoder)
    {
        this.decoder = decoder;
    }
    
    public void setEncoder(HHSolutionEncoder<T, K> encoder)
    {
        this.encoder = encoder;
    }

    public void setDownloader(HHSolutionDownloader<K> downloader)
    {
        this.downloader = downloader;
    }

    public void setUploader(HHSolutionUploader<K> uploader)
    {
        this.uploader = uploader;
    }

    HHSolutionDecoder<T, K> getDecoder()
    {
        return decoder;
    }

    HHSolutionEncoder<T, K> getEncoder()
    {
        return encoder;
    }

    HHSolutionDownloader<K> getDownloader()
    {
        return downloader;
    }

    HHSolutionUploader<K> getUploader()
    {
        return uploader;
    }
    
    @Processable
    private void validate()
    {
        if(decoder == null) throw new IllegalStateException("decoder not set");
        if(encoder == null) throw new IllegalStateException("encoder not set");
        if(downloader == null) throw new IllegalStateException("downloader not set");
        if(uploader == null) throw new IllegalStateException("uploader not set");
    }
}
