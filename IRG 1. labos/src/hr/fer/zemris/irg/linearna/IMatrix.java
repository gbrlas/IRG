package hr.fer.zemris.irg.linearna;

/**
 * Sučelje koje predstavlja matricu realnih brojeva. Matrice predstavljene ovim
 * sučeljem su matrice realnih brojeva i mogu biti proizvoljnih dimenzija.
 * Sučelje definira osnovni skup metoda za dobivanje informacija o matrici te za
 * provođenje čestih operacija nad matricama.
 *
 * @author marcupic
 */
public interface IMatrix {
	/**
	 * Metoda za dohvat broja redaka matrice.
	 *
	 * @return broj redaka
	 */
	int getRowsCount();

	/**
	 * Metoda za dohvat broja stupaca matrice.
	 *
	 * @return broj stupaca
	 */
	int getColsCount();

	/**
	 * Metoda za dohvat elementa matrice u zadanom retku i stupcu.
	 *
	 * @param row
	 *            redak, ide od 0 na više
	 * @param col
	 *            stupac, ide od 0 na više
	 * @return traženi element
	 */
	double get(int row, int col);

	/**
	 * Metoda za postavljanje vrijednosti u zadani redak i stupac.
	 *
	 * @param row
	 *            redak, ide od 0 na više
	 * @param col
	 *            stupac, ide od 0 na više
	 * @param value
	 *            vrijednost koju treba postaviti
	 * @return referencu na matricu
	 */
	IMatrix set(int row, int col, double value);

	/**
	 * Metoda za vraćanje nove kopije trenutne matrice. Najčešće će
	 * implementacija metodom {@link #newInstance(int, int)} stvoriti novu
	 * matricu i potom u nju iskopirati podatke.
	 *
	 * @return referencu na novu kopiju koja nije nikako povezana s izvornom
	 *         matricom.
	 */
	IMatrix copy();

	/**
	 * Metoda za stvaranja nove matrice zadanih dimenzija. Implementacija
	 * matrice koja će se dobiti određena je stvarnom implementacijom primjerka
	 * razreda nad kojim je ova metoda pozvana. Najčešće, implementacija će
	 * odlučiti konstruirati novu matricu jednake implementacije kao što je i
	 * sama trenutna matrica, osim ako se ne radi o kakvom proxyju (u tom
	 * slučaju zahtjev se može proslijediti originalnom objektu na koji Proxy
	 * gleda pa će on odlučiti o implementaciji).
	 *
	 * @param rows
	 *            željeni broj redaka
	 * @param cols
	 *            željeni broj stupaca
	 * @return referenca na novu matricu
	 */
	IMatrix newInstance(int rows, int cols);

	/**
	 * Metoda omogućava stvaranje transponirane matrice u odnosu na originalnu
	 * matricu. Ako je parametar {@code liveView} postavljen na {@code true},
	 * objekt koji se vrati mora biti "živi" pogled na originalnu matricu (za
	 * rješavanje ovoga prikladan je oblikovni obrazac Proxy). U suprotnom
	 * stvara se nova matrica koja čuva vlastitu kopiju podataka.
	 *
	 * @param liveView
	 *            želi li se dobiti živi pogled
	 * @return referenca na transponiranu matricu
	 */
	IMatrix nTranspose(boolean liveView);

	/**
	 * Metoda trenutnoj matrici dodaje zadanu matricu (provodi operaciju
	 * zbrajanja); pri tome se originalna matrica direktno mijenja.
	 *
	 * @param other
	 *            matrica koju treba pribrojiti
	 * @return referenca na trenutnu matricu
	 */
	IMatrix add(IMatrix other);

	/**
	 * Metoda stvara novu matricu koja odgovara zbroju trenutne matrice i
	 * predane matrice. Originalne matrice ostaju nepromijenjene.
	 *
	 * @param other
	 *            matrica koju treba pribrojiti
	 * @return referenca na novu matricu
	 */
	IMatrix nAdd(IMatrix other);

	/**
	 * Metoda od trenutne matrice oduzima zadanu matricu (provodi operaciju
	 * odbijanja); pri tome se originalna matrica direktno mijenja.
	 *
	 * @param other
	 *            matrica koju treba odbiti
	 * @return referenca na trenutnu matricu
	 */
	IMatrix sub(IMatrix other);

