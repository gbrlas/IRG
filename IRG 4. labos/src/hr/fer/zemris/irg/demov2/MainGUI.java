package hr.fer.zemris.irg.demov2;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

public class MainGUI {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) throws IOException {

		final double[] polje = { 3, 3, 2, 1 };
		final RealVector ociste = new ArrayRealVector(polje);

		final double[] polje2 = { 0, 0, 0, 1 };
		final RealVector glediste = new ArrayRealVector(polje2);

		final double[] polje3 = { 3, 5, 7, 1 };
		final RealVector izvor = new ArrayRealVector(polje3);

		final View view = new View(ociste, glediste, izvor);
		final Figure object = Figure.parse("./tijela/kocka.obj", view);

		// transformacija i projekcija
		for (int i = 0; i < object.vrhovi.size(); i++) {
			RealVector point = view.calculateT(object.vrhovi.get(i));
			point = view.calculateP(point);
			object.vrhovi.set(i, point);
		}

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
					gl.glClearColor(1, 1, 1, 1.0f); // neka je pozadina
					// slike bijele boje
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
					gl2.glTranslated(300, 300, 0);
					gl2.glScaled(700, 700, 700);

					// konstantno(gl2);
					gouraudovo(gl2);

				}

				private void gouraudovo(GL2 gl2) {
					for (int j = 0; j < object.poligoni.size(); j++) {
						final RealVector p = object.poligoni.get(j);

						if (object.polygonsToDraw.get(j) == true) {
							gl2.glBegin(GL.GL_TRIANGLES);
							for (int i = 0; i < 3; i++) {
								gl2.glColor3ub(
										(byte) (double) object.intenzitetiVrhova
										.get((int) (p.getEntry(i) - 1)),
										(byte) 0, (byte) 0);
								final float x = (float) object.vrhovi.get(
										(int) p.getEntry(i) - 1)
										.getEntry(0);
								final float y = (float) object.vrhovi.get(
										(int) p.getEntry(i) - 1)
										.getEntry(1);
								gl2.glVertex2f(x, y);
							}
							gl2.glEnd();
						}
					}
				}

				private void konstantno(GL2 gl2) {
					for (int j = 0; j < object.poligoni.size(); j++) {

						final RealVector p = object.poligoni.get(j);

						if (object.polygonsToDraw.get(j) == true) {
							final double color = object.inteziteti.get(j);

								gl2.glBegin(GL.GL_TRIANGLES);
							gl2.glColor3ub((byte) color, (byte) 0, (byte) 0);
							for (int i = 0; i < 3; i++) {
								final float x = (float) object.vrhovi.get(
										(int) p.getEntry(i) - 1)
										.getEntry(0);
								final float y = (float) object.vrhovi.get(
										(int) p.getEntry(i) - 1)
										.getEntry(1);

								gl2.glVertex2f(x, y);
							}
							gl2.glEnd();
						}
					}
				}

			});

			final JFrame jframe = new JFrame("Sjencanje");
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
