import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * 一个查看图片的应用程序
 * @version 1.0 2017-02-23
 * @author Shawn
 */
public class ImageViewer{
  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        JFrame frame = new ImageViewerFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
      }
    });
  }
}
/**
 * 一个frame包含一个label来显示图片
 */
class ImageViewerFrame extends JFrame{
  public ImageViewerFrame(){
    setTitle("ImageViewer");
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    label = new JLabel();
    add(label);

    chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("."));

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu menu = new JMenu("File");
    menuBar.add(menu);

    JMenuItem openItem = new JMenuItem("Open");
    menu.add(openItem);
    openItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        int result = chooser.showOpenDialog(null);

		if(result == JFileChooser.APPROVE_OPTION){
		  String name = chooser.getSelectedFile().getPath();
		  label.setIcon(new ImageIcon(name));
		}
      }
    });

	JMenuItem exitItem = new JMenuItem("Exit");
	menu.add(exitItem);
	exitItem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
			System.exit(0);
		}
	});

  }

  private JLabel label;
  private JFileChooser chooser;
  private static final int DEFAULT_WIDTH = 300;
  private static final int DEFAULT_HEIGHT = 400;
}

