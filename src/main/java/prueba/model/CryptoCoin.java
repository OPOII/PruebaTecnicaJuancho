package prueba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="CRYPTOCOIN")
public class CryptoCoin {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CLIENT_ID_GENERATOR", sequenceName="CLIENT_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CLIENT_ID_GENERATOR")
    @Column
    private Long id;
    @NotNull
    @NotBlank(message = "nombre is mandatory")
    @Column
    private String nombre;
    @NotNull
    @NotBlank(message = "symbol is mandatory")
    @Column
    private String symbol;
    @NotNull
    @NotBlank(message = "precio is mandatory")
    @Column
    private double precio;
    @NotNull
    @NotBlank(message = "rankingPosition is mandatory")
    @Column
    private int rankingPosition;

}
