package prueba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import prueba.model.CryptoCoin;
import prueba.model.User;
import prueba.repository.CryptoRepository;
import prueba.repository.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoRepository cryptoRepository;
    @Override
    @Transactional
    public User save(User user) {
        try {
           return userRepository.save(user);
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public void addCryptoCoin(CryptoCoin coin, Long id) {
        User actual=userRepository.findById(id).get();
        if(actual.getFavoriteCryptoCoin()==null){
            actual.setFavoriteCryptoCoin(coin);
            actual.getCurrentCoins().add(coin);
            userRepository.saveAndFlush(actual);
        }else{
            actual.getCurrentCoins().add(coin);
            userRepository.saveAndFlush(actual);
        }

    }

    @Override
    public List<CryptoCoin> getAllCryptos() {

        return null;
    }

    @Override
    public void setFavoriteCrypto(String name) {

    }
}
