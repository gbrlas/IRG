package hr.fer.zemris.irg.demov2;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.math3.complex.Complex;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

public class Julius {

	public static void main(String[] args) throws IOException {

		final BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));

		System.out.println("Unesite prag eps i maksimalan broj iteracija: ");

		String[] line = br.readLine().split("\\s+");

		final double epsilon = Double.parseDouble(line[0]);
		final double m = Double.parseDouble(line[1]);

		System.out
		.println("Unesite promatrano područje kompleksne ravnine (umin, umax, vmin, vmax): ");

		line = br.readLine().split("\\s+");

		final double[] bounds = new double[4];

		for (int i = 0; i < 4; i++)
			bounds[i] = Double.parseDouble(line[i]);

		final double umin = bounds[0];
		final double umax = bounds[1];
		final double vmin = bounds[2];
		final double vmax = bounds[3];

		System.out.println("Unesite parametre kompleksne točke C: ");
		line = br.readLine().split("\\s+");

		final double cRe = Double.parseDouble(line[0]);
		final double cIm = Double.parseDouble(line[1]);

		SwingUtilities
				.invokeLater(() -> {
					final GLProfile glprofile = GLProfile.getDefault();
					final GLCapabilities glcapabilities = new GLCapabilities(
							glprofile);
					final GLCanvas glcanvas = new GLCanvas(glcapabilities);

					// Reagiranje na pritisak tipki na misu . . .
				glcanvas.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {

					}
				});

				// Reagiranje na pomicanje misa
				glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {

					}
				});
				// Reagiranje na pritisak tipke na tipkovnici
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_R) {
							e.consume();

						}
					}
				});

				// Reagiranje na promjenu velicine platna
				glcanvas.addGLEventListener(new GLEventListener() {

					@Override
					public void reshape(GLAutoDrawable glautodrawable, int x,
							int y, int width, int height) {
						final GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();

						// coordinate system origin at lower left with width and
						// height same as the window
						final GLU glu = new GLU();
						glu.gluOrtho2D(0.0f, width, 0.0f, height);
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();
						gl2.glViewport(0, 0, width, height);

					}

					@Override
					public void init(GLAutoDrawable glautodrawable) {
						final GL2 gl = glautodrawable.getGL().getGL2();
						gl.glClearColor(1, 1, 1, 1.0f);
					}

					@Override
					public void dispose(GLAutoDrawable arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void display(GLAutoDrawable glautodrawable) {
						final GL2 gl2 = glautodrawable.getGL().getGL2();

						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();

						julius(gl2);

					}

					private void julius(GL2 gl2) {

						final double xmax = glcanvas.getWidth();
						final double ymax = glcanvas.getHeight();

						gl2.glBegin(GL.GL_POINTS);
						for (int x0 = 0; x0 < xmax; x0++) {
							for (int y0 = 0; y0 < ymax; y0++) {
								int k = -1;

							// odredi pripadnu točku kompleksne ravnine
								final double u0 = (umax - umin) * x0 / xmax
										+ umin;
								final double v0 = (vmax - vmin) * y0 / ymax
										+ vmin;

								Complex z = new Complex(u0, v0);

								double r;
								do {
									k++;
									final double nextRe = Math.pow(z.getReal(),
											2)
											- Math.pow(z.getImaginary(), 2)
											+ cRe;
									final double nextIm = 2 * z.getReal()
											* z.getImaginary() + cIm;
									final Complex zn = new Complex(nextRe,
											nextIm);

									r = Math.sqrt(Math.pow(zn.getReal(), 2)
											+ Math.pow(zn.getImaginary(), 2));
									z = zn;
								} while (r < epsilon && k < m);

							if (k == m) {
									gl2.glColor3f(0, 0, 0);
								} else {
									gl2.glColor3d(k / m, 1.0 - k / m / 2.0, 0.8
											- k / m / 3.0);
								}
								gl2.glVertex2f(x0, y0);
							}
						}
						gl2.glEnd();

					}

				});

				final JFrame jframe = new JFrame("Fraktali");
				jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jframe.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						jframe.dispose();
						System.exit(0);
					}
				});

				jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
				jframe.setSize(600, 600);

				jframe.setVisible(true);
				glcanvas.requestFocusInWindow();
			});

	}

}
