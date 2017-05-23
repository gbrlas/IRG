package hr.fer.zemris.irg.demov2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Figure {

	public List<RealVector> vrhovi;
	public List<RealVector> poligoni;
	public List<Boolean> polygonsToDraw;
	public List<Double> inteziteti;
	public List<RealVector> normale;
	public List<RealVector> normaleUvrhovima;
	public List<Double> intenzitetiVrhova;

	public Figure(List<RealVector> vertices, List<RealVector> polygons,
			View view) {
		this.vrhovi = vertices;
		this.poligoni = polygons;
		this.polygonsToDraw = new ArrayList<>();
		this.inteziteti = new ArrayList<>();
		this.normale = new ArrayList<>();
		this.normaleUvrhovima = new ArrayList<>();
		this.intenzitetiVrhova = new ArrayList<>();

		// stavi sve koordinate u [-1, 1]
		workspace();

		for (final RealVector p : poligoni) {
			calculatePlain(view, p);
		}

		calculateNormalsInVertices();

		calculateGouraud(view);
	}

	private List<RealVector> workspace() {

		double xmin = vrhovi.get(0).getEntry(0);
		double ymin = vrhovi.get(0).getEntry(1);
		double zmin = vrhovi.get(0).getEntry(2);
		double xmax = vrhovi.get(0).getEntry(0);
		double ymax = vrhovi.get(0).getEntry(1);
		double zmax = vrhovi.get(0).getEntry(2);

		for (final RealVector v : vrhovi) {

			if (v.getEntry(0) < xmin) {
				xmin = v.getEntry(0);

			} else if (v.getEntry(0) > xmax) {
				xmax = v.getEntry(0);

			} else if (v.getEntry(1) < ymin) {
				ymin = v.getEntry(1);

			} else if (v.getEntry(1) > ymax) {
				ymax = v.getEntry(1);

			} else if (v.getEntry(2) < zmin) {
				zmin = v.getEntry(2);

			} else if (v.getEntry(2) > zmax) {
				zmax = v.getEntry(2);
			}
		}

		final double sizeX = xmax - xmin;
		final double sizeY = ymax - ymin;
		final double sizeZ = zmax - zmin;
		final double centerX = (xmax + xmin) / 2;
		final double centerY = (ymax + ymin) / 2;
		final double centerZ = (zmax + zmin) / 2;
		final double scaling = 2 / Math.max(sizeX, Math.max(sizeY, sizeZ));

		for (final RealVector v : vrhovi) {
			v.setEntry(0, (v.getEntry(0) - centerX) * scaling);
			v.setEntry(1, (v.getEntry(1) - centerY) * scaling);
			v.setEntry(2, (v.getEntry(2) - centerZ) * scaling);
		}

		return vrhovi;
	}

	/**
	 * Method used for parsing the passed .obj file.
	 *
	 * @param filename
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static Figure parse(String filename, View view)
			throws NumberFormatException, IOException {

		final BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(filename))));
		String line;
		String[] input = new String[4];
		final List<RealVector> v = new ArrayList<>();
		final List<RealVector> p = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			input = line.split("\\s+");
			if (line.trim().isEmpty()) {
				continue;
			}
			if (input[0].equals("v")) {
				final double[] data = new double[4];

				for (int i = 0; i < 3; i++) {
					data[i] = Double.parseDouble(input[i + 1]);
				}

				data[3] = 1; // homogena koordinata
				v.add(new ArrayRealVector(data));
				continue;
			}
			if (input[0].equals("f")) {
				final double[] data = new double[3];

				for (int i = 0; i < 3; i++) {
					data[i] = Double.parseDouble(input[i + 1]);
				}

				p.add(new ArrayRealVector(data));
				continue;
			}
		}
		br.close();
		return new Figure(v, p, view);
	}

	@Override
	public String toString() {
		return "Vertices: " + vrhovi.toString() + "\n" + "Polygons: "
				+ poligoni.toString();
	}

	/**
	 * Method which calculates plain values and polygon intensities for constant
	 * shading.
	 *
	 * @param view
	 * @param p
	 */
	public void calculatePlain(View view, RealVector p) {
		double x1, x2, x3, y1, y2, y3, z1, z2, z3;
		double A, B, C, D;
		int point;

		final RealVector polygon = p;

		// prvi vrh poligona
		point = (int) polygon.getEntry(0) - 1;
		x1 = vrhovi.get(point).getEntry(0);
		y1 = vrhovi.get(point).getEntry(1);
		z1 = vrhovi.get(point).getEntry(2);

		// drugi vrh poligona
		point = (int) polygon.getEntry(1) - 1;
		x2 = vrhovi.get(point).getEntry(0);
		y2 = vrhovi.get(point).getEntry(1);
		z2 = vrhovi.get(point).getEntry(2);

		// treci vrh poligona
		point = (int) polygon.getEntry(2) - 1;
		x3 = vrhovi.get(point).getEntry(0);
		y3 = vrhovi.get(point).getEntry(1);
		z3 = vrhovi.get(point).getEntry(2);

		A = (y2 - y1) * (z3 - z1) - (z2 - z1) * (y3 - y1);
		B = -(x2 - x1) * (z3 - z1) + (z2 - z1) * (x3 - x1);
		C = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
		D = -x1 * A - y1 * B - z1 * C;

		// izracunaj je li poligon prednji ili straznji
		final double xt = (x1 + x2 + x3) / 3;
		final double yt = (y1 + y2 + y3) / 3;
		final double zt = (z1 + z2 + z3) / 3;

		final Vector3D normala = new Vector3D(A, B, C).normalize();
		System.out.println("A =" + A + " B = " + B + " C = " + C + " --> "
				+ normala.toString());

		final Vector3D ocisteV = new Vector3D(view.ociste.getEntry(0),
				view.ociste.getEntry(1), view.ociste.getEntry(2));
		final Vector3D teziste = new Vector3D(xt, yt, zt);

		final Vector3D tezisteOciste = new Vector3D(ocisteV.getX()
				- teziste.getX(), ocisteV.getY() - teziste.getY(),
				ocisteV.getZ() - teziste.getZ());

		boolean prednji;
		if (Vector3D.dotProduct(normala, tezisteOciste) > 0) {
			prednji = true;
			polygonsToDraw.add(true);
		} else {
			polygonsToDraw.add(false);
			prednji = false;
		}

		normale.add(new ArrayRealVector(new double[] { normala.getX(),
				normala.getY(), normala.getZ() }));

		// izracunaj intenzitet poligona
		final Vector3D izvor = new Vector3D(view.izvor.getEntry(0),
				view.izvor.getEntry(1), view.izvor.getEntry(2));
		final Vector3D tezisteIzvor = new Vector3D(izvor.getX() - xt,
				izvor.getY() - yt, izvor.getZ() - zt);

		final Vector3D L = tezisteIzvor.normalize();
		final Vector3D N = normala;

		final double ka = 0.3; // 0-1
		final double Ia = 100; // 0-255
		final double Ii = 200;
		final double kd = 0.78125; // 0-1

		double I;
		if (prednji) {
			I = Ia * ka + Ii * kd * Math.max((Vector3D.dotProduct(L, N)), 0);
		} else {
			I = Ia * ka;
		}

		inteziteti.add(I);
	}

	/**
	 * Method which calculates the normal values in all the vertices.
	 */
	public void calculateNormalsInVertices() {
		for (int i = 0; i < vrhovi.size(); i++) {
			final ArrayList<RealVector> n = new ArrayList<>();

			// dodaj u listu normale svih poligona kojima pripada vrh i
			for (int j = 0; j < poligoni.size(); j++) {
				if ((poligoni.get(j).getEntry(0) - 1 == i)
						|| (poligoni.get(j).getEntry(1) - 1 == i)
						|| (poligoni.get(j).getEntry(2) - 1 == i)) {
					n.add(normale.get(j));
				}
			}

			double nx = 0, ny = 0, nz = 0;

			for (final RealVector normala : n) {
				nx += normala.getEntry(0);
				ny += normala.getEntry(1);
				nz += normala.getEntry(2);
			}

			nx /= n.size();
			ny /= n.size();
			nz /= n.size();

			final double len = Math.sqrt(nx * nx + ny * ny + nz * nz);
			normaleUvrhovima.add(new ArrayRealVector(new double[] { nx / len,
					ny / len, nz / len }));
		}
	}

	/**
	 * Method which calculates verticess intensity using Gouraud's method of
	 * shading.
	 *
	 * @param i
	 * @param view
	 */
	public void calculateGouraud(View view) {

		final double ka = 0.3; // 0-1
		final double Ia = 150; // 0-255
		final double Ii = 255;
		final double kd = 0.78125; // 0-1

		double I;

		for (int i = 0; i < vrhovi.size(); i++) {
			final RealVector normala = normaleUvrhovima.get(i);
			final Vector3D izvor = new Vector3D(view.izvor.getEntry(0),
					view.izvor.getEntry(1), view.izvor.getEntry(2));
			final Vector3D vrhIzvor = new Vector3D(izvor.getX()
					- vrhovi.get(i).getEntry(0), izvor.getY()
					- vrhovi.get(i).getEntry(1), izvor.getZ()
					- vrhovi.get(i).getEntry(2));

			final Vector3D L = vrhIzvor.normalize();
			final Vector3D N;

			if (Math.abs(normala.getEntry(0)) == 0
					&& Math.abs(normala.getEntry(1)) == 0
					&& Math.abs(normala.getEntry(2)) == 0) {
				N = new Vector3D(0, 0, 0);
			} else {
				N = new Vector3D(normala.getEntry(0), normala.getEntry(1),
						normala.getEntry(2)).normalize();
			}

			I = Ia * ka + Ii * kd * Math.max((Vector3D.dotProduct(L, N)), 0);

			intenzitetiVrhova.add(I);
		}
	}

}