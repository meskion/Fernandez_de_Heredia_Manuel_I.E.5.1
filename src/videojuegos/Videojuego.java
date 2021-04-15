package videojuegos;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Clase que describe un videojuego para ser almacenado por el sistema de la
 * tienda.
 * 
 * @author manuf
 *
 */

public class Videojuego implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private static Integer contadorCodigo = 1;
	private String nombre;
	private Plataforma plataforma;
	private LocalDate lanzamiento;

	public Videojuego(String nombre, Plataforma plataforma, LocalDate lanzamiento){

		setNombre(nombre);
		setPlataforma(plataforma);
		setLanzamiento(lanzamiento);
		this.codigo = contadorCodigo++;
	}
	
	

	public static Integer getContadorCodigo() {
		return contadorCodigo;
	}



	public static void setContadorCodigo(Integer contadorCodigo) {
		Videojuego.contadorCodigo = contadorCodigo;
	}



	public Integer getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		Validar.validarNombre(nombre);
		this.nombre = nombre;
	}

	public Plataforma getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(Plataforma plataforma) {
		this.plataforma = plataforma;
	}

	public LocalDate getLanzamiento() {
		return lanzamiento;
	}

	public void setLanzamiento(LocalDate lanzamiento){
		Validar.validarFecha(lanzamiento);
		this.lanzamiento = lanzamiento;
	}

	@Override
	public String toString() {
		return String.join("",
				"codigo del videojuego: ", codigo.toString(),
				"\n nombre del videojuego:", nombre,
				"\n plataforma: ", plataforma.toString(),
				"\n lanzamiento: ", lanzamiento.toString(), "\n");
	}
	
	

}
