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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

	private static void listarVideojuegos() {
		videojuegos.values().forEach(System.out::println);

	}

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

	private static void guardarDatos() {
		if (cambiosPendientes) {
			// escribir el mapa en el fichero
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

	private static void test() {
		int cod = 1;

		List<Videojuego> a = new ArrayList<>();
		a.add(new Videojuego("aa", Plataforma.PC, LocalDate.of(1, 1, 1)));
		Videojuego vi = null;
		for (Videojuego v : a) {
			if (v.getCodigo().equals(1)) {

				vi = v;
			}

		}
		System.out.println(a.toString());
		a.remove(vi);
		System.out.println(a.toString());
	}

	public static void main(String[] args) {
		test();
	}
}
