package prueba.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import prueba.customHandler.ResponseHandler;
import prueba.model.JWTRequest;
import prueba.model.JWTResponse;
import prueba.model.User;
import prueba.service.UserServiceImpl;
import prueba.utility.JWTUtility;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private AuthenticationManager authenticationManager;

    private String token;
    @PostMapping("/authenticate")
    public ResponseEntity<Object> authentication(@RequestBody JWTRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        }catch(BadCredentialsException e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST,null);
        }
        final UserDetails userDetails=service.loadUserByUsername(request.getUsername());
        final String token=jwtUtility.generateToken(userDetails);
        if(token!=null && token!=""){
            service.saveAuthTokenToUser(request.getUsername(),token);
        }
        JWTResponse response=new JWTResponse(token);
        if(token!=null && token!=""){
            this.token=token;
        }
        return ResponseHandler.generateResponse("The login was succesfull", HttpStatus.OK,response);
    }
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User usuario) throws Exception {
        try{
            return ResponseHandler.generateResponse("The user was saved succesfull", HttpStatus.OK,service.save(usuario));
        }catch(Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST,null);
        }
    }

    @PostMapping("/addCoin")
    public ResponseEntity<Object> addCoin(@RequestParam String coinSymbol) throws Exception {
        try{
            String userName = jwtUtility.getUsernameFromToken(this.token);
            return ResponseHandler.generateResponse("The coin was saved succesfull", HttpStatus.OK,service.addCryptoCoin(coinSymbol,userName));
        }catch(Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST,null);

        }
    }
    @PutMapping("/setFavoriteCoin")
    public ResponseEntity<Object> setFavoriteCoin(@RequestParam String coinSymbol) throws Exception {
        try {
            String username=jwtUtility.getUsernameFromToken(this.token);
            return ResponseHandler.generateResponse("The coin was saved succesfull", HttpStatus.OK,service.addCryptoCoin(coinSymbol,username));
        }catch(Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST,null);
        }

    }

    @GetMapping("/getByUsername")
    public ResponseEntity<Object> getByUsername(@RequestParam String username)throws Exception {
        try {
            User searched=service.getUserByUsername(username);
            return ResponseHandler.generateResponse("The user was founded",HttpStatus.OK,searched);
        }catch(Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }
    @GetMapping("/listCoins")
    public ResponseEntity<Object>getCoinList(@Parameter(required = false,description="type 0 for ascending, 1 for descending, empty field for descending ") Integer orderCriteria)throws Exception{
        if(token!=null && token!=""){
            String username=jwtUtility.getUsernameFromToken(token);
            try{
                if(orderCriteria==null){
                    orderCriteria=1;
                }
                return ResponseHandler.generateResponse("The list was successfull",HttpStatus.OK,service.getAllCryptos(username,orderCriteria));
            }catch(Exception e){
                return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST,null);
            }
        }else{
            return ResponseHandler.generateResponse("The user isn't authenticated",HttpStatus.FORBIDDEN,null);
        }

    }
}
