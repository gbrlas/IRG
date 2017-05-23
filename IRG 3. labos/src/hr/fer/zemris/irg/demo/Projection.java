package hr.fer.zemris.irg.demo;

import hr.fer.zemris.irg.linearna.Matrix;
import hr.fer.zemris.irg.linearna.Vector;

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
public class Projection {
	final static int frameWidth = 600;
	final static int frameHeight = 600;
	static String ime = "Crtanje objekta";

	static List<Vector> vrhovi = new ArrayList<Vector>();
	final static List<DotFloat> poligoni = new ArrayList<DotFloat>();
	static DotFloat ociste;
	static DotFloat glediste;

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
					final Vector dot = new Vector(true, false,
							(Double.parseDouble(temp[1])),
							(Double.parseDouble(temp[2])),
							Double.parseDouble(temp[3]), 1);

					vrhovi.add(dot);
				} else if (line.startsWith("f")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							Double.parseDouble(temp[1]),
							Double.parseDouble(temp[2]),
							Double.parseDouble(temp[3]));
					poligoni.add(dot);
				} else if (line.startsWith("O")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							Double.parseDouble(temp[1]),
							Double.parseDouble(temp[2]),
							Double.parseDouble(temp[3]));
					ociste = dot;
				} else if (line.startsWith("G")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							Double.parseDouble(temp[1]),
							Double.parseDouble(temp[2]),
							Double.parseDouble(temp[3]));
					glediste = dot;
				}

				line = br.readLine();
			}
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

							gl2.glTranslated(300, 300, 0);
							gl2.glScaled(20, 20, 20);
					vrhovi = calculateTransformationMatrix(ociste,
							glediste, vrhovi);
							vrhovi = calculateProjectionMatrix(ociste,
							glediste, vrhovi);

							for (final DotFloat poligon : poligoni) {
								final Vector d1 = vrhovi.get((int) poligon
										.getX() - 1);
								final Vector d2 = vrhovi.get((int) poligon
										.getY() - 1);
								final Vector d3 = vrhovi.get((int) poligon
										.getZ() - 1);

								drawLine(gl2, d1.get(0), d1.get(1), d1.get(2),
								d2.get(0), d2.get(1), d2.get(2));
								drawLine(gl2, d1.get(0), d1.get(1), d1.get(2),
								d3.get(0), d3.get(1), d3.get(2));
								drawLine(gl2, d2.get(0), d2.get(1), d2.get(2),
								d3.get(0), d3.get(1), d3.get(2));

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
						double z1, double x2, double y2, double z2) {
							gl2.glPointSize(1.0f);
							gl2.glColor3f(1.0f, 0.0f, 0.0f);
							gl2.glBegin(GL.GL_LINES);
							gl2.glVertex3d(x1, y1, z1);
							gl2.glVertex3d(x2, y2, z2);
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

	private static List<Vector> calculateTransformationMatrix(DotFloat ociste,
			DotFloat glediste, List<Vector> vrhovi) {

		// vraćamo novo ishodište(očište) u ishodište referentnog sustava
		final double[][] arrayt1 = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 },
				{ 0, 0, 1, 0 },
				{ -ociste.getX(), -ociste.getY(), -ociste.getZ(), 1 } };
		final Matrix t1 = new Matrix(4, 4, arrayt1, true);

		// rotacija u xy-ravnini zbog poklapanja z-osi
		final double xg1 = glediste.getX() - ociste.getX();
		final double yg1 = glediste.getY() - ociste.getY();
		final double zg1 = glediste.getZ() - ociste.getZ();

		final double sinA = yg1
				/ Math.sqrt(Math.pow(xg1, 2) + Math.pow(yg1, 2));
		final double cosA = xg1
				/ Math.sqrt(Math.pow(xg1, 2) + Math.pow(yg1, 2));

		final double[][] arrayt2 = { { cosA, -sinA, 0, 0 },
				{ sinA, cosA, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		final Matrix t2 = new Matrix(4, 4, arrayt2, true);

		// rotacija u xz ravnini radi poklapanja z-osi
		final double xg2 = Math.sqrt(Math.pow(xg1, 2) + Math.pow(yg1, 2));
		final double zg2 = zg1;

		final double sinB = xg2
				/ Math.sqrt(Math.pow(xg2, 2) + Math.pow(zg2, 2));
		final double cosB = zg2
				/ Math.sqrt(Math.pow(xg2, 2) + Math.pow(zg2, 2));

		final double[][] arrayt3 = { { cosB, 0, sinB, 0 }, { 0, 1, 0, 0 },
				{ -sinB, 0, cosB, 0 }, { 0, 0, 0, 1 } };
		final Matrix t3 = new Matrix(4, 4, arrayt3, true);

		// zakretanje za 90°, poklapanje y-osi
		final double[][] arrayt4 = { { 0, -1, 0, 0 }, { 1, 0, 0, 0 },
				{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		final Matrix t4 = new Matrix(4, 4, arrayt4, true);

		// okretanje x-osi
		final double[][] arrayt5 = { { -1, 0, 0, 0 }, { 0, 1, 0, 0 },
				{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		final Matrix t5 = new Matrix(4, 4, arrayt5, true);

		final Matrix temp1 = (Matrix) t1.nMultiply(t2);
		final Matrix temp2 = (Matrix) temp1.nMultiply(t3);
		final Matrix temp3 = (Matrix) temp2.nMultiply(t4);
		final Matrix T = (Matrix) temp3.nMultiply(t5);

		final List<Vector> vrhovi2 = new ArrayList<Vector>();
		for (final Vector vrh : vrhovi) {
			vrhovi2.add((Vector) vrh.toRowMatrix(true).nMultiply(T)
					.toVector(false));
		}

		return vrhovi2;
	}

	private static List<Vector> calculateProjectionMatrix(DotFloat ociste,
			DotFloat glediste, List<Vector> vrhovi) {

		final double H = Math.sqrt(Math.pow(ociste.getX() - glediste.getX(), 2)
				+ Math.pow(ociste.getY() - glediste.getY(), 2)
				+ Math.pow(ociste.getZ() - glediste.getZ(), 2));

		final List<Vector> vrhovi2 = new ArrayList<Vector>();
		for (final Vector vrh : vrhovi) {
			final double xp = vrh.get(0) * H / vrh.get(2);
			final double yp = vrh.get(1) * H / vrh.get(2);
			final double zp = 0;
			vrhovi2.add(new Vector(xp, yp, zp));
		}
		return vrhovi2;
	}
}
