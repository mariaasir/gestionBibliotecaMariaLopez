package Services;

import DAOS.EjemplarDAO;
import DAOS.PrestamoDaoInt;
import DAOS.UsuarioDAO;
import DTOS.Ejemplar;
import DTOS.Prestamo;
import DTOS.Usuario;
import Validaciones.Validaciones;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author María López Patón 2ºDAM
 */


public class PrestamoServices {

    //Inicializacion de los DAOS y la  lista que guardará los ejemplares en memoria.
    private PrestamoDaoInt prestamoDAO;
    private List<Prestamo> prestamosEnMemoria = new ArrayList<>();
    private UsuarioDAO usuarioDAO;
    private EjemplarDAO ejemplarDAO;
    private Validaciones validar;
    Scanner scanner = new Scanner(System.in);


    //Constructor
    public PrestamoServices(PrestamoDaoInt prestamoDAO, UsuarioDAO usuarioDAO, EjemplarDAO ejemplarDAO, Validaciones validar) {
        this.prestamoDAO = prestamoDAO;
        this.usuarioDAO = usuarioDAO;
        this.ejemplarDAO = ejemplarDAO;
        this.validar = validar;
        sincronizarPrestamos();
    }


    //Metodo para insertar un prestamo
    public void insertarPrestamos() throws Exception {
        System.out.print("Introduce el ID del usuario: ");
        int idUsuario = scanner.nextInt();
        System.out.print("Introduce el ID del Ejemplar: ");
        int idEjemplar = scanner.nextInt();
        Usuario usuario = usuarioDAO.getUsuario(idUsuario);     //Busca el usuario a partir de su ID
        Ejemplar ejemplar = ejemplarDAO.getEjemplar(idEjemplar);    //Busca el ejemplar a partir de su ID
        Prestamo prestamo = new Prestamo(usuario, ejemplar);        //Crea un nuevo prestamo
        if (validar.estadoEjemplar(ejemplar)) {     //Valida el estado del ejemplar. Si no esta disponible, no dejará crear el prestamo
            if (validar.prestamosActivos(usuario, prestamoDAO.getAllPrestamos())) {         //Comprueba cuantos prestamos activos tiene el usuario. Si tiene más de 3 no dejará crear un usuario nuevo
                if (validar.penalizacionesActivas(usuario)) {       //Valida las penalizaciones activas. Si el usuario esta penalizado, no podrá realizarse el prestamo
                    prestamoDAO.insertPrestamo(prestamo);       //Si cumple todas las validaciones, se podrá crear el prestamo
                } else {
                    throw new Exception("El usuario esta penalizado. No podrá realizar un nuevo prestamo hasta el " + usuario.getPenalizacionHasta());
                }
            } else {
                throw new Exception("Ya tiene 3 prestamos activos. No pueden añadirse más prestamos a este usuario. Si deseas coger prestado un nuevo libro, devuelve alguno de los que ya tienes.");
            }
        } else {
            throw new Exception("El ejemplar no esta disponible para ser prestado.");
        }

        ejemplar.setEstado("Prestado");     //Cambia el estado del ejemplar a prestado
        ejemplarDAO.updateEjemplar(ejemplar);       //Actualiza el ejemplar para que se actualice su estado

        sincronizarPrestamos();
    }




    //Metodo para devolver un prestamo
    public void devolverPrestamo() {
        System.out.println("Introduce el ID del prestamo que deseas devolver:  ");  //Pregunta por el ID del prestamo que se desea devolver.
        int idPrestamo = scanner.nextInt();
        Prestamo prestamo = prestamoDAO.getPrestamoById(idPrestamo);            //Crea un prestamo buscando el prestamo por ID
        prestamo.getEjemplar().setEstado("Disponible");         //Cambia el estado del ejemplar a Disponible
        ejemplarDAO.updateEjemplar(prestamo.getEjemplar());     //Actualiza el ejemplar
        prestamo.setFechaDevolucion(LocalDate.now());           //Establece la fecha de devolución al día de hoy
        prestamoDAO.updatePrestamo(prestamo);                   //Actualiza el prestamo


        //Si la fecha de devolucion no ha superado 15 dias a la fecha de inicio, no tendrá penalización
        //Si la fecha de devolucion ha superado los 15 dias hará lo siguiente.
        if (prestamo.getFechaInicio().plusDays(15).isBefore(prestamo.getFechaDevolucion())) {
            LocalDate penalizado = prestamo.getUsuario().getPenalizacionHasta();    //Buscará si ya tiene penalizaciones
            if (penalizado == null) {           //Si no tiene, añadira a la fecha de hoy 15 días de penalización
                prestamo.getUsuario().setPenalizacionHasta(LocalDate.now().plusDays(15));
            } else {        //Si ya tiene fecha de penalización, le sumará 15 días más.
                prestamo.getUsuario().setPenalizacionHasta(penalizado.plusDays(15));
            }
            usuarioDAO.updateUsuario(prestamo.getUsuario());        //Se actualiza el usuario
        }

        sincronizarPrestamos();     //Sincroniza los prestamos
    }

    public void deletePrestamo(int id) {
        prestamoDAO.deletePrestamo(id);
        sincronizarPrestamos();
    }


    //Elimina los prestamos a partir del ISBN de un libro
    public void eliminarPrestamosPorLibro(String isbn) {
        for (Prestamo prestamo : prestamoDAO.getAllPrestamos()) {       //Recorre todos los prestamos
            if (prestamo.getEjemplar().getIsbn().getIsbn().equals(isbn)) {      //Busca el prestamo que contenga ese ISBN
                prestamoDAO.deletePrestamo(prestamo.getId());           //Elimina el prestamoencontrado
            }
        }
    }


    //Lista todos los prestamos
    public List<Prestamo> getPrestamos(int id) {
        return prestamosEnMemoria;
    }


    //Sincroniza los prestamos en memoria
    public void sincronizarPrestamos() {
        prestamosEnMemoria = prestamoDAO.getAllPrestamos();
    }
}
