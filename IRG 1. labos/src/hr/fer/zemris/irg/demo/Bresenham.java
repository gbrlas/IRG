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
 * Algorithm works for all angles.
 *
 * @author Goran Brlas
 * @version 1.0
 *
 */
public class Bresenham {

	/**
	 * Main method which draws the lines on the canvas.
	 *
	 * @param args
	 *            -none
	 * @throws IOException
	 *             if a problem occurs while reading user input from console
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

		final int frameWidth = 600;
		final int frameHeight = 600;

		final int x1 = Integer.parseInt(atemp[0]) + frameWidth / 2;
		final int y1 = Integer.parseInt(atemp[1]) + frameHeight / 2;

		final int x2 = Integer.parseInt(btemp[0]) + frameWidth / 2;
		final int y2 = Integer.parseInt(btemp[1]) + frameHeight / 2;

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

					gl2.glColor3d(1, 0, 0);
					// crtanje pomoćne linije
					gl2.glBegin(GL.GL_LINES);
					gl2.glVertex2f(x1, y1 + 20);
					gl2.glVertex2f(x2, y2 + 20);
					gl2.glEnd();

					gl2.glColor3d(1, 1, 1);

					// koordinatni sustav
					gl2.glBegin(GL.GL_LINES);
					gl2.glVertex2f(frameWidth / 2, 0);
					gl2.glVertex2f(frameWidth / 2, frameHeight);
					gl2.glEnd();

					gl2.glBegin(GL.GL_LINES);
					gl2.glVertex2f(frameWidth, frameHeight / 2);
					gl2.glVertex2f(0, frameHeight / 2);
					gl2.glEnd();

					if (x1 <= x2) {
						// 1. i 4. kvadrant
						if (y1 <= y2) {
							// 1. kvadrant
							drawLineTwo(gl2, x1, y1, x2, y2);
						} else {
							// 4. kvadrant
							drawLineThree(gl2, x1, y1, x2, y2);
						}
					} else {
						// 2. i 3. kvadrant, potrebno je zamijeniti
								// početne i završne koordinate
						if (y1 >= y2) {
							// 2. kvadrant
							drawLineTwo(gl2, x2, y2, x1, y1);
						} else {
							// 3. kvadrant
							drawLineThree(gl2, x2, y2, x1, y1);
						}
					}

				}

				/**
				 * Method which draws the line if the line angle is
				 * between 0-90°.
				 *
				 * @param gl2
				 * @param xs
				 *            Starting point x value.
				 * @param ys
				 *            Starting point y value.
				 * @param xe
				 *            Ending point x value.
				 * @param ye
				 *            Ending point y value.
				 */
				private void drawLineTwo(GL2 gl2, int xs, int ys,
						int xe, int ye) {
					int x;
					int yc;
					final int korekcija;
					int a, yf;

							// provjera je li pomak po y manji od pomaka po x
							// (je li nagib manji od 45°)
					if (ye - ys <= xe - xs) {
						a = 2 * (ye - ys);
						yc = ys;
						yf = -(xe - xs);
						korekcija = -2 * (xe - xs);

						gl2.glBegin(GL.GL_POINTS);
						for (x = xs; x <= xe; x++) {
							gl2.glVertex2i(x, yc);
							yf += a;

							if (yf >= 0) {
								yf += korekcija;
								yc++;
							}
						}
					} else {
						// zamijeni x i y koordinate
						x = xe;
						xe = ye;
						ye = x;
						x = xs;
						xs = ys;
						ys = x;
						a = 2 * (ye - ys);
						yc = ys;
						yf = -(xe - xs);
						korekcija = -2 * (xe - xs);

						gl2.glBegin(GL.GL_POINTS);
						for (x = xs; x <= xe; x++) {
							gl2.glVertex2i(yc, x);
							yf += a;

							if (yf >= 0) {
								yf += korekcija;
								yc++;
							}
						}
					}

					gl2.glEnd();
				}

				/**
				 * Method which draws the line if the line angle is
				 * between 0-(-90)°.
				 *
				 * @param gl2
				 * @param xs
				 *            Starting point x value.
				 * @param ys
				 *            Starting point y value.
				 * @param xe
				 *            Ending point x value.
				 * @param ye
				 *            Ending point y value.
				 */
				private void drawLineThree(GL2 gl2, int xs, int ys,
						int xe, int ye) {
					int x;
					int yc;
					final int korekcija;
					int a, yf;

					if (-(ye - ys) <= xe - xs) {
						a = 2 * (ye - ys);
						yc = ys;
						yf = (xe - xs);
						korekcija = 2 * (xe - xs);

						gl2.glBegin(GL.GL_POINTS);
						for (x = xs; x <= xe; x++) {
							gl2.glVertex2i(x, yc);
							yf += a;

							if (yf <= 0) {
								yf += korekcija;
								yc--;
							}
						}
					} else {
						// opet zamijena vrijednosti jer je kut veći od
								// -45°
						x = xe;
						xe = ys;
						ys = x;
						x = xs;
						xs = ye;
						ye = x;
						a = 2 * (ye - ys);
						yc = ys;
						yf = (xe - xs);
						korekcija = 2 * (xe - xs);

						gl2.glBegin(GL.GL_POINTS);
						for (x = xs; x <= xe; x++) {
							gl2.glVertex2i(yc, x);
							yf += a;

							if (yf <= 0) {
								yf += korekcija;
								yc--;
							}
						}
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
			jFrame.setSize(frameWidth, frameHeight);
			jFrame.setLocationRelativeTo(null);
			jFrame.setVisible(true);

			glCanvas.requestFocusInWindow();
		});
	}
}
