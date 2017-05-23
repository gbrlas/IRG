package hr.fer.zemris.irg.demo;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Class which reads from a provided .obj file and draws the object in 2D (z =
 * 0). There is also added functionality in calculating whether the provided dot
 * is inside or outside the object.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class BezierCurve {
	final static int frameWidth = 600;
	final static int frameHeight = 600;
	static String ime = "Crtanje objekta";

	static List<DotFloat> vrhovi = new ArrayList<DotFloat>();
	static List<Integer> factors = new ArrayList<Integer>();
	static int n;

	/**
	 * Main method which draws the polygon on the canvas.
	 *
	 * @param args
	 *            path to the .obj file
	 * @throws IOException
	 *             if a problem occurs while reading user input from console
	 */
	public static void main(String[] args) throws IOException {

		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
			String line = br.readLine();

			while (line != null) {
				if (line.startsWith("v")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							(Double.parseDouble(temp[1])),
							(Double.parseDouble(temp[2])),
							Double.parseDouble(temp[3]));
					n++;
					vrhovi.add(dot);
				}
				line = br.readLine();
			}

			n--;
			computeFactors(n);
		}

		SwingUtilities
		.invokeLater(() -> {
			final GLProfile glProfile = GLProfile.getDefault();
			final GLCapabilities glCapabilities = new GLCapabilities(
					glProfile);
			glCapabilities.setDepthBits(16);
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

					gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
					gl2.glClear(GL.GL_COLOR_BUFFER_BIT
							| GL.GL_DEPTH_BUFFER_BIT);

					gl2.glClearDepth(1.0f);
					gl2.glEnable(GL.GL_DEPTH_TEST);
					gl2.glDepthFunc(GL.GL_LEQUAL);

							drawBezier(gl2, 100);

							// crtaj pomocne linije
					for (int i = 1; i < n + 1; i++) {
						drawLine(gl2, vrhovi.get(i - 1).getX(), vrhovi
								.get(i - 1).getY(), vrhovi.get(i)
								.getX(), vrhovi.get(i).getY());
					}

				}

				public void drawBezier(GL2 gl2, int divs) {
					gl2.glPointSize(1.0f);
					gl2.glColor3f(0.0f, 0.0f, 255.0f);

					double t = 0;
					double b = 0;

					gl2.glBegin(GL.GL_LINE_STRIP);
					for (int i = 0; i <= divs; i++) {
						t = 1.0 / divs * i;
						double x = 0;
						double y = 0;
						double z = 0;

						for (int j = 0; j <= n; j++) {

							b = factors.get(j) * Math.pow(t, j)
											* Math.pow(1 - t, n - j);
							x += b * vrhovi.get(j).getX();
							y += b * vrhovi.get(j).getY();
							z += b * vrhovi.get(j).getZ();

						}
						gl2.glVertex3d(x, y, z);
					}

					gl2.glEnd();

				}

				/**
				 * Method used to draw the line.
				 *
				 * @param gl2
				 * @param x1
				 *            Starting point x value.
				 * @param y1
				 *            Starting point y value.
				 * @param x2
				 *            Ending point x value.
				 * @param y2
				 *            Ending point y value.
				 */
				public void drawLine(GL2 gl2, double x1, double y1,
						double x2, double y2) {
					gl2.glPointSize(1.0f);
					gl2.glColor3f(1.0f, 0.0f, 0.0f);
					gl2.glLineWidth(3);
					gl2.glBegin(GL.GL_LINES);
					gl2.glVertex2d(x1, y1);
					gl2.glVertex2d(x2, y2);
					gl2.glEnd();

				}
			});

			final JFrame jFrame = new JFrame(ime);
			jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowevent) {
					jFrame.dispose();
					System.exit(0);
				}
			});
			jFrame.getContentPane().add(glCanvas, BorderLayout.CENTER);
			jFrame.setSize(frameWidth, frameHeight);
			jFrame.setLocationRelativeTo(null);
			jFrame.setVisible(true);
			glCanvas.requestFocusInWindow();
		});
	}

	public static void computeFactors(int n) {
		int a = 1;

		for (int i = 1; i <= n + 1; i++) {
			factors.add(i - 1, a);
			a = a * (n - i + 1) / i;
		}
	}

}
