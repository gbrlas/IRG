package hr.fer.zemris.irg.demo;

import hr.fer.zemris.irg.linearna.Matrix;
import hr.fer.zemris.irg.linearna.Vector;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ObjectShadingGouraud {
	final static int frameWidth = 600;
	final static int frameHeight = 600;
	static String ime = "Crtanje objekta";

	static List<Vector> vrhovi = new ArrayList<Vector>();
	final static List<DotFloat> poligoni = new ArrayList<DotFloat>();
	static Map<Vector, Double> intenziteti = new HashMap<Vector, Double>();
	static Map<DotFloat, Plane> ravnine = new HashMap<DotFloat, Plane>();
	static Map<Vector, Vector> normaleVrhovi = new HashMap<Vector, Vector>();
	static DotFloat ociste;
	static DotFloat glediste;
	static DotFloat izvor;

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
				} else if (line.startsWith("I")) {
					final String[] temp = line.split(" ");
					final DotFloat dot = new DotFloat(
							Double.parseDouble(temp[1]),
							Double.parseDouble(temp[2]),
							Double.parseDouble(temp[3]));
					izvor = dot;
				}

				line = br.readLine();
			}
		}

		vrhovi = scaleToWorkspace();

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
							gl2.glScaled(100, 100, 100);
					vrhovi = calculateTransformationMatrix(ociste,
							glediste, vrhovi);
							vrhovi = calculateProjectionMatrix(ociste,
							glediste, vrhovi);

					izracunajRavnine(poligoni, vrhovi);
							odrediVidljivostIIntenzitet(vrhovi, gl2);

						}

				private void izracunajRavnine(List<DotFloat> poligoni,
						List<Vector> vektori) {
					ravnine.clear();
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

								ravnine.put(poligon, new Plane(A, B, C, D));
							}

							normaleVrhovi.clear();

							for (int i = 0; i < vektori.size(); i++) {
								final ArrayList<Vector> n = new ArrayList<>();

						for (int j = 0; j < poligoni.size(); j++) {
									if ((poligoni.get(j).getX() - 1 == i)
									|| (poligoni.get(j).getY() - 1 == i)
											|| (poligoni.get(j).getZ() - 1 == i)) {
										final Plane ravnina = ravnine
										.get(poligoni.get(j));

								n.add(new Vector(ravnina.getA(),
										ravnina.getB(), ravnina.getC()));
									}
								}

						double nx = 0, ny = 0, nz = 0;

						for (final Vector normala : n) {
							final Vector normala2 = (Vector) normala
									.normalize();
									nx += normala2.get(0);
									ny += normala2.get(1);
									nz = normala2.get(2);
								}

								nx = nx / n.size();
								ny = ny / n.size();
								nz = nz / n.size();

								Vector normalaVrh = new Vector(nx, ny, nz);
								normalaVrh = (Vector) normalaVrh.normalize();

						normaleVrhovi.put(vrhovi.get(i), normalaVrh);
							}
						}

				private void odrediVidljivostIIntenzitet(
								List<Vector> vektori, GL2 gl2) {
							for (final DotFloat poligon : poligoni) {
								final Vector d1 = vektori.get((int) poligon
										.getX() - 1);
								final Vector d2 = vektori.get((int) poligon
										.getY() - 1);
								final Vector d3 = vektori.get((int) poligon
										.getZ() - 1);

						final Plane ravnina = ravnine.get(poligon);

								final double r = ravnina.getA() * ociste.getX()
										+ ravnina.getB() * ociste.getY()
										+ ravnina.getC() * ociste.getZ()
										+ ravnina.getD();

								if (r >= 0) {
							Vector normala = normaleVrhovi.get(d1);

							Vector L = new Vector(izvor.getX()
									- d1.get(0), izvor.getY()
									- d1.get(1), izvor.getZ()
									- d1.get(2));
									L = (Vector) L.normalize();

									double LN = normala.scalarProduct(L);
									final double ka = 0.3; // 0-1
									final double Ia = 256; // 0-255
									final double Ii = 256;
									final double kd = 0.78125; // 0-1

									double I = Ia * ka + Ii * kd * LN;
									intenziteti.put(d1, I);

							normala = normaleVrhovi.get(d2);

							L = new Vector(izvor.getX() - d2.get(0),
									izvor.getY() - d2.get(1), izvor
									.getZ() - d2.get(2));
							L = (Vector) L.normalize();

							LN = normala.scalarProduct(L);

							I = Ia * ka + Ii * kd * LN;
							intenziteti.put(d2, I);

							normala = normaleVrhovi.get(d3);

							L = new Vector(izvor.getX() - d3.get(0),
									izvor.getY() - d3.get(1), izvor
									.getZ() - d3.get(2));
							L = (Vector) L.normalize();

							LN = normala.scalarProduct(L);

							I = Ia * ka + Ii * kd * LN;
							intenziteti.put(d3, I);

							drawPolygon(poligon, vektori, gl2);
								}

							}
						}

						public void drawPolygon(DotFloat poligon,
						List<Vector> vektori, GL2 gl2) {
							final List<Vector> vektori2 = new ArrayList<>();
							vektori2.add(vektori.get((int) poligon.getX() - 1));
							vektori2.add(vektori.get((int) poligon.getY() - 1));
							vektori2.add(vektori.get((int) poligon.getZ() - 1));

							gl2.glBegin(GL.GL_TRIANGLES);
							for (final Vector vrh : vektori2) {
								gl2.glColor3ub(
										(byte) (double) intenziteti.get(vrh),
										(byte) 0, (byte) 0);
								gl2.glVertex2d(vrh.get(0), vrh.get(1));
							}
							gl2.glEnd();

				};
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

	private static List<Vector> scaleToWorkspace() {
		double xmin = vrhovi.get(0).get(0);
		double ymin = vrhovi.get(0).get(1);
		final double zmin = vrhovi.get(0).get(2);

		double xmax = vrhovi.get(1).get(0);
		double ymax = vrhovi.get(1).get(1);
		double zmax = vrhovi.get(1).get(2);

		for (final Vector vrh : vrhovi) {
			if (vrh.get(0) < xmin) {
				xmin = vrh.get(0);
			} else if (vrh.get(0) > xmax) {
				xmax = vrh.get(0);
			}

			if (vrh.get(1) < ymin) {
				ymin = vrh.get(1);
			} else if (vrh.get(1) > ymax) {
				ymax = vrh.get(1);
			}

			if (vrh.get(2) < zmin) {
				xmin = vrh.get(2);
			} else if (vrh.get(2) > zmax) {
				zmax = vrh.get(2);
			}
		}

		final double xsrediste = (xmax + xmin) / 2;
		final double ysrediste = (ymax + ymin) / 2;
		final double zsrediste = (zmax + zmin) / 2;
		final double xvelicina = xmax - xmin;
		final double yvelicina = ymax - ymin;
		final double zvelicina = zmax - zmin;
		final double scaling = 2 / Math.max(xvelicina,
				Math.max(yvelicina, zvelicina));

		final List<Vector> noviVrhovi = new ArrayList<Vector>();

		for (final Vector vrh : vrhovi) {
			final double x = (vrh.get(0) - xsrediste) * scaling;
			final double y = (vrh.get(1) - ysrediste) * scaling;
			final double z = (vrh.get(2) - zsrediste) * scaling;

			final Vector vector = new Vector(x, y, z, 1);
			noviVrhovi.add(vector);
		}

		return noviVrhovi;
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
