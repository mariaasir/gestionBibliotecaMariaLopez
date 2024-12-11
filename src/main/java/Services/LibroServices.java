package Services;

import DAOS.EjemplarDaoInt;
import DAOS.LibroDaoInt;
import DAOS.PrestamoDaoInt;
import DTOS.Ejemplar;
import DTOS.Libro;
import DTOS.Prestamo;
import Validaciones.Validaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibroServices {
    //Inicializacion de los DAOS y la  lista que guardará los libros en memoria.
    private LibroDaoInt libroDAO;
    private EjemplarDaoInt ejemplarDAO;
    private PrestamoServices prestamoServices;
    private List<Libro> librosEnMemoria = new ArrayList<>();
    Validaciones validar = new Validaciones();
    Scanner scanner = new Scanner(System.in);

    //Constructor
    public LibroServices(LibroDaoInt libroDAO, EjemplarDaoInt ejemplarDAO, PrestamoServices prestamoServices) {
        this.libroDAO = libroDAO;
        this.ejemplarDAO = ejemplarDAO;
        this.prestamoServices = prestamoServices;
        sincronizarLibro();
    }


    //Metodo para insertar un libro
    public void insertarLibro() throws Exception {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Titulo: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        Libro libro = new Libro(isbn, titulo, autor);  //Crea un nuevo libro


        //Valida que el ISBN sea correcto
        if (validar.validarIsbn(isbn)) {
            libroDAO.insertLibro(libro);       //Si es correcto, lo inserta
            sincronizarLibro(); //Sincroniza y guarda los datos en memoria
        } else {
            throw new Exception("El ISBN no es válido");    //Si no es correcto, lanzará una excepción
        }
    }


    //Metodo para actualizar un libro.
    public void actualizarLibro() throws Exception {
        System.out.print("Introduce el ISBN del libro que deseas actualizar: ");
        String isbn = scanner.nextLine();
        Libro libro = libroDAO.getLibroByISBN(isbn);        //Busca el libro por su ISBN
        System.out.print("Titulo: ");
        libro.setTitulo(scanner.nextLine());
        System.out.print("Autor: ");
        libro.setAutor(scanner.nextLine());
        if (libro != null) {
            libroDAO.updateLibro(libro);        //Si encuentra el libro, lo actualiza
            sincronizarLibro();     //Sincroniza los datos en memoria
        } else {
            throw new Exception("No se encuentra el libro");    //Si no lo encuentra, lanza una excepcion
        }

    }


    //Metodo para borrar un libro
    public void borrarLibros() {
        System.out.print("ISBN del libro a eliminar: ");
        String isbn = scanner.nextLine();
        List<Ejemplar> ejemplares = ejemplarDAO.findAllEjemplar();   //Busca los ejemplares que tienen ese libro.
        prestamoServices.eliminarPrestamosPorLibro(isbn);            //Elimina los prestamos que tengan ese libro
        libroDAO.deleteLibro(isbn);                                     //Elimina el libro
        for (Ejemplar ejemplar : ejemplares) {
            if (ejemplar.getIsbn().equals(isbn)) {
                ejemplarDAO.deleteEjemplar(ejemplar.getId());           //Elimina los ejemplares que tienen ese libro
            }
        }
        sincronizarLibro();     //Sincroniza en memoria
    }


    //Metodo para contar el Stock de un libro
    public void obtenerLibroPorISBN() {
        System.out.println("Introduzca el ISBN del libro a comprobar el Stock: ");
        String isbn = scanner.nextLine();
        int cont = 0;
        for (Ejemplar e : libroDAO.getLibroByISBN(isbn).getEjemplars()) {       //Recorre todos los ejemplares a partir del ISBN introducido
            if (e.getEstado().equals("Disponible")) {       //Comprueba que el estado del ejemplar sea Disponible
                System.out.println(e);      //Lo muestra por pantalla
                cont++;         //Suma el contador
            }
        }
        System.out.println("Stock: " + cont);       //Imprime el contador
    }


    //Metodo para listar todos los  libros
    public List<Libro> listarLibros() {
        return librosEnMemoria;
    }


    //Metodo para sincronizar los libros en memoria
    public void sincronizarLibro() {
        librosEnMemoria = libroDAO.getAllLibros();
    }
}
