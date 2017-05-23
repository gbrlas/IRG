package hr.fer.zemris.irg.demo;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class Object {
	final static int frameWidth = 600;
	final static int frameHeight = 600;
	static String ime = "Crtanje objekta";

	final static List<DotFloat> vrhovi = new ArrayList<DotFloat>();
	final static List<DotFloat> poligoni = new ArrayList<DotFloat>();
	final static List<Plane> ravnine = new ArrayList<Plane>();

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
				if (line.startsWith("g")) {
					ime = line.split(" ")[1];
				} else if (line.startsWith("v")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							(Double.parseDouble(temp[1])),
							(Double.parseDouble(temp[2])),
							Double.parseDouble(temp[3]));

					vrhovi.add(dot);
				} else if (line.startsWith("f")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							Double.parseDouble(temp[1]),
							Double.parseDouble(temp[2]),
							Double.parseDouble(temp[3]));
					poligoni.add(dot);
				}

				line = br.readLine();
			}
		}

		DotFloat tDot = new DotFloat(0, 0, 0);

		try (final BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in))) {
			System.out.println("Unesite koordinate točke T: ");

			final String temp = br.readLine();
			final String[] btemp = temp.trim().split(" ");

			tDot = new DotFloat(Double.parseDouble(btemp[0]),
					Double.parseDouble(btemp[1]), Double.parseDouble(btemp[2]));
		}

		// za svaki poligon izraćunaj njegovu jednadžbu ravnine
		calculatePlaneParameters();

		calculateDot(tDot);

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

							gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
							gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
							gl2.glTranslated(400, 300, 0);
							gl2.glScaled(800, 800, 1);

							for (final DotFloat poligon : poligoni) {
								final DotFloat d1 = vrhovi.get((int) poligon
										.getX() - 1);
								final DotFloat d2 = vrhovi.get((int) poligon
										.getY() - 1);
								final DotFloat d3 = vrhovi.get((int) poligon
										.getZ() - 1);

								drawLine(gl2, d1.getX(), d1.getY(), d2.getX(),
										d2.getY());
								drawLine(gl2, d1.getX(), d1.getY(), d3.getX(),
										d3.getY());
								drawLine(gl2, d2.getX(), d2.getY(), d3.getX(),
										d3.getY());

							}

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

	/**
	 * Method which calculates the plane parameters for every polygon and stores
	 * them in the planes list.
	 */
	private static void calculatePlaneParameters() {
		for (final DotFloat poligon : poligoni) {
			final DotFloat d1 = vrhovi.get((int) poligon.getX() - 1);
			final DotFloat d2 = vrhovi.get((int) poligon.getY() - 1);
			final DotFloat d3 = vrhovi.get((int) poligon.getZ() - 1);

			final double A = (d2.getY() - d1.getY()) * (d3.getZ() - d1.getZ())
					- (d2.getZ() - d1.getZ()) * (d3.getY() - d1.getY());
			final double B = -(d2.getX() - d1.getX()) * (d3.getZ() - d1.getZ())
					+ (d2.getZ() - d1.getZ()) * (d3.getX() - d1.getX());
			final double C = (d2.getX() - d1.getX()) * (d3.getY() - d1.getY())
					- (d2.getY() - d1.getY()) * (d3.getX() - d1.getX());
			final double D = -d1.getX() * A - d1.getY() * B - d1.getZ() * C;

			final Plane plane = new Plane(A, B, C, D);
			ravnine.add(plane);
		}
	}

	/**
	 * Method which calculates whether the provided dot is inside the object.
	 *
	 * @param tDot
	 *            dot
	 */
	private static void calculateDot(DotFloat tDot) {
		boolean test = true;

		for (final Plane ravnina : ravnine) {
			// > 0 - točka je iznad ravnine
			// = 0 - točka je na ravnini
			// < 0 - točka je ispod ravnine
			final double r = ravnina.getA() * tDot.getX() + ravnina.getB()
					* tDot.getY() + ravnina.getC() * tDot.getZ()
					+ ravnina.getD();

			if (r > 0) {
				test = false;
				break;
			}
		}

		System.out.println(test ? "Točka T je unutar tijela!"
				: "Točka T je izvan tijela!");
	}
}
