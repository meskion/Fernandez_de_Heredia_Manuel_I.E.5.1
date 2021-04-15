package videojuegos;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Validar {

	public static void validarFecha(LocalDate date){
		if (date.compareTo(LocalDate.now()) > 0) {
		
			throw new DateTimeException("la fecha es superior a la fecha actual");
		}

	}

	public static void validarNombre(String nombre) {
		if (nombre == null || nombre.equals("")) {
			throw new IllegalArgumentException("Nombre vacio o nulo");
		}
	}

}
