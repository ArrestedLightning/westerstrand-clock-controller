/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clock.controller;

/**
 *
 * @author joshualange
 */
public interface CopyPasteButtonListener {
    public void copyClicked(CopyPasteButtons source);
    public void pasteClicked(CopyPasteButtons source, boolean clear);
}
