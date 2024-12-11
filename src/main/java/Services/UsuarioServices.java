package Services;

import DAOS.PrestamoDaoInt;
import DAOS.UsuarioDaoInt;
import DTOS.Ejemplar;
import DTOS.Usuario;
import Validaciones.Validaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UsuarioServices {
    private UsuarioDaoInt usuarioDAO;
    private List<Usuario> usuariosEnMemoria = new ArrayList<>();
    Validaciones validar = new Validaciones();
    Scanner scanner = new Scanner(System.in);


    public UsuarioServices(UsuarioDaoInt usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        sincronizarUsuarios();
    }


    //Metodo para insertar usuario
    public void insertarUsuario() throws Exception {
        System.out.print("Introduce el DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Introduce el Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce el email: ");
        String email = scanner.nextLine();
        System.out.print("Introduce el password: ");
        String password = scanner.nextLine();
        System.out.print("Introduce el tipo de usuario ( Normal / Administrador ): ");
        String tipoUsuario = scanner.nextLine();

        Usuario usuario = new Usuario(dni, nombre, email, password, tipoUsuario);

        if (validar.validarDni(dni)) {
            if (validar.validarCorreo(email)) {
                if (validar.validarTipo(tipoUsuario)) {
                    usuarioDAO.insertUsuario(usuario);
                    sincronizarUsuarios();
                } else {
                    throw new Exception("El usuario solo puede ser Normal o Administrador");
                }
            } else {
                throw new Exception("El correo electrónico no es válido.");
            }
        } else {
            throw new Exception("El DNI no es válido.");
        }
    }


    //Metodo para actualizar usuario
    public void actualizarUsuario() throws Exception {
        System.out.print("¿Qué usuario deseas actualizar? Introduce su ID: ");
        int id = scanner.nextInt();
        Usuario usuario = usuarioDAO.getUsuario(id);
        scanner.nextLine();
        System.out.print("Introduce el Nombre: ");
        usuario.setNombre(scanner.nextLine());
        System.out.print("Introduce el email: ");
        usuario.setEmail(scanner.nextLine());
        System.out.print("Introduce el password: ");
        usuario.setPassword(scanner.nextLine());
        System.out.print("Introduce el tipo de usuario ( Normal / Administrador ): ");
        usuario.setTipo(scanner.nextLine());

        if (validar.validarDni(usuario.getDni())) {
            if (validar.validarCorreo(usuario.getEmail())) {
                if (validar.validarTipo(usuario.getTipo())) {

                    usuarioDAO.updateUsuario(usuario);
                    sincronizarUsuarios();
                } else {
                    throw new Exception("El correo electrónico no es válido.");
                }
            } else {
                throw new Exception("El DNI no es válido");
            }

        }
    }


    public void borrarUsuario() {
        System.out.print("Introduce el ID del usuario a eliminar: ");
        int id = scanner.nextInt();
        usuarioDAO.deleteUsuario(id);
        sincronizarUsuarios();


    }

    public List<Usuario> obtenerUsuarios() {
        return usuariosEnMemoria;
    }

    public void sincronizarUsuarios() {
        usuariosEnMemoria = usuarioDAO.getUsuarios();
    }
}
