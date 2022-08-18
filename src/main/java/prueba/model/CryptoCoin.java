package prueba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name="CRYPTOCOIN")
public class CryptoCoin {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CLIENT_ID_GENERATOR", sequenceName="CLIENT_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CLIENT_ID_GENERATOR")
    @Column
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @NotNull
    @NotBlank(message = "nombre is mandatory")
    @Column
    private String nombre;
    @NotNull
    @NotBlank(message = "precio is mandatory")
    @Column
    private double precio;
    @NotNull
    @NotBlank(message = "rankingPosition is mandatory")
    @Column
    private int rankingPosition;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public CryptoCoin(){

    }
}
