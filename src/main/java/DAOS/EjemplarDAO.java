package DAOS;

import DTOS.Ejemplar;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;


public class EjemplarDAO implements EjemplarDaoInt {
    //Creación del Entity Manager
    private EntityManager em;

    //Constructor
    public EjemplarDAO(EntityManager em) {
        this.em = em;
    }


    //Metodo para insertar Ejemplar
    @Override
    public void insertEjemplar(Ejemplar ejemplar) {
        em.getTransaction().begin();
        em.persist(ejemplar);
        em.getTransaction().commit();

    }

    //Metodo para actualizar un ejemplar
    @Override
    public Ejemplar updateEjemplar(Ejemplar ejemplar) {
        em.getTransaction().begin();
        Ejemplar nuevoEjemplar = em.merge(ejemplar);
        em.getTransaction().commit();
        return nuevoEjemplar;
    }

    //Metodo para eliminar un ejemplar
    @Override
    public void deleteEjemplar(int id) {
        em.getTransaction().begin();
        Ejemplar ejemplar = em.find(Ejemplar.class, id);
        if (ejemplar != null) {
            em.remove(ejemplar);
        }
        em.getTransaction().commit();

    }


    //Metodo para ver el stock disponible de un ejemplar.
    @Override
    public int stockDisponible(Ejemplar ejemplar) {
        em.getTransaction().begin();
        int contador = 0;
        if (ejemplar.getEstado().equals("Disponible")){
            contador++;
        }
        em.getTransaction().commit();
        return contador;
    }

    //Metodo para conseguir un ejemplar a través de su ID.
    @Override
    public Ejemplar getEjemplar(int id) {
        em.getTransaction().begin();
        Ejemplar ejemplar = em.find(Ejemplar.class, id);
        em.getTransaction().commit();
        return ejemplar;
    }

    //Metodo para listar todos los ejemplares
    @Override
    public List<Ejemplar> findAllEjemplar() {
        TypedQuery<Ejemplar> query = em.createQuery("SELECT e FROM Ejemplar e", Ejemplar.class);
        return query.getResultList();
    }
}
