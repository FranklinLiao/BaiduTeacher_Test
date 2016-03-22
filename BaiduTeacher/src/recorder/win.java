package recorder;

import java.io.File;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


import service.JMFSample;
import service.JavaSoundRecorder;
import service.Sample;
import service.TestMusic;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JRootPane;

public class win {
	private static Text txtContent;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		final Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		final JavaSoundRecorder jRecorder= new JavaSoundRecorder(); 
		
		
		final Button btnRecord = new Button(shell, SWT.NONE);
		btnRecord.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(btnRecord.getText().equals("Start Record"))
				{			
					new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(10);
							} catch (InterruptedException ex) {
								ex.printStackTrace();
							}
							File wavFile = new File("D:/RecordAudio.wav");
							if(wavFile.exists())
								wavFile.delete();
							jRecorder.start();							
								
						}
					}).start();
					
					btnRecord.setText("Stop Record");
					
					return;
				}
				if(btnRecord.getText().equals("Stop Record"))
				{				
					new Thread(new Runnable() {
						public void run() {
							jRecorder.finish();
							//
							try {
								Sample.getToken();
								final String res = Sample.method1();
								
								if (!display.isDisposed()) {

								      Runnable runnable = new Runnable() {

								            public void run() {

								                  // 你改界面的代码
								            	txtContent.setText(res);
								            }

								            };

								      display.syncExec(runnable); // 关键在这一句上

								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}							
						}
					}).start();
					btnRecord.setText("Start Record");
					
					return;
				}
			}
		});
		btnRecord.setText("Start Record");
		btnRecord.setBounds(43, 59, 80, 27);
		
	
		
		Button btnPlay = new Button(shell, SWT.NONE);		
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(1);
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
						TestMusic sound = new TestMusic("D:/RecordAudio.wav");
						// play the sound
						sound.play();
					}
				}).start();
			}
		});
		btnPlay.setText("Play Sound");
		btnPlay.setBounds(151, 59, 80, 27);
		
		txtContent = new Text(shell, SWT.BORDER | SWT.MULTI);
		txtContent.setEditable(false);
		txtContent.setBounds(261, 10, 163, 158);
		
		Button btnPlayVideo = new Button(shell, SWT.NONE);
		btnPlayVideo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				 JMFSample sp = new JMFSample();  
			     sp.play();  
			}
		});
		btnPlayVideo.setBounds(43, 102, 80, 27);
		btnPlayVideo.setText("Play Video");
		
		Composite composite = new Composite(shell, SWT.EMBEDDED);
		composite.setBounds(47, 149, 64, 64);
		
		Frame frame = SWT_AWT.new_Frame(composite);
		
		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
