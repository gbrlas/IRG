package hr.fer.zemris.irg.demo;

import hr.fer.zemris.irg.linearna.Matrix;
import hr.fer.zemris.irg.linearna.Vector;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
 * 0).
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class BezierProjection {
	final static int frameWidth = 600;
	final static int frameHeight = 600;
	static String ime = "Crtanje objekta";

	static List<Vector> vrhovi = new ArrayList<Vector>();
	static List<Vector> vrhoviStarting = new ArrayList<Vector>();
	static List<Vector> vrhoviBlue = new ArrayList<Vector>();
	static List<DotFloat> vrhoviBezier = new ArrayList<DotFloat>();
	final static List<DotFloat> poligoni = new ArrayList<DotFloat>();
	static DotFloat ociste;
	static DotFloat ocisteStarting;
	static DotFloat glediste;
	static DotFloat gledisteStarting;
	static double t = 0;
	static int n;
	static List<Integer> factors = new ArrayList<Integer>();
	static int k = 0;

	/**
	 * Main method which draws the object.
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
					vrhoviStarting.add(dot);
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
					ocisteStarting = new DotFloat(Double.parseDouble(temp[1]),
							Double.parseDouble(temp[2]),
							Double.parseDouble(temp[3]));
				} else if (line.startsWith("G")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							Double.parseDouble(temp[1]),
							Double.parseDouble(temp[2]),
							Double.parseDouble(temp[3]));
					glediste = dot;
					gledisteStarting = new DotFloat(
							Double.parseDouble(temp[1]),
							Double.parseDouble(temp[2]),
							Double.parseDouble(temp[3]));
				}

				line = br.readLine();
			}
		}

		try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
			String line = br.readLine();

			while (line != null) {
				if (line.startsWith("v")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							(Double.parseDouble(temp[1])),
							(Double.parseDouble(temp[2])),
							Double.parseDouble(temp[3]));
					n++;
					vrhoviBezier.add(dot);
				}
				line = br.readLine();
			}

			n--;
			computeFactors(n);
		}

		System.out
				.println("Pritisnite 'n' i 'm' za kretanje očišta, 'j' i 'k' za kretanje gledišta ili 'r' za resetiranje.");

		SwingUtilities
		.invokeLater(() -> {
			final GLProfile glProfile = GLProfile.getDefault();
			final GLCapabilities glCapabilities = new GLCapabilities(
					glProfile);
			glCapabilities.setDepthBits(16);
			final GLCanvas glCanvas = new GLCanvas(glCapabilities);

					glCanvas.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_N) {
						vrhovi.clear();
						vrhovi.addAll(vrhoviStarting);
						if (t < 1) {
							t += 0.01;
						}
						ociste = odrediTocku();
						System.out.println("ociste: " + ociste.getX()
								+ " " + ociste.getY() + " "
								+ ociste.getZ());
						glCanvas.display();

					} else if (e.getKeyCode() == KeyEvent.VK_M) {
						vrhovi.clear();
						vrhovi.addAll(vrhoviStarting);
						if (t > 0) {
							t -= 0.01;
						}
						ociste = odrediTocku();
						glCanvas.display();
					} else if (e.getKeyCode() == KeyEvent.VK_J) {
						vrhovi.clear();
						vrhovi.addAll(vrhoviStarting);
						if (t < 1) {
							t += 0.01;
						}
						glediste = odrediTocku();
						glCanvas.display();

					} else if (e.getKeyCode() == KeyEvent.VK_K) {
						vrhovi.clear();
						vrhovi.addAll(vrhoviStarting);
						if (t > 0) {
							t -= 0.01;
						}
						glediste = odrediTocku();
						glCanvas.display();
					} else if (e.getKeyCode() == KeyEvent.VK_R) {
						vrhovi.clear();
						vrhovi.addAll(vrhoviStarting);
						t = 0;
						glediste = gledisteStarting.copy();
						ociste = ocisteStarting.copy();
						glCanvas.display();
					} else if (e.getKeyCode() == KeyEvent.VK_I) {
						vrhovi.clear();
						if (t < 1) {
							t += 0.01;
						}
						final DotFloat tocka = odrediTocku();
						for (final Vector vrh : vrhoviStarting) {
							final Vector vector = new Vector(vrh.get(0)
									+ tocka.getX(), vrh.get(1)
									+ tocka.getY(), vrh.get(2)
									+ tocka.getZ(), 1);
							vrhovi.add(vector);
						}
						glCanvas.display();
					} else if (e.getKeyCode() == KeyEvent.VK_O) {
						vrhovi.clear();
						if (t > 0) {
							t -= 0.01;
						}
						final DotFloat tocka = odrediTocku();
						for (final Vector vrh : vrhoviStarting) {
							final Vector vector = new Vector(vrh.get(0)
									+ tocka.getX(), vrh.get(1)
									+ tocka.getY(), vrh.get(2)
									+ tocka.getZ(), 1);
							vrhovi.add(vector);
						}
						glCanvas.display();
					}
				}
			});

			glCanvas.addGLEventListener(new GLEventListener() {

				@Override
				public void reshape(GLAutoDrawable drawable, int x,
						int y, int width, int height) {
					final GL2 gl2 = drawable.getGL().getGL2();
					gl2.glMatrixMode(GL2.GL_PROJECTION);
					gl2.glLoadIdentity();

							vrhovi.clear();
					vrhovi.addAll(vrhoviStarting);

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

					if (k == 0) {
						gl2.glTranslated(300, 300, 0);
						gl2.glScaled(100, 100, 100);
						k++;
					}

					vrhovi = calculateTransformationMatrix(ociste,
									glediste, vrhovi);
					vrhovi = calculateProjectionMatrix(ociste,
									glediste, vrhovi);

					System.out.println("t = " + t);
					if (k == 1) {
						vrhoviBlue.addAll(vrhovi);
					}

							// za sve poligone izračunaj jesu li vidljivi i
							// prikaži ih ako jesu
							odrediVidljivost2(vrhovi, gl2);
							odrediVidljivost(vrhoviBlue, gl2);

				}

				/**
				 * Method which calculates the polygon visibility using
				 * the eye location and the polygon plane.
				 *
				 * @param vektori
				 * @param gl2
				 */
				private void odrediVidljivost(List<Vector> vektori,
						GL2 gl2) {
					for (final DotFloat poligon : poligoni) {
						final Vector d1 = vektori.get((int) poligon
										.getX() - 1);
						final Vector d2 = vektori.get((int) poligon
										.getY() - 1);
						final Vector d3 = vektori.get((int) poligon
										.getZ() - 1);

						final double A = (d2.get(1) - d1.get(1))
								* (d3.get(2) - d1.get(2))
								- (d2.get(2) - d1.get(2))
								* (d3.get(1) - d1.get(1));
						final double B = -(d2.get(0) - d1.get(0))
								* (d3.get(2) - d1.get(2))
								+ (d2.get(2) - d1.get(2))
								* (d3.get(0) - d1.get(0));
						final double C = (d2.get(0) - d1.get(0))
								* (d3.get(1) - d1.get(1))
								- (d2.get(1) - d1.get(1))
								* (d3.get(0) - d1.get(0));
						final double D = -d1.get(0) * A - d1.get(1) * B
								- d1.get(2) * C;

						final Plane ravnina = new Plane(A, B, C, D);

						final double r = ravnina.getA() * ociste.getX()
								+ ravnina.getB() * ociste.getY()
								+ ravnina.getC() * ociste.getZ()
								+ ravnina.getD();

						if (r >= 0 && vektori == vrhoviBlue) {
							drawLine2(gl2, d1.get(0), d1.get(1),
											d1.get(2), d2.get(0), d2.get(1),
											d2.get(2));
							drawLine2(gl2, d1.get(0), d1.get(1),
											d1.get(2), d3.get(0), d3.get(1),
											d3.get(2));
							drawLine2(gl2, d2.get(0), d2.get(1),
											d2.get(2), d3.get(0), d3.get(1),
											d3.get(2));
						} else if (r >= 0) {
							drawLine(gl2, d1.get(0), d1.get(1),
									d1.get(2), d2.get(0), d2.get(1),
									d2.get(2));
							drawLine(gl2, d1.get(0), d1.get(1),
											d1.get(2), d3.get(0), d3.get(1),
											d3.get(2));
							drawLine(gl2, d2.get(0), d2.get(1),
											d2.get(2), d3.get(0), d3.get(1),
											d3.get(2));
						}

					}
				}

				/**
				 * Method which calculates the polygon visibility using
				 * the angle between the eye plane normal vector and the
				 * polygon-eye vector.
				 *
				 * @param vektori
				 * @param gl2
				 */
				private void odrediVidljivost2(List<Vector> vektori,
						GL2 gl2) {
					for (final DotFloat poligon : poligoni) {
						final Vector d1 = vektori.get((int) poligon
								.getX() - 1);
						final Vector d2 = vektori.get((int) poligon
								.getY() - 1);
						final Vector d3 = vektori.get((int) poligon
								.getZ() - 1);

						final Vector v1 = new Vector(d2.get(0)
								- d1.get(0), d2.get(1) - d1.get(1), d2
								.get(2) - d1.get(2));
						final Vector v2 = new Vector(d3.get(0)
								- d1.get(0), d3.get(1) - d1.get(1), d3
								.get(2) - d1.get(2));

						final Vector normala = (Vector) v1
								.nVectorProduct(v2).normalize();
						final Vector eye = (new Vector(ociste.getX()
								- d1.get(0), ociste.getY() - d1.get(1),
								ociste.getZ() - d1.get(2)));

						final double r = normala.scalarProduct(eye);
						if (r >= 0 && vektori == vrhoviBlue) {
							drawLine2(gl2, d1.get(0), d1.get(1),
									d1.get(2), d2.get(0), d2.get(1),
									d2.get(2));
							drawLine2(gl2, d1.get(0), d1.get(1),
									d1.get(2), d3.get(0), d3.get(1),
									d3.get(2));
							drawLine2(gl2, d2.get(0), d2.get(1),
									d2.get(2), d3.get(0), d3.get(1),
									d3.get(2));
						} else if (r >= 0) {
							drawLine(gl2, d1.get(0), d1.get(1),
									d1.get(2), d2.get(0), d2.get(1),
									d2.get(2));
							drawLine(gl2, d1.get(0), d1.get(1),
									d1.get(2), d3.get(0), d3.get(1),
									d3.get(2));
							drawLine(gl2, d2.get(0), d2.get(1),
									d2.get(2), d3.get(0), d3.get(1),
									d3.get(2));
						}

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
				private void drawLine(GL2 gl2, double x1, double y1,
								double z1, double x2, double y2, double z2) {
					gl2.glPointSize(1.0f);
					gl2.glLineWidth(2);
					gl2.glColor3f(1.0f, 0.0f, 0.0f);
					gl2.glBegin(GL.GL_LINES);
					gl2.glVertex3d(x1, y1, z1);
					gl2.glVertex3d(x2, y2, z2);
					gl2.glEnd();

				}

				/**
				 * Method used to draw the blue line.
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
				private void drawLine2(GL2 gl2, double x1, double y1,
						double z1, double x2, double y2, double z2) {
					gl2.glPointSize(1.0f);
					gl2.glColor3f(0.0f, 0.0f, 255.0f);
					gl2.glLineWidth(1);
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

	/**
	 * Method which calculates the transformation matrix.
	 *
	 * @param ociste
	 * @param glediste
	 * @param vrhovi
	 * @return List containing all the dots after applying the transformation
	 *         matrix to them.
	 */
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

	/**
	 * Method which calculates the projection matrix.
	 *
	 * @param ociste
	 * @param glediste
	 * @param vrhovi
	 * @return List containing all the dots after applying the projection matrix
	 *         to them.
	 */
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

	/**
	 * Method which calculates the factors used in the Bernstein expression.
	 *
	 * @param n
	 *            Factors used in the Bernstein expression.
	 */
	public static void computeFactors(int n) {
		int a = 1;

		for (int i = 1; i <= n + 1; i++) {
			factors.add(i - 1, a);
			a = a * (n - i + 1) / i;
		}
	}

	/**
	 * Method which calculates the Bezier coordinates of a dot.
	 *
	 * @return Bezier dot coordinates.
	 */
	public static DotFloat odrediTocku() {
		double x = 0;
		double y = 0;
		double z = 0;
		double b;
		for (int j = 0; j <= n; j++) {

			b = factors.get(j) * Math.pow(t, j) * Math.pow(1 - t, n - j);

			x += b * vrhoviBezier.get(j).getX();
			y += b * vrhoviBezier.get(j).getY();
			z += b * vrhoviBezier.get(j).getZ();

		}

		return new DotFloat(x, y, z);
	}

}
