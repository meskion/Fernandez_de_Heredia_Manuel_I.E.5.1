package videojuegos;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Clase con metodos estaticos que maneja la aplicación de la tienda y accede y
 * manipula el fichero con los datos almacenados de los videojuegos
 * 
 * @author manuf
 *
 */
public class TiendaVideojuegos {

	private static Map<Integer, Videojuego> videojuegos;
	private static Scanner sc;
	private static boolean cambiosPendientes = false;

	private static void printMenuPrincipal() {
		System.out.println("================================================");
		System.out.println("\t======== Gestión de Videojuegos ========");
		System.out.println("================================================");
		System.out.println("");
		System.out.println("1.- Añadir un videojuego.");
		System.out.println("2.- Listar videojuegos.");
		System.out.println("3.- Borrar un videojuego.");
		System.out.println("4.- Guardar datos en fichero.");
		System.out.println("5.- Recuperar datos desde fichero.");
		System.out.println("");
		System.out.println("0.- Salir de la aplicación.");
		System.out.println("========================================");
		System.out.println("Introduzca la opción elegida:");

	}

	/**
	 * Metodo que se llama en main, inicializa las variables globales como el
	 * escaner o el mapa de Videojuegos, y comprueba el estado del fichero, cargando
	 * si es necesario
	 */
	public static void start() {
		videojuegos = new HashMap<>();
		sc = new Scanner(System.in);
		if (Files.exists(Path.of("videojue.dat"))) {
			leerVideojuegos("videojue.dat");
		} else {
			System.out.println("No existe ningun dato guardado sobre los videojuegos, se creará un archivo nuevo");
		}

		menuPrincipal();
	}

	/**
	 * metodo que lee los objetos 'Videojuego' del archivo y los guarda en el
	 * HashMap de la clase para su uso en la sesion
	 * 
	 * @param string
	 */
	private static void leerVideojuegos(String string) {

		videojuegos.clear();
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {

			fin = new FileInputStream(string);
			ois = new ObjectInputStream(fin);

			do {
				Videojuego v = (Videojuego) ois.readObject();
				videojuegos.put(v.getCodigo(), v);
			} while (true);

		} catch (EOFException eof) {
			System.out.println(" --------------------------");
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("El archivo no contiene la clase serializable esperada");
			e.printStackTrace();
		} finally {
			videojuegos.keySet().stream().max(Integer::compareTo).ifPresent(m -> Videojuego.setContadorCodigo(m + 1));

			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Metodo con el switch de opciones de la aplicación que el usuario navega.
	 */
	private static void menuPrincipal() {

		boolean continuar = true;
		do {

			printMenuPrincipal();
			String option = sc.nextLine();

			switch (option) {
			case "1":
				addVideojuego();
				break;
			case "2":
				listarVideojuegos();
				break;
			case "3":
				borrarVideojuego();
				break;
			case "4":
				guardarDatos();
				break;
			case "5":
				recuperarDatos();
				break;
			case "0":
				salir();
				continuar = false;
				break;
			default:
				System.err.println("Opcion no valida");
				break;
			}
		} while (continuar);

	}

	/**
	 * añade un videojuego al HashMap de la clase, leyendo valores por consola.
	 */
	private static void addVideojuego() {
		System.out.println("Introduzca los datos del videojuego:");
		boolean continuar;
		do {
			continuar = false;
			try {
				System.out.println("Nombre del videojuego:");
				String nombre = sc.nextLine();

				System.out.println("Plataforma Principal:");
				Plataforma plataforma = Plataforma.valueOf(sc.nextLine());

				System.out.println("Fecha de lanzamiento: ");
				LocalDate fecha = LocalDate.parse(sc.nextLine());

				Videojuego v = new Videojuego(nombre, plataforma, fecha);
				videojuegos.put(v.getCodigo(), v);
				System.out.println("Se ha creado el nuevo videojuego");
				cambiosPendientes = true;

			} catch (IllegalArgumentException e) {
				System.err.println("Dato introducido con formato no valido");
				continuar = true;
			} catch (DateTimeException e) {
				System.err.println("Fecha no valida");
				continuar = true;
			}
		} while (continuar);

	}

	/**
	 * Lista los videojuegos del HashMap por consola
	 */
	private static void listarVideojuegos() {
		videojuegos.values().forEach(System.out::println);

	}

	/**
	 * Elimina un videojuego segun un codigo leido por consola, y pide confirmación
	 * antes de realizar la operacion final, por seguridad.
	 */
	private static void borrarVideojuego() {

		Integer codigo = null;
		boolean continuar;
		do {
			System.out.println("Codigo del videojuego a borrar:");
			continuar = false;
			try {
				codigo = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				continuar = true;
				System.err.println("Formato de numero no valido");
			}
		} while (continuar);

		if (videojuegos.containsKey(codigo)) {
			System.out.println("Se va a proceder a borrar de la lista:");
			System.out.println(videojuegos.get(codigo).toString());
			System.out.println("¿Desea continuar con el borrado? (S/N):");
			if (sc.nextLine().toUpperCase().equals("S")) {
				videojuegos.remove(codigo);
				System.out.println("Videojuego borrado");
				cambiosPendientes = true;
			} else
				System.out.println("Borrado cancelado");
		}

	}

	/**
	 * Guarda los datos temporales en el HashMap en el fichero "videojue.dat". Si no
	 * existiese ese fichero lo crearía.
	 */
	private static void guardarDatos() {
		if (cambiosPendientes) {
			try {
				FileOutputStream fs = new FileOutputStream("videojue.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fs);

				for (Videojuego v : videojuegos.values()) {
					oos.writeObject(v);
				}

				if (oos != null) {
					oos.close();
					fs.close();
				}

				System.out.println("Los datos se han guardado correctamente en el fichero \".\\videojue.dat\".");
				cambiosPendientes = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			System.out.println("No hay cambios por guardar");
	}

	/**
	 * Carga los datos de "videojue.dat" en la aplicación, eliminando cualquier
	 * cambio no guardado desde la ultima vez que se cargó desde fichero. El sistema
	 * pide confirmación de la operación si hay cambios que se van a perder
	 */
	private static void recuperarDatos() {
		if (cambiosPendientes) {
			System.out.println("Ha realizado cambios que no ha guardado en disco.");
			System.out.println("Si continúa la carga del archivo se restaurarán los datos");
			System.out.println("de disco y se perderán los cambios no guardados.");
			System.out.println("¿Desea continuar con la carga y restaurar los datos del archivo? (S/N)");

			if (sc.nextLine().toUpperCase().equals("S")) {
				leerVideojuegos("videojue.dat");
				cambiosPendientes = false;
				System.out.println("datos recuperados");
			} else
				System.out.println("recuperacion cancelada");
		}

	}

	/**
	 * Sale de la aplicación. Si el sistema detecta cambios sin guardar, da la opción de guardarlos antes de salir.
	 */
	private static void salir() {
		if (cambiosPendientes) {
			System.out.println(
					"Ha realizado cambios que no ha guardado en disco. ¿Desea guardarlos antes de salir? (S/N)");
			if (sc.nextLine().toUpperCase().equals("S")) {
				guardarDatos();
			}
		}

		System.out.println("Cerrando aplicación...");
	}

	
}
