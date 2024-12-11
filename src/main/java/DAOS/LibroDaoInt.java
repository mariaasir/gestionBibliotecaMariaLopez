package DAOS;

import DTOS.Libro;

import java.util.List;

/**
 * @author María López Patón 2ºDAM
 */


public interface LibroDaoInt {
    void insertLibro(Libro libro);
    Libro updateLibro(Libro libro);
    void deleteLibro(String isbn);
    Libro getLibroByISBN(String isbn);
    List<Libro> getAllLibros();
}
