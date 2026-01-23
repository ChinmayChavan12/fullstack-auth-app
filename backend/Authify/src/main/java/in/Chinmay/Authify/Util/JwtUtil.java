package in.Chinmay.Authify.Util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private String secretKey;
    public JwtUtil(){
        secretKey=generateSecretKey();
    }

    public String generateSecretKey(){
        try{
            KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey= keyGen.generateKey();
            System.out.println("Secret Key: "+secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());

        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error genrating secret key",e);
        }

    }

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
       return  createToken(claims,userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String email) {
            return Jwts.builder().setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                    .signWith(SignatureAlgorithm.HS256,secretKey)
                    .compact();
    }
}
