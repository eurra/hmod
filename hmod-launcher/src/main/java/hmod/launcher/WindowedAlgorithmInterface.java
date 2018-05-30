
package hmod.launcher;

import hmod.core.AlgorithmException;
import hmod.core.Procedure;
import hmod.core.StackControl;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import optefx.util.output.OutputConfig;

/**
 *
 * @author Enrique Urra C.
 */
class WindowedAlgorithmInterface extends JDialog implements AlgorithmInterface
{
    //private AlgorithmFrameWriter writer;
    private StackControl rc;
    private final boolean autoClose;
    private boolean closed;
    private final String algorithmName;
    
    public WindowedAlgorithmInterface(String name, boolean threading, boolean autoClose)
    {        
        initComponents();
        DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.algorithmName = name;
        updateTitle(null);
        
        if(!threading)
            setModalityType(ModalityType.APPLICATION_MODAL);
        
        //this.writer = new AlgorithmFrameWriter(jTextArea1.getDocument());
        //this.writer = new AlgorithmFrameWriter(jTextArea1);
        //this.writer = new StringWriter();
        this.autoClose = autoClose;
    }
    
    private void updateTitle(String state)
    {
        setTitle((state == null ? "" : "(" + state +  ")") + " Algorithm execution: '" + algorithmName + "'");
    }
    
    /*private class InnerWorker extends SwingWorker<Void, String>
    {
        public Consumer<String> getHandler()
        {
            return (s) -> publish(s);
        }

        @Override
        protected void process(List<String> chunks)
        {
            for(String chunk : chunks)
                jTextArea1.append(chunk);
            
            if(jTextArea1.getLineCount() > 1000)
            {
                try
                {
                    int lastOffset = jTextArea1.getLineEndOffset(jTextArea1.getLineCount() - 1000);
                    jTextArea1.replaceRange("", 0, lastOffset);
                }
                catch(BadLocationException ex)
                {
                    throw new AlgorithmException(ex);
                }
            }
        }
        
        @Override
        protected Void doInBackground() throws Exception
        {
            SwingUtilities.invokeLater(() -> updateTitle("RUNNING"));
            
            algorithm.run();          
            
            SwingUtilities.invokeLater(() -> {
                WindowedAlgorithmInterface.this.jButton1.setText("Stopped");
                WindowedAlgorithmInterface.this.jButton1.setEnabled(false);

                updateTitle("FINISHED");

                if(autoClose && !WindowedAlgorithmInterface.this.closed)
                    WindowedAlgorithmInterface.this.dispatchEvent(new WindowEvent(WindowedAlgorithmInterface.this, WindowEvent.WINDOW_CLOSING));
            });
            
            return null;
        }
    }*/

    @Override
    public Runnable configure(Procedure algorithm, OutputConfig outputConfigBuilder)
    {
        rc = StackControl.startNew();
        
        /*Consumer<String> handle = (s) -> {
            try
            {   
                SwingUtilities.invokeAndWait(() -> {
                    //jTextArea1.append(s);
                    Document doc = jTextArea1.getDocument();
                    
                    try
                    {
                        doc.insertString(doc.getLength(), s, null);
                    }
                    catch(BadLocationException ex)
                    {
                        throw new AlgorithmException(ex);
                    }

                    if(jTextArea1.getLineCount() > 1000)
                    {
                        try
                        {
                            int lastOffset = jTextArea1.getLineEndOffset(jTextArea1.getLineCount() - 1000);
                            jTextArea1.replaceRange("", 0, lastOffset);
                        }
                        catch(BadLocationException ex)
                        {
                            throw new AlgorithmException(ex);
                        }
                    }
                });
            }
            catch(InterruptedException | InvocationTargetException ex)
            {
            }
        };
        
        outputConfigBuilder.setSystemOutput(new AlgorithmFrameWriter(handle));*/
        outputConfigBuilder.setSystemOutput(new AlgorithmFrameWriter(jTextArea1));
        
        return () -> {
            SwingUtilities.invokeLater(() -> updateTitle("RUNNING"));
            
            algorithm.run(rc);          
            
            SwingUtilities.invokeLater(() -> {
                WindowedAlgorithmInterface.this.jButton1.setText("Stopped");
                WindowedAlgorithmInterface.this.jButton1.setEnabled(false);

                updateTitle("FINISHED");

                if(autoClose && !WindowedAlgorithmInterface.this.closed)
                    WindowedAlgorithmInterface.this.dispatchEvent(new WindowEvent(WindowedAlgorithmInterface.this, WindowEvent.WINDOW_CLOSING));
            });
        };
    }

    @Override
    public void start(Thread launcherThread)
    {
        try
        {
            SwingUtilities.invokeAndWait(() -> setVisible(true));
        }
        catch(InterruptedException | InvocationTargetException ex)
        {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Output:");

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTextArea1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Stop execution");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Auto-scroll");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 781, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBox1)
                        .addGap(583, 583, 583)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jCheckBox1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_jCheckBox1ItemStateChanged
    {//GEN-HEADEREND:event_jCheckBox1ItemStateChanged
        DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret();
        
        if(jCheckBox1.isSelected())
        {
            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
        else
        {
            caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        closed = true;
        
        try
        {
            rc.stop();
        }
        catch(AlgorithmException ex)
        {
        }
        
        //algorithmThread.interrupt();
        //algorithmThread.interrupt();
    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        jButton1.setText("Stopping...");
        jButton1.setEnabled(false);
        
        try
        {
            rc.stop();
        }
        catch(AlgorithmException ex)
        {
        }
        
        //algorithmThread.interrupt();
        //algorithmThread.interrupt();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextArea1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTextArea1MouseClicked
    {//GEN-HEADEREND:event_jTextArea1MouseClicked
        if(jCheckBox1.isSelected())
        {
            DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret();
            caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
            jCheckBox1.setSelected(false);
        }
    }//GEN-LAST:event_jTextArea1MouseClicked
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
