package Validaciones;

import DTOS.Ejemplar;
import DTOS.Prestamo;
import DTOS.Usuario;

import java.time.LocalDate;
import java.util.List;

public class Validaciones {


    public Validaciones() {
    }


    //Validación del ISBN
    public boolean validarIsbn(String isbn) {
        if (isbn == null || isbn.length() != 13 || !isbn.matches("\\d{13}")) {
            return false;
        }
        int sum = 0;

        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int digitoDeControl = 10 - (sum % 10);
        if (digitoDeControl == 10) {
            digitoDeControl = 0;
        }

        return digitoDeControl == Character.getNumericValue(isbn.charAt(12));
    }

    //Validación del DNI
    public boolean validarDni(String dni) {
        String[] letras = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};

        if (dni.length() != 9) {
            return false;
        } else {
            //Validacion del NIE
            if (dni.substring(0, 1).equals("X")) {
                dni = "0" + dni.substring(1);
            } else if (dni.substring(0, 1).equals("Y")) {
                dni = "1" + dni.substring(1);
            } else if (dni.substring(0, 1).equals("Z")) {
                dni = "2" + dni.substring(1);
            }
        }
        int numeros = Integer.parseInt(dni.substring(0, 8));
        if (letras[numeros % 23].equals(dni.substring(8))) {
            return true;
        } else {
            return false;
        }
    }


    //Validación del Correo Electrónico
    public boolean validarCorreo(String correo) {
        if (correo.contains("@")) {
            return true;
        } else {
            return false;
        }
    }

    //Validacion del Tipo de Usuario

    public boolean validarTipo(String tipo) {
        if (tipo.equals("Normal") || tipo.equals("Administrador")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validarEstado(String estado) {
        if (estado.equals("Disponible") || estado.equals("Prestado") || estado.equals("Dañado")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean prestamosActivos(Usuario usuario, List<Prestamo> prestamos) {
        int contador = 0;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getUsuario().equals(usuario) && prestamo.getFechaDevolucion() == null) {
                contador++;
            }
        }
        if (contador < 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean estadoEjemplar(Ejemplar ejemplar) {
        if (ejemplar.getEstado().equals("Disponible")){
            return true;
        } else
            return false;
    }


    public boolean penalizacionesActivas(Usuario usuario){
        if (usuario.getPenalizacionHasta() == null){
            return true;
        } else if (usuario.getPenalizacionHasta().isBefore(LocalDate.now())){
            return true;
        } else return false;
    }
}
