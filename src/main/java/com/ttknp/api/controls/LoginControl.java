package com.ttknp.api.controls;

import com.ttknp.security.custom.configs.jwt.JwtService;
import com.ttknp.security.custom.entities.LoginModel;
import com.ttknp.security.custom.entities.LoginRequest;
import com.ttknp.security.custom.entities.LoginResponse;
import com.ttknp.security.custom.helpers.auth.UsefulAuthHelper;
import io.jsonwebtoken.JwtBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

// By default /api/** it's security
@RestController
@RequestMapping(value = "/register")
public class LoginControl {

    private final JwtService jwtService;
    private final ResourceLoader resourceLoader;
    private final Environment environment;
    @Autowired
    public LoginControl(JwtService jwtService, ResourceLoader resourceLoader, Environment environment) {
        this.jwtService = jwtService;
        this.resourceLoader = resourceLoader;
        this.environment = environment;
    }
    @PostConstruct
    public void init() {
        initPublicKey();
        // initSecretKey();
    }

    // For auth PS256
    public void initPublicKey() {
        PrivateKey privateKey;
        // Load the private key from the resources folder on application start
        // ****
        Resource resource = resourceLoader.getResource("classpath:ssl/private2048.pem");
        try {
            privateKey = UsefulAuthHelper.getPrivateKey(resource.getFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        jwtService.setPs256PrivateKey(privateKey);
    }

    // For auth HS256
    public void initSecretKey() {
        String secretKey = environment.getProperty("hs256jwt.secret.key");
        jwtService.setHS256secretKey(secretKey);
    }


    @PostMapping(value = "/login")
    private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        getModels().forEach((LoginModel loginModelTemp) -> {
            // find by username
            if (loginModelTemp.getUsername().equals(loginRequest.getUsername())) {
                if ( UsefulAuthHelper.validatePasswordStringWithPasswordBCrypt(loginRequest.getPassword(), loginModelTemp.getPassword())) { // check password string with password bcrypt from database
                    JwtBuilder jwtBuilder = jwtService.generateRS256Token(null, loginModelTemp); /// Generate token and set all details as claims,issue&expired token,... by LoginModel
                    loginResponse.setToken(jwtBuilder.compact());
                } else {
                    loginResponse.setToken(null);
                }
            }
        });
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginResponse);
    }


    private List<LoginModel> getModels() {
        List<LoginModel> models = new ArrayList<>();
        LoginModel loginModel3 = new LoginModel();
        LoginModel loginModel4 = new LoginModel();
        loginModel3.setUsername("test3");
        loginModel3.setEmail("test3@hotmail.com");
        loginModel3.setRole("admin"); // ** required work for hasAuthority(...)
        loginModel3.setCreateBy("ADMIN");
        loginModel3.setPassword(UsefulAuthHelper.convertStringToBCryptString("1"));
        loginModel4.setUsername("test4");
        loginModel4.setEmail("test4@hotmail.com");
        loginModel4.setRole("user"); // ** required work for hasAuthority(...)
        loginModel4.setCreateBy("ADMIN");
        loginModel4.setPassword(UsefulAuthHelper.convertStringToBCryptString("1"));
        models.add(loginModel3);
        models.add(loginModel4);
        return models;
    }


}
