package prueba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="USER")
public class User {
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
    @NotBlank(message = "apellido is mandatory")
    @Column
    private String apellido;
    @NotNull
    @NotBlank(message = "username is mandatory")
    @Column
    private String username;
    @NotNull
    @NotBlank(message = "password is mandatory")
    @Size(min=8,message="{validation.name.size.too_short}")
    @Column
    private String password;
    @NotNull
    @NotBlank(message = "currency is mandatory is mandatory")
    @Column
    private String userCurrency;
    @NotNull
    @Column
    private double TRMToLocalCurrency;

    private CryptoCoin favoriteCryptoCoin;

    @OneToMany(targetEntity=User.class,mappedBy="user")
    @JsonIgnore
    private List<CryptoCoin>currentCoins;

    public User(){
        this.currentCoins = new ArrayList<CryptoCoin>();
        favoriteCryptoCoin=null;
    }

}
