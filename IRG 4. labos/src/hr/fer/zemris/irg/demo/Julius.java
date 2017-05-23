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

public class Julius {

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter epsilon and number of iterations: ");

		String[] line = br.readLine().split("\\s+");

		double epsilon = Double.parseDouble(line[0]);
		double m = Double.parseDouble(line[1]);

		System.out.println("Enter complex area: ");

		line = br.readLine().split("\\s+");

		double[] bounds = new double[4];

		for (int i = 0; i < 4; i++)
			bounds[i] = Double.parseDouble(line[i]);

		double umin = bounds[0];
		double umax = bounds[1];
		double vmin = bounds[2];
		double vmax = bounds[3];

		System.out.println("Enter C complex constant parameters: ");
		line = br.readLine().split("\\s+");

		double cRe = Double.parseDouble(line[0]);
		double cIm = Double.parseDouble(line[1]);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
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
					public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();

						// coordinate system origin at lower left with width and
						// height same as the window
						GLU glu = new GLU();
						glu.gluOrtho2D(0.0f, width, 0.0f, height);
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();
						gl2.glViewport(0, 0, width, height);

					}

					@Override
					public void init(GLAutoDrawable glautodrawable) {
						GL2 gl = glautodrawable.getGL().getGL2();
						gl.glClearColor(1, 1, 1, 1.0f);
					}

					@Override
					public void dispose(GLAutoDrawable arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();

						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();

						julius(gl2);

					}

					private void julius(GL2 gl2) {

						double xmax = glcanvas.getWidth();
						double ymax = glcanvas.getHeight();

						gl2.glBegin(GL.GL_POINTS);
						for (int x0 = 1; x0 < xmax; x0++) {
							for (int y0 = 1; y0 < ymax; y0++) {
								int k = -1;
								double u0 = x0 / xmax * (umax - umin) + umin;
								double v0 = y0 / ymax * (vmax - vmin) + vmin;
								Complex z = new Complex(u0, v0);
								double r;
								while (k < m) {
									k++;
									Complex zn = new Complex(
											Math.pow(z.getReal(), 2) - Math.pow(z.getImaginary(), 2) + cRe,
											2 * z.getReal() * z.getImaginary() + cIm);
									r = Math.pow(Math.pow(zn.getReal(), 2) + Math.pow(zn.getImaginary(), 2), 0.5);
									z = zn;
									if (r >= epsilon) {
										break;
									}
								}
								if (k >= m) {
									gl2.glColor3f(0, 0, 0);
								} else {
									gl2.glColor3f((float) (k / m), (float) (1 - (double) k / m / 2.0),
											(float) (0.8 - (double) k / m / 3.0));
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
			}
		});

	}

}
