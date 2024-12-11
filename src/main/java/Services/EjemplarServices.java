package Services;

import DAOS.EjemplarDaoInt;
import DAOS.LibroDAO;
import DTOS.Ejemplar;
import DTOS.Libro;
import Validaciones.Validaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author María López Patón 2ºDAM
 */


public class EjemplarServices {

    //Inicializacion de los DAOS y la  lista que guardará los ejemplares en memoria.
    private EjemplarDaoInt ejemplarDao;
    private LibroDAO libroDao;
    private List<Ejemplar> ejemplaresEnMemoria = new ArrayList<Ejemplar>();
    Validaciones validar = new Validaciones();
    Scanner scanner = new Scanner(System.in);

    //Constructor
    public EjemplarServices(EjemplarDaoInt ejemplarDao, LibroDAO libroDao) {
        this.ejemplarDao = ejemplarDao;
        this.libroDao = libroDao;
        sincronizarEjemplares();
    }


    //Metodo para insertar Ejemplar
    public void insertarEjemplar() throws Exception {
        System.out.print("Introduce el ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Introduce el estado: ");
        String estado = scanner.nextLine();
        Libro libro = libroDao.getLibroByISBN(isbn);            //Busca el libro que tenga ese ISBN
        Ejemplar ejemplar = new Ejemplar(libro, estado);        //Crea un nuevo ejemplar con el libro de ese ISBN y el estado introducido


        //Comprueba que el ISBN y el estado sean correctos
        if (validar.validarIsbn(isbn)) {
            if (validar.validarEstado(estado)) {
                ejemplarDao.insertEjemplar(ejemplar); //Si todo es correcto, inserta el ejemplar
                sincronizarEjemplares(); //Sincroniza los ejemplares en memoria.
            } else {
                throw new Exception("El estado no es válido. Solo puede ser Disponible / Dañado / Prestado");
            }
        } else {
            throw new Exception("El ISBN no es válido");
        }


    }


    //Metodo para actualizar un ejemplar
    public void actualizarEjemplar() throws Exception {
        System.out.print("Introduce el ID del ejemplar que deseas actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Ejemplar ejemplar = ejemplarDao.getEjemplar(id);        //Crea un nuevo ejemplar buscando el ejemplar en memoria por su ID.
        System.out.print("Introduce el estado: ");
        ejemplar.setEstado(scanner.nextLine());
        if (validar.validarEstado(ejemplar.getEstado())) {  //Compureba que el estado es correcto
            ejemplarDao.updateEjemplar(ejemplar);       //Lo actualiza
            sincronizarEjemplares();            //Sincroniza de nuevo los ejemplares
        } else {
            throw new Exception("El estado no es válido. Solo puede ser Disponible / Dañado / Prestado"); //Lanza una excepcion si el estado no es correcto.
        }

    }


    //Metodo para borrar el ejemplar
    public void borrarEjemplar() {
        System.out.print("Introduce el ID del Ejemplar a eliminar: ");
        int id = scanner.nextInt();
        ejemplarDao.deleteEjemplar(id);
        sincronizarEjemplares();
    }


    //Metodo para obtener todos los ejemplares
    public List<Ejemplar> obtenerEjemplares() {
        return ejemplaresEnMemoria;
    }


    //Metodo para sincronizar en memoria
    public void sincronizarEjemplares() {
        ejemplaresEnMemoria = ejemplarDao.findAllEjemplar();
    }
}
