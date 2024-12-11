package DAOS;

import DTOS.Prestamo;
import Services.PrestamoServices;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

/**
 * @author María López Patón 2ºDAM
 */


public class PrestamoDAO implements PrestamoDaoInt {
    //Creación del Entity Manager
    EntityManager em;


    public PrestamoServices prestamoService;


    //Constructor
    public PrestamoDAO(EntityManager em) {
        this.em = em;
    }


    //Metodo para insertar un prestamo
    @Override
    public void insertPrestamo(Prestamo prestamo) {
        em.getTransaction().begin();
        em.persist(prestamo);
        em.getTransaction().commit();
    }


    //Metodo para actualizar un prestamo
    @Override
    public void updatePrestamo(Prestamo prestamo) {
        em.getTransaction().begin();
        em.merge(prestamo);
        em.getTransaction().commit();
    }


    //Metodo para devolver un ejemplar de un prestamo
    @Override
    public void devolverPrestamo(Prestamo prestamo) {
        prestamo.getEjemplar().setEstado("Disponible"); //Vuelve a poner el estado del libro en Disponible
        updatePrestamo(prestamo);

    }


    //Metodo para conseguir el prestamos a partir del ID
    @Override
    public Prestamo getPrestamoById(int id) {
        em.getTransaction().begin();
        Prestamo prestamo = em.find(Prestamo.class, id);
        em.getTransaction().commit();
        return prestamo;
    }

public void deletePrestamo(int id) {
        em.getTransaction().begin();
        Prestamo prestamo = em.find(Prestamo.class, id);
        if (prestamo != null) {
            em.remove(prestamo);
        }
        em.getTransaction().commit();
}
    //Metodo para recoger todos los prestamos en una lista
    @Override
    public List<Prestamo> getAllPrestamos() {
        TypedQuery<Prestamo> query = em.createQuery("SELECT p FROM Prestamo p", Prestamo.class);
        return query.getResultList();
    }


}
