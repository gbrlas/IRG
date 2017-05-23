package hr.fer.zemris.irg.linearna;


/**
 * Sučelje predstavlja model jednog proizvoljno velikog vektora (veličina će,
 * naravno, prilikom stvaranja odgovarajućih objekata biti fiksirana u trenutku
 * stvaranja).
 *
 * @author marcupic
 *
 */
public interface IVector {
	/**
	 * Metoda dohvaća zadanu komponentu vektora.
	 *
	 * @param index
	 *            indeks komponente; kreće se od 0 pa do dimension-1
	 * @return traženu vrijednost ili baca iznimku ako je indeks izvan
	 *         dozvoljenih granica
	 * @throws IndexOutOfBoundsException
	 *             ako se zatraži nepostojeći element
	 */
	double get(int index);

	/**
	 * Postavlja zadanu komponentu na vrijednost koja je predana.
	 *
	 * @param index
	 *            indeks komponente; kreće od 0 pa do dimension-1
	 * @param value
	 *            vrijednost na koju treba postaviti komponentu
	 * @return referencu na trenutni vektor
	 * @throws UnmodifiableObjectException
	 *             ako je vektor nepromjenjiv (read-only)
	 */
	IVector set(int index, double value) throws UnmodifiableObjectException;

	/**
	 * Metoda vraća dimenzionalnost vektora.
	 *
	 * @return dimenzionalnost
	 */
	int getDimension();

	/**
	 * Vraća novu kopiju trenutnog vektora koja se može slobodno mijenjati i
	 * koja nije povezana s originalnim vektorom. Originalni vektor se ne
	 * mijenja. Najčešće će implementacija metodom {@link #newInstance(int)}
	 * stvoriti odgovarajući novi primjerak i potom u njega prekopirati podatke.
	 *
	 * @return referencu na novu kopiju vektora
	 */
	IVector copy();

	/**
	 * Vraća novi vektor čija je dimenzionalnost jednaka {@code n} (argument
	 * metode). Pri tome se iz trenutnog vektora u novi kopira onoliko elemenata
	 * koliko to {@code n} dopusti. Ako je novi vektor veće dimenzionalnosti,
	 * ostatak se inicijalizira na vrijednost 0. Originalni vektor se ne
	 * mijenja.
	 *
	 * @param n
	 *            željena dimenzionalnost novog vektora
	 * @return referencu na novi vektor
	 */
	IVector copyPart(int n);

	/**
	 * Metoda vraća novi primjerak vektora zadane dimenzije. Vektor ima svoj
	 * zaseban spremnik vrijednosti komponenata i ni na koji način nije povezan
	 * s trenutnim vektorom (osim što dijeli logiku koja odlučuje koji će se
	 * konkretni razred koristiti za taj novi vektor). Implementacija vektora
	 * koja će se dobiti određena je stvarnom implementacijom primjerka razreda
	 * nad kojim je ova metoda pozvana. Najčešće, implementacija će odlučiti
	 * konstruirati novi vektor jednake implementacije kao što je i sam trenutni
	 * vektor, osim ako se ne radi o kakvom proxyju (u tom slučaju zahtjev se
	 * može proslijediti originalnom objektu na koji Proxy gleda pa će on
	 * odlučiti o implementaciji).
	 *
	 * @param dimension
	 *            dimenzionalnost vektora
	 * @return referenca na novi vektor
	 */
	IVector newInstance(int dimension);

	/**
	 * Trenutni vektor modificira tako sto mu dodaje predani vektor.
	 *
	 * @param other
	 *            vektor za koji se treba uvećati trenutni vektor
	 * @return referencu na trenutni vektor
	 * @throws IncompatibleOperandException
	 *             ako predani vektor nije kompatibilan s trenutnim
	 */
	IVector add(IVector other) throws IncompatibleOperandException;

	/**
	 * Vraća novi vektor koji je jednak trenutnom vektoru uvećanom za predani
	 * vektor. Originalni vektor se ne mijenja.
	 *
	 * @param other
	 *            vektor za koji se treba uvećati trenutni vektor
	 * @return referencu na novi vektor koji predstavlja sumu
	 * @throws IncompatibleOperandException
	 *             ako predani vektor nije kompatibilan s trenutnim
	 */
	IVector nAdd(IVector other) throws IncompatibleOperandException;

	/**
	 * Trenutni vektor modificira tako što mu oduzima predani vektor.
	 *
	 * @param other
	 *            vektor za koji se treba umanjiti trenutni vektor
	 * @return referencu na trenutni vektor
	 * @throws IncompatibleOperandException
	 *             ako predani vektor nije kompatibilan s trenutnim
	 */
	IVector sub(IVector other) throws IncompatibleOperandException;

	/**
	 * Vraća novi vektor koji je jednak trenutnom vektoru umanjenom za predani
	 * vektor. Originalni vektor se ne mijenja.
	 *
	 * @param other
	 *            vektor za koji se treba umanjiti trenutni vektor
	 * @return referencu na novi vektor koji predstavlja razliku
	 * @throws IncompatibleOperandException
	 *             ako predani vektor nije kompatibilan s trenutnim
	 */
	IVector nSub(IVector other) throws IncompatibleOperandException;

