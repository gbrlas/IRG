package hr.fer.zemris.irg.demo;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

public class GlutTest {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) {
		SwingUtilities
				.invokeLater(() -> {
					final GLProfile glProfile = GLProfile.getDefault();
					final GLCapabilities glCapabilities = new GLCapabilities(
							glProfile);
					final GLCanvas glCanvas = new GLCanvas(glCapabilities);

					// reagiranje na pritisak tipke na misu
				glCanvas.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						System.out.println("Mis je kliknut na: x=" + e.getX()
								+ ", y=" + e.getY());
						// napravi nesto
						// posalji zahtjev za ponovnim crtanjem
						glCanvas.display();
					}
				});

				// reagiranje na pomicanje pokazivaca misa
				glCanvas.addMouseMotionListener(new MouseMotionAdapter() {

				@Override
					public void mouseMoved(java.awt.event.MouseEvent e) {
						System.out.println("Mis pomaknut na: x=" + e.getX()
							+ ", y=" + e.getY());
						// napravi nesto
						// posalji zahtjev za ponovnim crtanjem
						glCanvas.display();
					}

			});

				// reagiranje na pritiske tipaka na tipkovnici
				glCanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_R) {
							e.consume();
							// napravi nesto
							// posalji zahtjev za ponovnim crtanjem
							glCanvas.display();
						}
					}
				});

				// reagiranje na promjenu velicine platna, na zahtjev za
				// crtanjem i slicno
				glCanvas.addGLEventListener(new GLEventListener() {

				@Override
					public void reshape(GLAutoDrawable drawable, int x, int y,
						int width, int height) {
						final GL2 gl2 = drawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();

						final GLU glu = new GLU();
						glu.gluOrtho2D(0.0f, width, 0.0f, height);

					gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();

					gl2.glViewport(0, 0, width, height);
					}

				@Override
					public void init(GLAutoDrawable drawable) {
						// TODO Auto-generated method stub

				}

				@Override
					public void dispose(GLAutoDrawable drawable) {
						// TODO Auto-generated method stub

				}

				@Override
					public void display(GLAutoDrawable drawable) {
						final GL2 gl2 = drawable.getGL().getGL2();
						final int width = drawable.getSurfaceWidth();
						final int height = drawable.getSurfaceHeight();

					gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

					// draw a triangle filling the window
						gl2.glLoadIdentity();
						gl2.glBegin(GL.GL_TRIANGLES);
						gl2.glColor3f(1, 0, 0);
						gl2.glVertex2f(0, 0);
						gl2.glColor3f(0, 1, 0);
						gl2.glVertex2f(width, 0);
						gl2.glColor3f(0, 0, 1);
						gl2.glVertex2f(width / 2, height);
						gl2.glEnd();
				}
				});

				final JFrame jFrame = new JFrame(
						"Primjer prikaza obojenog trokuta");

			jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent windowevent) {
						jFrame.dispose();
						System.exit(0);
					}
				});

			jFrame.getContentPane().add(glCanvas, BorderLayout.CENTER);
				jFrame.setSize(640, 480);
				jFrame.setVisible(true);
				glCanvas.requestFocusInWindow();
		});

	}
}
