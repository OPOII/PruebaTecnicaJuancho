package prueba.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import prueba.model.User;
import prueba.repository.CryptoRepository;
import prueba.repository.UserRepository;
import prueba.utility.TestConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
@Import({TestConfig.class})
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CryptoRepository cryptoRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    @BeforeEach
    void SetUp(){
        MockitoAnnotations.initMocks(this);
        user=new User();
        user.setId(Long.valueOf("1"));
        user.setNombre("Juancho");
        user.setApellido("te presta");
        user.setUsername("juanchito");
        user.setPassword("pruebatecnica");
        user.setUserCurrency("EURO");
        user.setExpireTokenDate(null);
        user.setTRMToLocalCurrency(1.5);
        user.setFavoriteCryptoCoin(null);
        user.setCurrentCoins(new ArrayList<>());
    }
    @Test
    void save() throws Exception {
        /*when(userRepository.save(any(User.class))).thenReturn(user);
        assertNotNull(userService.save(user));
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
        assertEquals(userService.existById(Long.valueOf("1")),true);*/
    }

    @Test
    void addCryptoCoin() {
    }

    @Test
    void updateCoinCurrency() {
    }

    @Test
    void getAllCryptos() {
    }

    @Test
    void searchByUsername() {
    }

    @Test
    void setFavoriteCrypto() {
    }

    @Test
    void getUserByUsername() {
    }

    @Test
    void getFavoriteCryptoCoin() {
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void saveAuthTokenToUser() {
    }

    @Test
    void getAccessToken() {
    }

    @Test
    void searchSymbolCoin() {
    }
}