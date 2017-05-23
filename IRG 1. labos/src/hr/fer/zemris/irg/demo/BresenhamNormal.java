package hr.fer.zemris.irg.demo;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

/**
 * Class which uses Bresenham algorithm to draw a line on the screen, comparing
 * the result with the built-in OpenGL method GL_LINES. Starting and ending
 * point are provided by the user through the console when prompted.
 *
 * Warning: algorithm works only if the line angle is in range of 0-45°.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class BresenhamNormal {
	static int x;
	static int y;
	static double D;

	/**
	 * Main method which draws the lines on the canvas.
	 *
	 * @param args
	 *            -none
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.print("Unesite koordinate točke A: \n");
		final String astr = br.readLine();
		final String[] atemp = astr.split(" ");
		System.out.print("Unesite koordinate točke B: \n");
		final String bstr = br.readLine();
		final String[] btemp = bstr.split(" ");

		final int x1 = Integer.parseInt(atemp[0]);
		final int y1 = Integer.parseInt(atemp[1]);

		final int x2 = Integer.parseInt(btemp[0]);
		final int y2 = Integer.parseInt(btemp[1]);

		x = x1;
		y = y1;

		SwingUtilities
		.invokeLater(() -> {
			final GLProfile glProfile = GLProfile.getDefault();
			final GLCapabilities glCapabilities = new GLCapabilities(
					glProfile);
			final GLCanvas glCanvas = new GLCanvas(glCapabilities);

			glCanvas.addGLEventListener(new GLEventListener() {

				@Override
				public void reshape(GLAutoDrawable drawable, int x,
						int y, int width, int height) {
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

					gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
					drawLine(gl2, x1, y1, x2, y2);
				}

				public void drawLine(GL2 gl2, int x1, int y1, int x2,
						int y2) {
					gl2.glPointSize(1.0f);

					// crtanje pomoćne linije
							gl2.glBegin(GL.GL_LINES);
					gl2.glVertex2i(x1, y1 + 20);
					gl2.glVertex2i(x2, y2 + 20);
					gl2.glEnd();

					final int x0 = x2 - x1;
					final int y0 = y2 - y1;

					D = y0 / (double) x0 - 0.5;

					gl2.glBegin(GL.GL_POINTS);
					for (int i = 0; i < x0; i++) {
						gl2.glVertex2i(x, y);

						if (D > 0) {
							y++;
							D = D - 1.0;
						}

						x++;
						D += y0 / x0;
					}

					gl2.glEnd();

						}
			});

			final JFrame jFrame = new JFrame("Crtanje točke");
			jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowevent) {
					jFrame.dispose();
					System.exit(0);
				}
			});
			jFrame.getContentPane().add(glCanvas, BorderLayout.CENTER);
			jFrame.setSize(300, 300);
			jFrame.setVisible(true);
			glCanvas.requestFocusInWindow();
		});
	}

}
