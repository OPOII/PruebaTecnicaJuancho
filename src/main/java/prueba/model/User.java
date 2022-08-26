package prueba.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="USUARIO")
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    private String password;
    @NotNull
    @NotBlank(message = "currency is mandatory is mandatory")
    @Column
    private String userCurrency;
    @NotNull(message="currency value cant be null")
    @Column
    @NotEmpty(message = "currency value cant be empty")
    private double TRMToLocalCurrency;
    @Column
    @JsonIgnore
    private Date expireTokenDate;
    @Column(length = 1024)
    @JsonIgnore
    private String authToken;
    @Column(length = 1024)
    @JsonIgnore
    private String apiToken;
    @OneToOne
    @JsonIgnore
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private CryptoCoin favoriteCryptoCoin=null;

    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<CryptoCoin>currentCoins;


}
