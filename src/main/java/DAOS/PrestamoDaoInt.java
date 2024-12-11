package DAOS;

import DTOS.Prestamo;

import java.util.List;

public interface PrestamoDaoInt {
    void insertPrestamo(Prestamo prestamo);
    void updatePrestamo(Prestamo prestamo);
    void devolverPrestamo(Prestamo prestamo);
    void deletePrestamo(int id);
    Prestamo getPrestamoById(int id);
    List<Prestamo> getAllPrestamos();
}
