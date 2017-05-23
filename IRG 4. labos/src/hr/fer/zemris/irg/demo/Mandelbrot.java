package hr.fer.zemris.irg.demo;

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

public class Mandelbrot {

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

		for (int i = 0; i < 4; i++) {
			bounds[i] = Double.parseDouble(line[i]);
		}

		final double umin = bounds[0];
		final double umax = bounds[1];
		final double vmin = bounds[2];
		final double vmax = bounds[3];

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

					mandelbrot(gl2);
				}

				private void mandelbrot(GL2 gl2) {
					final double xmax = glcanvas.getWidth();
					final double ymax = glcanvas.getHeight();

					gl2.glBegin(GL.GL_POINTS);
					for (int x0 = 1; x0 < xmax; x0++) {
						for (int y0 = 1; y0 < ymax; y0++) {
							int k = -1;
							final double u0 = x0 / xmax * (umax - umin)
									+ umin;
							final double v0 = y0 / ymax * (vmax - vmin)
									+ vmin;
							Complex z = new Complex(0, 0);
							final double cRe = u0, cIm = v0;
							double r;
							do {
								k++;
								final Complex zn = new Complex(Math.pow(
										z.getReal(), 2)
										- Math.pow(z.getImaginary(), 2)
										+ cRe, 2 * z.getReal()
										* z.getImaginary() + cIm);
								r = Math.pow(Math.pow(zn.getReal(), 2)
										+ Math.pow(zn.getImaginary(), 2),
										0.5);
								z = zn;
							} while (r < epsilon && k < m);

							if (k == -1) {
								gl2.glColor3d(0, 0, 0);
							} else {

								int rc, gc, bc;

								k--;

								rc = ((int) (1.0 * k / (m - 1) * 255 + 0.5));

								gc = (255 - rc);

								bc = (int) ((k % (m / 2)) * 255 / (m / 2));

								gl2.glColor3ub((byte) rc, (byte) gc,
											(byte) bc);
							}
							gl2.glVertex2i(x0, y0);
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
