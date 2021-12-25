package com.royenheart.client.dialogs;

import javax.swing.*;
import java.io.File;

/**
 * 文件保存窗口
 * @author RoyenHeart
 * @author revrg
 * @author Tang-li-bian
 */
public class FileStoreDialog extends JFileChooser {

    public FileStoreDialog(String defaultFileName) {

        setSelectedFile(new File(defaultFileName));

    }

}
