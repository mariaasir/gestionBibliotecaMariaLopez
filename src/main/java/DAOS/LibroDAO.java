package DAOS;

import DTOS.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;


public class LibroDAO implements LibroDaoInt {
    //Creación del Entity Manager
    private EntityManager em;

    //Constructor
    public LibroDAO(EntityManager em) {
        this.em = em;
    }

    //Metodo para insertar un libro
    @Override
    public void insertLibro(Libro libro) {
        em.getTransaction().begin();
        em.persist(libro);
        em.getTransaction().commit();
    }


    //Metodo para actualizar un libro
    @Override
    public Libro updateLibro(Libro libro) {
        em.getTransaction().begin();
        Libro nuevoLibro = em.merge(libro);
        em.getTransaction().commit();
        return nuevoLibro;
    }


    //Metodo para eliminar un libro a partir de su ISBN
    @Override
    public void deleteLibro(String isbn) {
        em.getTransaction().begin();
        Libro libro = em.find(Libro.class, isbn);
        if (libro != null) {
            em.remove(libro);
        }
        em.getTransaction().commit();
    }


    //Metodo para conseguir un libro a partir de su ISBN
    @Override
    public Libro getLibroByISBN(String isbn) {
        em.getTransaction().begin();
        Libro libro = em.find(Libro.class, isbn);
        em.getTransaction().commit();
        return libro;
    }


    //Metodo para guardar todos los libros en una lista.
    @Override
    public List<Libro> getAllLibros() {
        TypedQuery<Libro> query = em.createQuery("SELECT l FROM Libro l", Libro.class);
        return query.getResultList();
    }
}
