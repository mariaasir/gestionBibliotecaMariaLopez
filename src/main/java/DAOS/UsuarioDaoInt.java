package DAOS;

import DTOS.Usuario;

import java.util.List;

/**
 * @author María López Patón 2ºDAM
 */


public interface UsuarioDaoInt {
    void insertUsuario(Usuario usuario);
    Usuario updateUsuario(Usuario usuario);
    void deleteUsuario(int id);
    Usuario getUsuario(int id);
    List<Usuario> getUsuarios();
}
