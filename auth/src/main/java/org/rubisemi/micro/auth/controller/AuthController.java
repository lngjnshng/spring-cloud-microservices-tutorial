package org.rubisemi.micro.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.rubisemi.micro.auth.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "")
@Tag(name = "Authentication Login API", description = "Create, Update, Delete and Query orders")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final JwtEncoder encoder;

    public AuthController(UserDetailsService userDetailsService, JwtEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    @Operation(summary = "Login", description = "Login with username and password")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Login successfully", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Fail to login"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        if (user.getPassword().equalsIgnoreCase(userDetails.getPassword())) {
            String token = generateToken(userDetails);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("X-AUTH-TOKEN", token);
            return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_JSON).body("{\"token\":\"" + token + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("Invalid username or password");
        }
    }

    private String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        long expiry = 36000L;
        // @formatter:off
        String scope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(userDetails.getUsername())
                .claim("scope", scope)
                .build();
        // @formatter:on
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
