package com.ttknp.api.controls;

import com.ttknp.security.custom.configs.jwt.JwtService;
import com.ttknp.security.custom.entities.LoginModel;
import com.ttknp.security.custom.entities.LoginRequest;
import com.ttknp.security.custom.entities.LoginResponse;
import com.ttknp.security.custom.helpers.auth.UsefulAuthHelper;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

// By default /api/** it's security
@RestController
@RequestMapping(value = "/register")
public class LoginControl {

    private final JwtService jwtService;

    @Autowired
    public LoginControl(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login")
    private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        getModels().forEach((LoginModel loginModelTemp) -> {
            // find by username
            if (loginModelTemp.getUsername().equals(loginRequest.getUsername())) {
                if ( UsefulAuthHelper.validatePasswordStringWithPasswordBCrypt(loginRequest.getPassword(), loginModelTemp.getPassword())) { // check password string with password bcrypt from database
                    JwtBuilder jwtBuilder = jwtService.generateToken(null, loginModelTemp); /// Generate token and set all details as claims,issue&expired token,... by LoginModel
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
