package com.royenheart.client.dialogs;

import javax.swing.*;
import java.io.File;

/**
 * 文件选择窗口
 * @author RoyenHeart
 * @author revrg
 * @author Tang-li-bian
 */
public class FileChooseDialog extends JFileChooser {

    public FileChooseDialog(String folder) {
        setCurrentDirectory(new File(folder));
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setMultiSelectionEnabled(false);
    }

}
