package com.csm.systeminfo;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.*;

final public class ClipboardData implements ClipboardOwner {

    private static ClipboardData it;
    private ClipboardData() {
        it = this;
    }

    public static ClipboardData getInst(){
        if (it == null){
            it = new ClipboardData();
        }
        return it;
    }

    public void lostOwnership( Clipboard aClipboard, Transferable aContents)
    {
    }
    public void setClipboardContents( String aString )
    {
        StringSelection stringSelection = new StringSelection( aString );
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents( stringSelection, this );
    }
    public String getClipboardContents()
    {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getCuontents is not crrently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null) &&
                        contents.isDataFlavorSupported(DataFlavor.stringFlavor)
                ;

        if ( hasTransferableText) {
            try {
                result = (String)contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex){
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("s"+ClipboardData.getInst().getClipboardContents());
        Clipboard c=Toolkit.getDefaultToolkit().getSystemClipboard();
    }
}
