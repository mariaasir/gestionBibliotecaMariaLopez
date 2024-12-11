package Services;

import DAOS.EjemplarDAO;
import DAOS.PrestamoDaoInt;
import DAOS.UsuarioDAO;
import DTOS.Ejemplar;
import DTOS.Libro;
import DTOS.Prestamo;
import DTOS.Usuario;
import Validaciones.Validaciones;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PrestamoServices {

    private PrestamoDaoInt prestamoDAO;
    private List<Prestamo> prestamosEnMemoria = new ArrayList<>();
    private UsuarioDAO usuarioDAO;
    private EjemplarDAO ejemplarDAO;
    private Validaciones validar;
    Scanner scanner = new Scanner(System.in);

    public PrestamoServices(PrestamoDaoInt prestamoDAO, UsuarioDAO usuarioDAO, EjemplarDAO ejemplarDAO, Validaciones validar) {
        this.prestamoDAO = prestamoDAO;
        this.usuarioDAO = usuarioDAO;
        this.ejemplarDAO = ejemplarDAO;
        this.validar = validar;
        sincronizarPrestamos();
    }

    public void insertarPrestamos() throws Exception {
        System.out.print("Introduce el ID del usuario: ");
        int idUsuario = scanner.nextInt();
        System.out.print("Introduce el ID del Ejemplar: ");
        int idEjemplar = scanner.nextInt();
        Usuario usuario = usuarioDAO.getUsuario(idUsuario);
        Ejemplar ejemplar = ejemplarDAO.getEjemplar(idEjemplar);
        Prestamo prestamo = new Prestamo(usuario, ejemplar);
        if (validar.estadoEjemplar(ejemplar)) {
            if (validar.prestamosActivos(usuario, prestamoDAO.getAllPrestamos())) {
                if (validar.penalizacionesActivas(usuario)) {
                    prestamoDAO.insertPrestamo(prestamo);
                } else {
                    throw new Exception("El usuario esta penalizado. No podrá realizar un nuevo prestamo hasta el " + usuario.getPenalizacionHasta());
                }
            } else {
                throw new Exception("Ya tiene 3 prestamos activos. No pueden añadirse más prestamos a este usuario. Si deseas coger prestado un nuevo libro, devuelve alguno de los que ya tienes.");
            }
        } else {
            throw new Exception("El ejemplar no esta disponible para ser prestado.");
        }


        ejemplar.setEstado("Prestado");
        ejemplarDAO.updateEjemplar(ejemplar);

        sincronizarPrestamos();
    }

    public void actualizarPrestamos(Prestamo prestamo) {
        prestamoDAO.updatePrestamo(prestamo);
        sincronizarPrestamos();
    }


    public void devolverPrestamo() {
        System.out.println("Introduce el ID del prestamo que deseas devolver:  ");
        int idPrestamo = scanner.nextInt();
        Prestamo prestamo = prestamoDAO.getPrestamoById(idPrestamo);
        prestamo.getEjemplar().setEstado("Disponible");
        ejemplarDAO.updateEjemplar(prestamo.getEjemplar());
        prestamo.setFechaDevolucion(LocalDate.now());
        prestamoDAO.updatePrestamo(prestamo);

        if (prestamo.getFechaInicio().plusDays(15).isBefore(prestamo.getFechaDevolucion())) {
            LocalDate penalizado = prestamo.getUsuario().getPenalizacionHasta();
            if (penalizado == null) {
                prestamo.getUsuario().setPenalizacionHasta(LocalDate.now().plusDays(15));
            } else {
                prestamo.getUsuario().setPenalizacionHasta(penalizado.plusDays(15));
            }
            usuarioDAO.updateUsuario(prestamo.getUsuario());
        }

        sincronizarPrestamos();
    }

    public void deletePrestamo(int id) {
        prestamoDAO.deletePrestamo(id);
        sincronizarPrestamos();
    }

    public void eliminarPrestamosPorLibro(String isbn) {
        for (Prestamo prestamo : prestamoDAO.getAllPrestamos()) {
            if (prestamo.getEjemplar().getIsbn().getIsbn().equals(isbn)) {
                prestamoDAO.deletePrestamo(prestamo.getId());
            }
        }
    }

    public List<Prestamo> getPrestamos(int id) {
        return prestamosEnMemoria;
    }

    public void sincronizarPrestamos() {
        prestamosEnMemoria = prestamoDAO.getAllPrestamos();
    }
}