	/**
	 * Trenutni vektor modificira tako što ga skalarno množi sa zadanim
	 * skalarom.
	 *
	 * @param byValue
	 *            vrijednost skalara
	 * @return referencu na trenutni vektor
	 */
	IVector scalarMultiply(double byValue);

	/**
	 * Vraća novi vektor koji je jednak trenutnom koji je pomnožen sa zadanim
	 * skalarom. Originalni vektor se ne mijenja.
	 *
	 * @param byValue
	 *            vrijednost skalara
	 * @return referencu na novi vektor
	 */
	IVector nScalarMultiply(double byValue);

	/**
	 * Metoda vraća normu trenutnog vektora.
	 *
	 * @return normu
	 */
	double norm();

	/**
	 * Metoda normalizira trenutni vektor.
	 *
	 * @return referencu na trenutni vektor
	 */
	IVector normalize();

	/**
	 * Metoda vraća novi vektor koji je jednak normaliziranom trenutnom vektoru.
	 * Originalni vektor se ne mijenja.
	 *
	 * @return referencu na novi vektor
	 */
	IVector nNormalize();

	/**
	 * Metoda računa i vraća kosinus kuta između trenutnog vektora i predanog
	 * vektora.
	 *
	 * @param other
	 *            drugi vektor
	 * @return kosinus pripadnog kuta
	 * @throws IncompatibleOperandException
	 *             ako predani vektor nije kompatibilan s trenutnim
	 */
	double cosine(IVector other) throws IncompatibleOperandException;

	/**
	 * Metoda računa skalarni produkt trenutnog vektora i zadanog vektora.
	 *
	 * @param other
	 *            vektor s kojim se trenutni množi
	 * @return vrijednost skalarnog produkta
	 * @throws IncompatibleOperandException
	 *             ako predani vektor nije kompatibilan s trenutnim
	 */
	double scalarProduct(IVector other) throws IncompatibleOperandException;

	/**
	 * Metoda vraća novi vektor koji je jednak vektorskom produktu izmedu
	 * trenutnog vektora i zadanog vektora. Originalni vektor se ne mijenja.
	 *
	 * @param other
	 *            vektor s kojim se računa vektorski produkt
	 * @return referencu na novi vektor
	 * @throws IncompatibleOperandException
	 *             ako dimenzionalnost trenutnog ili predanog vektora nije 3
	 */
	IVector nVectorProduct(IVector other) throws IncompatibleOperandException;

	/**
	 * Vraća vektor u radom prostoru koji se dobije iz trenutnog ako se trenutni
	 * promatra kao da je u homogenom prostoru. To će biti vektor za jedan manje
	 * dimenzionalnosti od trenutnog kod kojeg su sve komponente jednake
	 * trenutnim podijeljenim sa zadnjom. Ako je posljednja komponenta nula (pa
	 * dijeljenje nije definirano), događa se iznimka. Originalni vektor se ne
	 * mijenja.
	 * 
	 * @return referencu na novi vektor
	 * @throws UnsupportedOperationException
	 *             ako je posljednja komponenta originalnog vektora jednaka 0.
	 */
	IVector nFromHomogeneus();

	/**
	 * Trenutni vektor konvertira u jednoretčanu matricu. Ako je
	 * {@code liveView} postavljen na {@code true}, vraća objekt koji je živi
	 * pogled na taj vektor -- primjerice, ako se u vektoru promijeni neka
	 * komponenta, i ovaj pogled će toga biti svjestan; ako korisnik preko
	 * dobivene matrice napravi promjenu, ta će promjena biti vidljiva i u
	 * vektoru. Ako je {@code liveView} postavljen na {@code false}, generirana
	 * matrica čuva kopiju izvornih podataka. Originalni vektor se ne mijenja.
	 * 
	 * @param liveView
	 *            treba li vratiti živi pogled
	 * @return matrica dobivena temeljem trenutnog vektora
	 */
	IMatrix toRowMatrix(boolean liveView);

	/**
	 * Trenutni vektor konvertira u jednostupčanu matricu. Ako je
	 * {@code liveView} postavljen na {@code true}, vraća objekt koji je živi
	 * pogled na taj vektor -- primjerice, ako se u vektoru promijeni neka
	 * komponenta, i ovaj pogled će toga biti svjestan; ako korisnik preko
	 * dobivene matrice napravi promjenu, ta će promjena biti vidljiva i u
	 * vektoru. Ako je {@code liveView} postavljen na {@code false}, generirana
	 * matrica čuva kopiju izvornih podataka.
	 * 
	 * @param liveView
	 *            treba li vratiti živi pogled
	 * @return matrica dobivena temeljem trenutnog vektora
	 */
	IMatrix toColumnMatrix(boolean liveView);

	/**
	 * Vraća kopiju vektora predstavljenog kao polje.
	 *
	 * @return polje
	 */
	double[] toArray();
}
