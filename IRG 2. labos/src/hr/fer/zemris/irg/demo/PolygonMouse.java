package hr.fer.zemris.irg.demo;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
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
 * Class which asks user input regarding the polygon level and its coordinates,
 * and then draws the polygon using OpenGL functions. There is also added
 * functionality in calculating whether the provided dot is inside or outside
 * the polygon.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class PolygonMouse {
	final static int frameWidth = 600;
	final static int frameHeight = 600;

	static int xmin = frameWidth;
	static int xmax = 0;
	static int ymin = frameHeight;
	static int ymax = 0;
	static boolean keyboard = false;

	static Dot n1 = new Dot(0, 0, 0);

	static int clicked;
	static int n;

	/**
	 * Main method which draws the polygon on the canvas.
	 *
	 * @param args
	 *            -none
	 * @throws IOException
	 *             if a problem occurs while reading user input from console
	 */
	public static void main(String[] args) throws IOException {

		SwingUtilities
		.invokeLater(() -> {
			final GLProfile glProfile = GLProfile.getDefault();
			final GLCapabilities glCapabilities = new GLCapabilities(
					glProfile);
			final GLCanvas glCanvas = new GLCanvas(glCapabilities);

			final List<Integer> a = new ArrayList<Integer>();
			final List<Integer> b = new ArrayList<Integer>();
			final List<Integer> c = new ArrayList<Integer>();
			final List<Dot> list = new ArrayList<Dot>();

			final JFrame jFrame = new JFrame("Polygon");
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

			glCanvas.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (keyboard) {
						final Dot dot = new Dot(e.getX(), frameHeight
								- e.getY(), 0);
						System.out.println("Točka T" + " - "
								+ dot.getX() + "," + dot.getY());
						calculateDot(a, b, c, dot);
					} else {
						if (clicked == 0) {
							n1 = new Dot(e.getX(), frameHeight
									- e.getY(), 0);
						}

						final Dot dot = new Dot(e.getX(), frameHeight
								- e.getY(), 0);
						clicked++;
						if (clicked < n) {
							System.out.println("Točka " + clicked
									+ " - " + dot.getX() + ","
									+ dot.getY());
							list.add(dot);
						} else if (clicked == n) {
							System.out.println("Točka " + clicked
									+ " - " + dot.getX() + ","
									+ dot.getY());
							list.add(dot);
							list.add(n1);
							setMinMax(list);
							addLineParameters2(list, a, b, c);
							glCanvas.display();
						} else {
							calculateDot(a, b, c, dot);
						}
					}
				}
			});

			try {
				final BufferedReader br = new BufferedReader(
						new InputStreamReader(System.in));

				System.out.println("Unesite broj vrhova poligona: ");
				n = Integer.parseInt(br.readLine());

				System.out
				.println("Odaberite kako želite unijeti točke (m/k): ");
				final String unos = br.readLine();

				switch (unos) {
				case "k":
					enterDots(n, br, list);
					setMinMax(list);
					addLineParameters(list, a, b, c);
					keyboard = true;
					break;

				case "m":
					break;
				default:
					break;
				}
				//

			} catch (final Exception e) {
				System.out.println(e.getLocalizedMessage());
				System.out.println(e.getStackTrace());
			}

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

					for (int i = 0; i < list.size() - 1; i++) {
						final Dot d1 = list.get(i);
						final Dot d2 = list.get(i + 1);
						drawLine(gl2, d1.getX(), d1.getY(), d2.getX(),
								d2.getY());
					}

					for (int j = ymin; j <= ymax; j++) {
						int L = xmin;
						int D = xmax;

						for (int i = 0; i < a.size(); i++) {
							// brid je vodoravan
							if (a.get(i) == 0) {
								// ako se brid i zraka podudaraju
								if (list.get(i).getY() == j) {
									if (list.get(i).getX() < list.get(
											i + 1).getX()) {
										L = list.get(i).getX();
										D = list.get(i + 1).getX();
									} else {
										L = list.get(i + 1).getX();
										D = list.get(i).getX();
									}
									break;
								}
								// brid nije vodoravan, postoji točno 1
								// sjecište
									} else {
								// računanje x-koordinate sjecišta zrake
								// i brida
								final int x1 = (-b.get(i) * j - c
												.get(i)) / a.get(i);

								// ako je lijevi brid
								if (list.get(i).getY() < list
												.get(i + 1).getY()) {
									// traži se najdesnije sjecište
									// zrake za lijevim bridom
									if (x1 > L) {
										L = x1;
									}
								}

								// ako je desni brid
								if (list.get(i).getY() >= list.get(
												i + 1).getY()) {
									// traži se najljevije sjecište
									// zrake sa desnim bridom
									if (x1 < D) {
										D = x1;
									}
								}
							}

								}

						if (L < D) {
							drawLine(gl2, L, j, D, j);
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
				public void drawLine(GL2 gl2, int x1, int y1, int x2,
						int y2) {
					gl2.glPointSize(1.0f);
					gl2.glColor3f(1.0f, 0.0f, 0.0f);
					gl2.glBegin(GL.GL_LINES);
					gl2.glVertex2i(x1, y1);
					gl2.glVertex2i(x2, y2);
					gl2.glEnd();

				}
			});

		});
	}

	/**
	 * Method which calculates the line parameters and adds them in their
	 * respective arrays.
	 *
	 * @param list
	 *            List containing all polygon dots.
	 * @param a
	 * @param b
	 * @param c
	 */
	private static void addLineParameters(List<Dot> list, List<Integer> a,
			List<Integer> b, List<Integer> c) {
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			// računanje koeficijenata pravaca
			for (int i = 0; i < list.size() - 1; i++) {
				final Dot d1 = list.get(i);
				final Dot d2 = list.get(i + 1);

				a.add(d1.getY() - d2.getY());
				b.add(-d1.getX() + d2.getX());
				c.add(d1.getX() * d2.getY() - d2.getX() * d1.getY());
			}

			System.out.println("Unesite koordinate točke T: ");
			final String temp = br.readLine();
			final String[] btemp = temp.split(" ");

			final Dot tDot = new Dot(Integer.parseInt(btemp[0]),
					Integer.parseInt(btemp[1]), 0);

			calculateDot(a, b, c, tDot);
		} catch (final Exception e) {
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getStackTrace());
		}
	}

	/**
	 * Method which calculates the line parameters and adds them in their
	 * respective arrays.
	 *
	 * @param list
	 *            List containing all polygon dots.
	 * @param a
	 * @param b
	 * @param c
	 */
	private static void addLineParameters2(List<Dot> list, List<Integer> a,
			List<Integer> b, List<Integer> c) {
		for (int i = 0; i < list.size() - 1; i++) {
			final Dot d1 = list.get(i);
			final Dot d2 = list.get(i + 1);

			a.add(d1.getY() - d2.getY());
			b.add(-d1.getX() + d2.getX());
			c.add(d1.getX() * d2.getY() - d2.getX() * d1.getY());
		}

	}

	/**
	 * Method which calculates whether the provided dot is inside of the
	 * polygon. The dot is inside if scalar products of the dot and all polygon
	 * sides is < 0 if the polygon dots are counter clockwise, or > 0 if the
	 * polygon dots are clockwise.
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param dot
	 */
	private static void calculateDot(List<Integer> a, List<Integer> b,
			List<Integer> c, Dot dot) {
		boolean test = true;

		for (int i = 0; i < a.size(); i++) {
			if ((dot.getX() * a.get(i)) + (dot.getY() * b.get(i)) + c.get(i) > 0) {
				test = false;
				break;
			}
		}

		System.out.println(test ? "Točka T(" + dot.getX() + "," + dot.getY()
				+ ") je unutar poligona!" : "Točka T(" + dot.getX() + ","
				+ dot.getY() + ") je izvan poligona!");
	}

	/**
	 * Method which calculates and sets the min/max x/y values in the polygon.
	 *
	 * @param list
	 *            List containing all polygon dots.
	 */
	private static void setMinMax(List<Dot> list) {
		for (final Dot dot : list) {
			if (dot.getX() < xmin) {
				xmin = dot.getX();
			}

			if (dot.getX() > xmax) {
				xmax = dot.getX();
			}

			if (dot.getY() < ymin) {
				ymin = dot.getY();
			}

			if (dot.getY() > ymax) {
				ymax = dot.getY();
			}
		}
	}

	/**
	 * Method used for processing entered dots.
	 *
	 * @param n
	 *            Number of dots to be added.
	 * @param br
	 *            buffered reader
	 * @param list
	 *            List used for storing the dots.
	 */
	private static void enterDots(int n, BufferedReader br, List<Dot> list) {
		for (int i = 0; i < n; i++) {
			System.out.println("Unesite koordinate " + (i + 1) + ". vrha: ");
			String temp;
			try {
				temp = br.readLine();
				final String[] btemp = temp.split(" ");

				final Dot dot = new Dot(Integer.parseInt(btemp[0]),
						Integer.parseInt(btemp[1]), 0);

				if (i == 0) {
					n1 = dot.copy();
				}

				list.add(dot);
			} catch (final IOException e) {
				System.out.println(e.getLocalizedMessage());
				System.out.println(e.getStackTrace());
			}

		}

		list.add(n1);
	}

}
