package pruebas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import videojuegos.Plataforma;
import videojuegos.Videojuego;

class VideojuegoTest {

	static Videojuego v;
	@BeforeEach
	void setUp() throws Exception {
		 v = new Videojuego("Smash Bros", Plataforma.WII, LocalDate.of(2009, 12, 23));
	}

	@Test
	void testGetNombre() {
		assertEquals("Smash Bros", v.getNombre());
	}

	@Test
	void testSetNombre() {
		v.setNombre("papapa");
		assertEquals("papapa", v.getNombre());
		assertThrows(IllegalArgumentException.class,() ->v.setNombre(""));
		assertThrows(IllegalArgumentException.class,() ->v.setNombre(null));
	}

	@Test
	void testGetPlataforma() {
		assertEquals(Plataforma.WII, v.getPlataforma());
	}

	@Test
	void testSetPlataforma() {
		v.setPlataforma(Plataforma.DS);
		assertEquals(Plataforma.DS, v.getPlataforma());
	}

	@Test
	void testGetLanzamiento() {
		assertEquals(LocalDate.of(2009, 12, 23), v.getLanzamiento());
	}

	@Test
	void testSetLanzamiento() {
		LocalDate fechaTest = LocalDate.of(2009, 11, 10);
		v.setLanzamiento(fechaTest);
		assertEquals(fechaTest, v.getLanzamiento());
		assertThrows(DateTimeException.class,() ->v.setLanzamiento(LocalDate.of(2022, 2, 2)));
	}

	
}
