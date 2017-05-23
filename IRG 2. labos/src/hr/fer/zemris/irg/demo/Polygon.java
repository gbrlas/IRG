package hr.fer.zemris.irg.demo;

import java.awt.BorderLayout;
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
public class Polygon {
	final static int frameWidth = 600;
	final static int frameHeight = 600;

	static int xmin = frameWidth;
	static int xmax = 0;
	static int ymin = frameHeight;
	static int ymax = 0;

	/**
	 * Main method which draws the polygon on the canvas.
	 *
	 * @param args
	 *            -none
	 * @throws IOException
	 *             if a problem occurs while reading user input from console
	 */
	public static void main(String[] args) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));

		System.out.println("Unesite broj vrhova poligona: ");
		final int n = Integer.parseInt(br.readLine());
		final List<Dot> list = new ArrayList<Dot>();

		Dot n1 = new Dot(0, 0, 0);

		// unos vrhova
		for (int i = 0; i < n; i++) {
			System.out.println("Unesite koordinate " + (i + 1) + ". vrha: ");
			final String temp = br.readLine();
			final String[] btemp = temp.split(" ");

			final Dot dot = new Dot(Integer.parseInt(btemp[0]),
					Integer.parseInt(btemp[1]), 0);

			if (i == 0) {
				n1 = dot.copy();
			}

			list.add(dot);
		}

		list.add(n1);

		// dohvati minimalnih x i y koordinata
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

		final List<Integer> a = new ArrayList<Integer>();
		final List<Integer> b = new ArrayList<Integer>();
		final List<Integer> c = new ArrayList<Integer>();

		// dobivanje jednadžbi pravaca (bridova)
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

		boolean test = true;

		// ako je točka iznad bilo kojeg brida, onda ne pripada konveksnom
		// poligonu
		// ovdje je zadan smjer kazaljke na satu, da je obrnuto onda bi se
		// gledalo za <0
		for (int i = 0; i < a.size(); i++) {
			if ((tDot.getX() * a.get(i)) + (tDot.getY() * b.get(i)) + c.get(i) > 0) {
				test = false;
				break;
			}
		}

		System.out.println(test ? "Točka T je unutar poligona!"
				: "Točka T je izvan poligona!");

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
									if (a.get(i) == 0) {
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
									} else {
										// računanje koordiante sjecišta
										final int x1 = (-b.get(i) * j - c
										.get(i)) / a.get(i);

										// lijevi brid
								if (list.get(i).getY() < list
										.get(i + 1).getY()) {
									// pamtimo najdesnije sjecište za
									// lijeve
											// bridove
											if (x1 > L) {
												L = x1;
											}
								}

										// desni brid
										if (list.get(i).getY() >= list.get(
										i + 1).getY()) {
											// za desne bridove pamtimo
									// najlijevije
											// sjecište
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
		});
	}

}
