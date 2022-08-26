package prueba.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prueba.model.CryptoCoin;
import prueba.model.User;
import prueba.repository.CryptoRepository;
import prueba.repository.UserRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoRepository cryptoRepository;
    @Autowired
    private  PasswordEncoder encoder;

    @Override

    public User save(User user) throws Exception {
        User searched=this.getUserByUsername(user.getUsername());
        if(searched!=null){
            throw new Exception("The username is already taken by other user");
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            String accessToken=this.getAccessToken();
            Date expireTokenDay=new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(expireTokenDay);
            c.add(Calendar.DATE, 1);
            expireTokenDay = c.getTime();
            System.out.println(expireTokenDay);
            user.setExpireTokenDate(expireTokenDay);
            user.setApiToken(accessToken);
            return userRepository.save(user);
        }
    }
    @Override
    public CryptoCoin addCryptoCoin(String symbol,String username) throws Exception {
        User userSession=userRepository.findByUsername(username);
        if(userSession!=null){
            Date today=new Date();
            Date expireTokenDay=userSession.getExpireTokenDate();
            if(today.after(expireTokenDay)){
                String getToken=this.getAccessToken();
                userSession.setApiToken(getToken);
            }
            try {
                CryptoCoin searchCoin=this.searchSymbolCoin(symbol,userSession.getApiToken());
                //Price in dollar
                if(!userSession.getUserCurrency().equalsIgnoreCase("USD")){
                    double userTRM=userSession.getTRMToLocalCurrency();
                   this.updateCoinCurrency(searchCoin,userTRM);
                }
                if(userSession.getFavoriteCryptoCoin()==null){
                    userSession.setFavoriteCryptoCoin(searchCoin);
                    userSession.getCurrentCoins().add(searchCoin);
                    userRepository.saveAndFlush(userSession);
                }else{
                    userSession.getCurrentCoins().add(searchCoin);
                    userRepository.saveAndFlush(userSession);
                }
                return searchCoin;
            }catch(Exception e){
                throw new Exception(e);
            }
        }else{
            return null;
        }
    }
    public void updateCoinCurrency(CryptoCoin coin,double userCurrency){
        double currency=coin.getPrecio()*userCurrency;
        coin.setPrecio(currency);
        cryptoRepository.saveAndFlush(coin);
    }


    @Override
    public List<CryptoCoin> getAllCryptos(String username, int orderCriteria)throws Exception {
        User user=this.searchByUsername(username);
        if(user!=null){
            List<CryptoCoin>list=user.getCurrentCoins();
            if(list.size()>2){
                if(orderCriteria==0){
                   List<CryptoCoin> sorted=(ArrayList<CryptoCoin>)list.stream().sorted(Comparator.comparing(CryptoCoin::getPrecio)).collect(Collectors.toList());
                   return sorted;
                }else if(orderCriteria==1){
                    List<CryptoCoin> sorteds=(ArrayList<CryptoCoin>)list.stream().sorted(Comparator.comparing(CryptoCoin::getPrecio)).collect(Collectors.toList());
                    Collections.reverse(sorteds);
                    return sorteds;
                }
            }
            return user.getCurrentCoins();
        }else{
            throw new UsernameNotFoundException("El usuario que intentaba buscar no se encuentra");
        }
    }
    public User searchByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public CryptoCoin setFavoriteCrypto(String name,String username) {
        CryptoCoin search=cryptoRepository.findBySymbol(name);
        if(search!=null){
           User user= userRepository.findByUsername(username);
           user.setFavoriteCryptoCoin(search);
           userRepository.saveAndFlush(user);
           return search;
        }else{
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username)throws Exception {
        User search=userRepository.findByUsername(username);
        if(search!=null){
            return search;
        }else{
            return null;
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username!=null && username!=""){
            User founded=userRepository.findByUsername(username);
            return new org.springframework.security.core.userdetails.User(founded.getUsername(),founded.getPassword(),new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("USER_NOT_FOUND");
        }

    }



    public void saveAuthTokenToUser(String username, String token){
       User user=userRepository.findByUsername(username);
       user.setAuthToken(token);
       userRepository.saveAndFlush(user);
    }
    public String getAccessToken() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://bravenewcoin.p.rapidapi.com/oauth/token"))
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", "79192b3294msh7a6f314e68f9525p12e899jsn6fae355fe8bb")
                .header("X-RapidAPI-Host", "bravenewcoin.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\r\"audience\": \"https://api.bravenewcoin.com\",\r\"client_id\": \"oCdQoZoI96ERE9HY3sQ7JmbACfBf55RY\",\r\"grant_type\": \"client_credentials\"\r}")).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject data=new Gson().fromJson(response.body(),JsonObject.class);
        String token=data.get("access_token").toString();
        return token;
    }
    public CryptoCoin searchSymbolCoin(String symbol,String accessToken) throws Exception {
        CryptoCoin searched=cryptoRepository.findBySymbol(symbol);
        if(searched!=null){
            return searched;
        }else{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://bravenewcoin.p.rapidapi.com/asset?symbol="+symbol+"&status=ACTIVE"))
                    .header("X-RapidAPI-Key", "79192b3294msh7a6f314e68f9525p12e899jsn6fae355fe8bb")
                    .header("X-RapidAPI-Host", "bravenewcoin.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            if(response.statusCode()==200){
                JsonObject data=new Gson().fromJson(response.body(),JsonObject.class);
                JsonArray content=data.get("content").getAsJsonArray();
                JsonObject object=content.get(0).getAsJsonObject();
                String cryptoID=object.get("id").getAsString();
                System.out.println(cryptoID);
                System.out.println(accessToken);
                //Obtain the coin information
                HttpRequest request2 = HttpRequest.newBuilder()
                        .uri(URI.create("https://bravenewcoin.p.rapidapi.com/market-cap?assetId="+cryptoID))
                        .header("Authorization", "Bearer "+accessToken.substring(1,accessToken.length() -1))
                        .header("X-RapidAPI-Key", "79192b3294msh7a6f314e68f9525p12e899jsn6fae355fe8bb")
                        .header("X-RapidAPI-Host", "bravenewcoin.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> response2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());

                JsonObject data2=new Gson().fromJson(response2.body(),JsonObject.class);

                JsonArray content2=data2.get("content").getAsJsonArray();

                JsonObject object2=content2.get(0).getAsJsonObject();


                String coinName=object.get("name").getAsString();
                String symbols=object.get("symbol").getAsString();
                double precio=object2.get("price").getAsDouble();
                int marketCapRank=object2.get("marketCapRank").getAsInt();
                searched=new CryptoCoin();
                searched.setNombre(coinName);
                searched.setSymbol(symbols);
                searched.setPrecio(precio);
                searched.setRankingPosition(marketCapRank);
                return cryptoRepository.save(searched);

            }else{
                throw new Exception("El simbolo de la criptomoneda que busca no existe");
            }
        }
    }

}
