package videojuegos;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Clase con metodos estaticos para validar atributos de la clase Videojuego
 * 
 * @author manuf
 *
 */
public class Validar {

	/**
	 * comprueba que una fecha de salida de un videojuego sea valida, que el
	 * videojuego haya salido ya. Si la fecha esta en el futuro, lanza una excpecion
	 * del tipo DateTimeException
	 * 
	 * @param date
	 */
	public static void validarFecha(LocalDate date) {
		if (date.compareTo(LocalDate.now()) > 0) {

			throw new DateTimeException("la fecha es superior a la fecha actual");
		}

	}

	/**
	 * Comprueba que el nombre del videojuego sea valido, que no sea null o una
	 * cadena vacia, y lanza IllegalArgumentException si no es valido.
	 * 
	 * @param nombre
	 */
	public static void validarNombre(String nombre) {
		if (nombre == null || nombre.equals("")) {
			throw new IllegalArgumentException("Nombre vacio o nulo");
		}
	}

}
