package prueba.service;


import prueba.model.CryptoCoin;
import prueba.model.User;

import java.util.List;

public interface UserService {

    public User save(User user);
    public void addCryptoCoin(CryptoCoin coin, Long id);
    public List<CryptoCoin> getAllCryptos();
    public void setFavoriteCrypto(String name);
}
