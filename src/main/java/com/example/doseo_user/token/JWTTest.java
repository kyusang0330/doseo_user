package com.example.doseo_user.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JWTTest {
    public static void printToken(String token) {
        String[] split = token.split("\\.");
        //base64로 디코딩해서 토큰의 내부 값 읽기
        System.out.println("header:"+new String(Base64.getDecoder().decode(split[0])));
        System.out.println("payload:"+new String(Base64.getDecoder().decode(split[1])));
    }
    public static void main(String[] args) {
        //jjwt를 이용한 토큰의 사용
        //토큰생성 - 빌더를 이용해서 작업
        //signWith로 서명을 만들때 정의한 "myvalue"라는 값이 우리 서버에서 지정한 비밀키
        //                             --------
        //                             20글자 이상 인코딩된 문자열로 작업
        String mytoken = Jwts.builder()
                //사용자정의클레이임
                .addClaims(Map.of("name","bts","id","heaves"))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(SignatureAlgorithm.HS256,"myvalue")
                .compact();//위의 정보를 이용해서 토큰을 생성
        System.out.println(mytoken);
        printToken(mytoken);
        System.out.println("==========================");
        //토큰을 파싱하는 작업
       Jws<Claims> tokeninfo = Jwts.parser().setSigningKey("myvalue").parseClaimsJws(mytoken);
        System.out.println(tokeninfo);
        System.out.println(tokeninfo.getBody());
        System.out.println(tokeninfo.getBody().get("username"));
        System.out.println(tokeninfo.getBody().getExpiration());
        System.out.println(tokeninfo.getSignature());


    }
}
