package DAOS;

import DTOS.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * @author María López Patón 2ºDAM
 */


public class UsuarioDAO implements UsuarioDaoInt {
    //Creación del Entity Manager
    EntityManager em;


    //Constructor
    public UsuarioDAO(EntityManager em) {
        this.em = em;
    }


    //Metodo para insertar un usuario
    @Override
    public void insertUsuario(Usuario usuario) {
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();

    }


    //Metodo para actualizar un usuario
    @Override
    public Usuario updateUsuario(Usuario usuario) {
        em.getTransaction().begin();
        Usuario usuario1 = em.merge(usuario);
        em.getTransaction().commit();
        return usuario1;
    }


    //Metodo para eliminar un usuario
    @Override
    public void deleteUsuario(int id) {
        em.getTransaction().begin();
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            em.remove(usuario);
        }
        em.getTransaction().commit();
    }


    //Metodo para conseguir un usuario a partir de su ID
    @Override
    public Usuario getUsuario(int id) {
        em.getTransaction().begin();
        Usuario usuario = em.find(Usuario.class, id);
        em.getTransaction().commit();
        return usuario;
    }

    //Metodo para recoger todos los usuarios en una lista
    @Override
    public List<Usuario> getUsuarios() {
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
        return query.getResultList();
    }
}
