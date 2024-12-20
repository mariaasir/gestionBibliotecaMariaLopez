package DAOS;

import DTOS.Ejemplar;

import java.util.List;

/**
 * @author María López Patón 2ºDAM
 */

public interface EjemplarDaoInt {
    void insertEjemplar(Ejemplar ejemplar);
    Ejemplar updateEjemplar(Ejemplar ejemplar);
    int stockDisponible(Ejemplar ejemplar);
    void deleteEjemplar(int id);
    Ejemplar getEjemplar(int id);
    List<Ejemplar> findAllEjemplar();
}
