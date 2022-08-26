package prueba.service;


import prueba.model.CryptoCoin;
import prueba.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface UserService {

     User save(User user) throws Exception;
     CryptoCoin addCryptoCoin(String symbol,String username) throws Exception;
     HashMap<String,List<CryptoCoin>> getAllCryptos(String username, int orderCriteria) throws Exception;
     CryptoCoin setFavoriteCrypto(String name,String username)throws Exception;

     User getUserByUsername(String username)throws Exception;
     CryptoCoin getFavoriteCryptoCoin(String username)throws Exception;
}