	/**
	 * Metoda stvara novu matricu koja odgovara razlici trenutne matrice i
	 * predane matrice. Originalne matrice ostaju nepromijenjene.
	 *
	 * @param other
	 *            matrica koju treba odbiti
	 * @return referenca na novu matricu
	 */
	IMatrix nSub(IMatrix other);

	/**
	 * Metoda stvara novu matricu koja odgovara matričnom umnošku trenutne
	 * matrice i predane matrice. Originalne matrice ostaju nepromijenjene.
	 *
	 * @param other
	 *            matrica s kojom treba pomnožiti trenutnu
	 * @return referenca na novu matricu
	 */
	IMatrix nMultiply(IMatrix other);

	/**
	 * Metoda računa determinantu trenutne matrice.
	 *
	 * @return determinantu
	 * @throws IncompatibleOperandException
	 *             ako matrica nije kvadratna
	 */
	double determinant() throws IncompatibleOperandException;

	/**
	 * Metoda vraća matricu koja odgovara trenutnoj matrici nakon izbacivanja
	 * zadanog retka i zadanog stupca (oboje se numerira od 0). Ta nova matrica
	 * imat će za jedan manje broj redaka i za jedan manje broj stupaca u odnosu
	 * na trenutnu matricu. Ako je parametar {@code liveView} postavljen na
	 * {@code true}, metoda mora vratiti živi pogled na izvornu matricu; to
	 * znači da, primjerice, ako u ovom pogledu izmjenimo neki element, promjena
	 * će se vidjeti i u originalnoj matrici na odgovarajućem mjestu (hint:
	 * prikladan je oblikovni obrazac Proxy). Ako je {@code liveView} postavljen
	 * na {@code false}, stvara se nova matrica koja ima svoju vlastitu kopiju
	 * podataka.
	 *
	 * @param row
	 *            redak koji treba izbaciti
	 * @param col
	 *            stupac koji treba izbaciti
	 * @param liveView
	 *            želi li se dobiti živi pogled
	 * @return matricu koja predstavlja podmatricu
	 */
	IMatrix subMatrix(int row, int col, boolean liveView);

	/**
	 * Metoda provodi postupak invertiranja zadane matrice i vraća novu matricu
	 * koja je inverz. Metoda mora biti implementirana uporabom matrice
	 * kofaktora kako je to opisano u knjizi (osim ako baš jako ne želite neku
	 * kompliciraniju ali bitno efikasniju metodu). Ovdje nije naglasak na
	 * efikasnosti implementacije. U slučaju da matrica nije invertibilna,
	 * očekuje se da će biti izazvana {@link UnsupportedOperationException}.
	 * Originalna matrica ostaje nepromijenjena.
	 *
	 * @return referencu na novu matricu koja je inverz trenutne
	 */
	IMatrix nInvert();

	/**
	 * Sadržaj trenutne matrice kopira u dvodimenzijsko polje koje potom vraća.
	 *
	 * @return polje s kopijom sadržaja matrice
	 */
	double[][] toArray();

	/**
	 * Temeljem trenutne matrice stvara vektor. Ovo je dakako legalno samo ako
	 * je matrica jednoretčana ili jednostupčana. Ako je {@code liveView}
	 * postavljen na {@code true}, treba se vratiti živi pogled na trenutnu
	 * matricu. Ako je {@code liveView} postavljen na {@code false}, treba se
	 * vratiti referenca na vektor koji čuva svoju vlastitu kopiju podataka.
	 *
	 * @param liveView
	 *            želi li se dobiti živi pogled
	 * @return referenca na odgovarajući vektor
	 */
	IVector toVector(boolean liveView);

	/**
	 * Vraća novu matricu čiji su svi elementi jednaki elementima trenutne
	 * matrice pomnoženima s zadanom vrijednosti. Originalna matrica ostaje
	 * nepromijenjena.
	 *
	 * @param value
	 *            vrijednost s kojom se množe svi elementi
	 * @return novu matricu
	 */
	IMatrix nScalarMultiply(double value);

	/**
	 * Sve elemente trenutne matrice množi sa zadanom vrijednostima. Vraća
	 * referencu na trenutnu matricu.
	 *
	 * @param value
	 *            vrijednost s kojom se množe svi elementi
	 * @return referencu na trenutnu matricu
	 */
	IMatrix scalarMultiply(double value);

	/**
	 * Modificira trenutnu matricu tako da postaje jedinična matrica.
	 *
	 * @return referencu na trenutnu matricu
	 */
	IMatrix makeIdentity();
}
