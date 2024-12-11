import DAOS.*;
import DTOS.Ejemplar;
import DTOS.Libro;
import DTOS.Prestamo;
import DTOS.Usuario;
import Services.EjemplarServices;
import Services.LibroServices;
import Services.PrestamoServices;
import Services.UsuarioServices;
import Validaciones.Validaciones;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("miUnidad");
    static EntityManager em = emf.createEntityManager();

    static EjemplarServices ejemplarServices = new EjemplarServices(new EjemplarDAO(em), new LibroDAO(em));
    static PrestamoServices prestamoServices = new PrestamoServices(new PrestamoDAO(em), new UsuarioDAO(em), new EjemplarDAO(em), new Validaciones());
    static LibroServices libroServices = new LibroServices(new LibroDAO(em), new EjemplarDAO(em), prestamoServices);
    static UsuarioServices usuarioServices = new UsuarioServices(new UsuarioDAO(em));

    public static void menuAdmin(Usuario usuario) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("--------------- GESTIÓN ADMINISTRADOR-----------------");
            System.out.println("-----LISTAR-----");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Listar libros");
            System.out.println("3. Listar prestamos");
            System.out.println("4. Listar ejemplares");

            System.out.println("-----CREAR-----");
            System.out.println("5. Crear Usuario");
            System.out.println("6. Crear Libro");
            System.out.println("7. Crear Prestamo");
            System.out.println("8. Crear Ejemplar");

            System.out.println("-----ACTUALIZAR-----");
            System.out.println("9 . Actualizar Usuario");
            System.out.println("10. Actualizar Libro");
            System.out.println("11. Actualizar Ejemplar");

            System.out.println("-----ELIMINAR-----");
            System.out.println("12. Eliminar Usuario");
            System.out.println("13. Eliminar Libro");
            System.out.println("14. Eliminar Ejemplar");
            System.out.println("-----DEVOLVER-----");
            System.out.println("15. Devolver Prestamo");
            System.out.println("-----STOCK-----");
            System.out.println("16. OBTENER STOCK DE LIBROS");
            System.out.println("0. Salir.");
            System.out.print("Elige una opción: ");

            opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    for(Usuario u : usuarioServices.obtenerUsuarios()){
                        System.out.println(u);

                    }
                    break;
                case 2:
                    for (Libro l : libroServices.listarLibros()){
                        System.out.println(l);
                    }
                    break;
                case 3:
                    for (Prestamo p : prestamoServices.getPrestamos(usuario.getId())){
                        System.out.println(p);
                    }
                    break;
                case 4:
                    for (Ejemplar e : ejemplarServices.obtenerEjemplares()){
                        System.out.println(e);
                    }
                    break;
                case 5:
                    usuarioServices.insertarUsuario();
                    usuarioServices.sincronizarUsuarios();
                    break;
                case 6:
                    libroServices.insertarLibro();
                    libroServices.sincronizarLibro();
                    break;
                case 7:
                    System.out.println("-----USUARIOS------");
                    for(Usuario u : usuarioServices.obtenerUsuarios()){
                        System.out.println(u);

                    }
                    System.out.println();
                    System.out.println("-----EJEMPLARES------");
                    for (Ejemplar e : ejemplarServices.obtenerEjemplares()){
                        System.out.println(e);
                    }
                    prestamoServices.insertarPrestamos();
                    prestamoServices.sincronizarPrestamos();
                    break;
                case 8:
                    for (Libro l : libroServices.listarLibros()){
                        System.out.println(l);
                    }
                    ejemplarServices.insertarEjemplar();
                    ejemplarServices.sincronizarEjemplares();
                    break;
                case 9:
                    for(Usuario u : usuarioServices.obtenerUsuarios()){
                        System.out.println(u);

                    }
                    usuarioServices.actualizarUsuario();
                    usuarioServices.sincronizarUsuarios();
                    break;
                case 10:
                    for (Libro l : libroServices.listarLibros()){
                        System.out.println(l);
                    }
                    libroServices.actualizarLibro();
                    libroServices.sincronizarLibro();
                    break;
                case 11:
                    for (Ejemplar e : ejemplarServices.obtenerEjemplares()){
                        System.out.println(e);
                    }
                    ejemplarServices.actualizarEjemplar();
                    ejemplarServices.sincronizarEjemplares();
                    break;
                case 12:
                    for(Usuario u : usuarioServices.obtenerUsuarios()){
                        System.out.println(u);

                    }
                    usuarioServices.borrarUsuario();
                    usuarioServices.sincronizarUsuarios();
                    break;
                case 13:
                    for (Libro l : libroServices.listarLibros()){
                        System.out.println(l);
                    }
                    libroServices.borrarLibros();
                    libroServices.sincronizarLibro();
                    prestamoServices.sincronizarPrestamos();
                    ejemplarServices.sincronizarEjemplares();
                    break;
                case 14:
                    for (Ejemplar e : ejemplarServices.obtenerEjemplares()){
                        System.out.println(e);
                    }
                    ejemplarServices.borrarEjemplar();
                    ejemplarServices.sincronizarEjemplares();
                    break;
                case 15:
                    for (Prestamo p : prestamoServices.getPrestamos(usuario.getId())) {
                        System.out.println(p);
                    }
                    prestamoServices.devolverPrestamo();
                    prestamoServices.sincronizarPrestamos();
                    break;

                case 16:
                    for (Libro l : libroServices.listarLibros()) {
                        System.out.println(l);
                    }
                    libroServices.obtenerLibroPorISBN();
            }
        } while (opcion != 0);
    }

    public static void menuNormal(Usuario usuario) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("---------------GESTIÓN NORMAL-----------------");
            System.out.println("1. Ver mis prestamos.");
            System.out.println("0. Salir.");
            System.out.print("Selecciona una ocpión: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    for (Prestamo p : prestamoServices.getPrestamos(usuario.getId())) {
                        if (p.getUsuario().equals(usuario)){
                            System.out.println(p);
                        }
                    }
                case 0:
                    break;

            }
        } while (opcion != 0);

    }


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("-------------INICIO DE SESIÓN-------------");
            System.out.println("1. Iniciar sesión");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del usuario: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String contrasena = scanner.nextLine();
                    System.out.print("ID: ");
                    int id = scanner.nextInt();

                    for (Usuario u : usuarioServices.obtenerUsuarios()) {
                        if (u.getId() == id) {
                            if (u.getPassword().equals(contrasena) && u.getNombre().equals(nombre)) {
                                if (u.getTipo().equals("administrador")) {
                                    menuAdmin(u);
                                } else if (u.getTipo().equals("normal")) {
                                    menuNormal(u);
                                }
                            } else throw new Exception("El usuario no existe");
                            break;
                        }
                    }

            }
        } while (opcion != 0);

    }
}