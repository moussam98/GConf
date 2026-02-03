package com.ensao.gi4.security.auth.ott;

import com.ensao.gi4.security.auth.AuthenticationResponse;
import com.ensao.gi4.security.token.TokenService;
import com.ensao.gi4.service.api.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ott.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/ott")
final class OneTimeTokenController {

    private final OneTimeTokenService ottService;
    private final UserService userService;
    private final TokenService tokenService;


    OneTimeTokenController(OneTimeTokenService oneTimeTokenService, UserService userService, TokenService tokenService) {
        this.ottService = oneTimeTokenService;
        this.userService = userService;
        this.tokenService = tokenService;
    }


    @PostMapping("/generate")
    public ResponseEntity<String> generateToken(@RequestParam("username") String username){
        userService.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Couldn't generate the one time token. Username doesn't exist !"));
        OneTimeToken ott = ottService.generate(new GenerateOneTimeTokenRequest(username));
        // TODO: create a mail service to send the token
        System.out.printf("Generating token for username: %s and token: %s\n", ott.getUsername(), ott.getTokenValue());
        return ResponseEntity.ok("One time token has been generated and sent to your email !");
    }

    @GetMapping("/login")
    public ResponseEntity<AuthenticationResponse> verifyOTT(@RequestParam String token){
        OneTimeToken consumed = ottService.consume(new OneTimeTokenAuthenticationToken(token));
        if(Objects.isNull(consumed)){
           throw new InvalidOneTimeTokenException("Invalid token");
        }
        String accessToken = tokenService.generateAccessToken(consumed.getUsername());
        String refreshToken = tokenService.generateRefreshToken(consumed.getUsername());
        tokenService.saveUserTokens(consumed.getUsername(), accessToken, refreshToken);
        return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
    }


}
