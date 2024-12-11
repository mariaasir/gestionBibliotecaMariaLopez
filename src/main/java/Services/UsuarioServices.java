package Services;

import DAOS.PrestamoDaoInt;
import DAOS.UsuarioDaoInt;
import DTOS.Ejemplar;
import DTOS.Usuario;
import Validaciones.Validaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author María López Patón 2ºDAM
 */


public class UsuarioServices {
    //Inicializacion de los DAOS y la  lista que guardará los usuarios en memoria.

    private UsuarioDaoInt usuarioDAO;
    private List<Usuario> usuariosEnMemoria = new ArrayList<>();
    Validaciones validar = new Validaciones();
    Scanner scanner = new Scanner(System.in);



    //Constructor
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

        Usuario usuario = new Usuario(dni, nombre, email, password, tipoUsuario);       //Crea un nuevo usuario

        if (validar.validarDni(dni)) {          //Valida que el dni sea correcto
            if (validar.validarCorreo(email)) {         //Valida que el correo electrónico sea correcto
                if (validar.validarTipo(tipoUsuario)) {         //Valida que el tipo de usuario sea correcto
                    usuarioDAO.insertUsuario(usuario);          //Si todas las validaciones son correctas, insertará el usuario
                    sincronizarUsuarios();          //Sincroniza los datos en memoria
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
        int id = scanner.nextInt();         //Recoge el ID del usuario a actualizar
        Usuario usuario = usuarioDAO.getUsuario(id);                //Guarda el usuario al que pertenece el id
        scanner.nextLine();
        System.out.print("Introduce el Nombre: ");
        usuario.setNombre(scanner.nextLine());          //Cambia el nombre
        System.out.print("Introduce el email: ");
        usuario.setEmail(scanner.nextLine());       //Cambia el email
        System.out.print("Introduce el password: ");
        usuario.setPassword(scanner.nextLine());        //Cambia la contraseña
        System.out.print("Introduce el tipo de usuario ( Normal / Administrador ): ");
        usuario.setTipo(scanner.nextLine());        //Cambia el tipo de usuario

        if (validar.validarDni(usuario.getDni())) {         //Valida que el DNI sea correcto
            if (validar.validarCorreo(usuario.getEmail())) {        //Valida que el correo sea correcto
                if (validar.validarTipo(usuario.getTipo())) {       //Valida que el tipo de usuario sea correcto
                    usuarioDAO.updateUsuario(usuario);          //Si todo es correcto, lo actualiza
                    sincronizarUsuarios();              //Sincroniza los datos en memoria
                } else {
                    throw new Exception("El correo electrónico no es válido.");             //Si los datos no son correctos, lanza excepciones
                }
            } else {
                throw new Exception("El DNI no es válido");
            }

        }
    }



    //Metodo para eliminar un usuario
    public void borrarUsuario() {
        System.out.print("Introduce el ID del usuario a eliminar: ");
        int id = scanner.nextInt();     //Recoge el ID del usuario
        usuarioDAO.deleteUsuario(id);       //Elimina el usuario
        sincronizarUsuarios();      //Sincroniza los datos en memoria
    }


    //Metodo que devuelve una lista de todos los usuarios
    public List<Usuario> obtenerUsuarios() {
        return usuariosEnMemoria;
    }


    //Sincroniza los datos en memoria
    public void sincronizarUsuarios() {
        usuariosEnMemoria = usuarioDAO.getUsuarios();
    }
}
