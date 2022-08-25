package prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import prueba.customHandler.ResponseHandler;
import prueba.model.JWTRequest;
import prueba.model.JWTResponse;
import prueba.model.User;
import prueba.service.UserServiceImpl;
import prueba.utility.JWTUtility;

import javax.servlet.http.HttpServletRequest;

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
            return ResponseHandler.generateResponse("The login was succesfull", HttpStatus.OK,null);
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
        return ResponseHandler.generateResponse("The user was saved succesfull", HttpStatus.OK,service.save(usuario));
    }

    @PostMapping("/addCoin")
    public ResponseEntity<Object> addCoin(@RequestParam String coinSymbol) throws Exception {
       // String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        String userName = null;
        token = this.token;
        userName = jwtUtility.getUsernameFromToken(token);
        System.out.println("----------------------- COIN---------------");
        System.out.println(userName);
        return ResponseHandler.generateResponse("The coin was saved succesfull", HttpStatus.OK,service.addCryptoCoin(coinSymbol,userName));
    }
    @PutMapping("/setFavoriteCoin")
    public ResponseEntity<Object> setFavoriteCoin(@CurrentSecurityContext(expression="authentication?.name")String username,@RequestParam String coinSymbol) throws Exception {
        return ResponseHandler.generateResponse("The coin was saved succesfull", HttpStatus.OK,service.addCryptoCoin(coinSymbol,username));
    }

    @GetMapping("/getByUsername")
    public ResponseEntity<Object> getByUsername(@RequestParam String username)throws Exception {
        User searched=service.getUserByUsername(username);
        if(searched!=null){
            return ResponseHandler.generateResponse("The user was founded",HttpStatus.OK,searched);
        }else{
            return ResponseHandler.generateResponse("The user doesnt exist",HttpStatus.NOT_FOUND,null);

        }
    }
}
