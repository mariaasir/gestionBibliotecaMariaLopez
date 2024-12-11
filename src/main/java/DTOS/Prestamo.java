package DTOS;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

/**
 * @author María López Patón 2ºDAM
 */


@Entity
@Table(name = "prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "usuario_id", nullable = false)
    private DTOS.Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ejemplar_id", nullable = false)
    private Ejemplar ejemplar;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaDevolucion")


    private LocalDate fechaDevolucion;

    //Constructores
    public Prestamo() {
    }

    public Prestamo(Usuario usuario, Ejemplar ejemplar) {
        this.usuario = usuario;
        this.ejemplar = ejemplar;
        this.fechaInicio = LocalDate.now();
        this.fechaDevolucion = null;
    }


    //Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DTOS.Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(DTOS.Usuario usuario) {
        this.usuario = usuario;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }


    //To String
    @Override
    public String toString() {
        return id + " " +
                ", " + usuario.getId() + "(" + usuario.getNombre() + ")" +
                ", " + ejemplar.getId() + "(" + ejemplar.getIsbn().getTitulo() + ")" +
                ", " + fechaInicio +
                ", " + fechaDevolucion;
    }
}