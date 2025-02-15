package com.example.doseo_user.controller;

import com.example.doseo_user.dto.UserRequestDTO;
import com.example.doseo_user.dto.UserResponseDTO;
import com.example.doseo_user.entity.User;
import com.example.doseo_user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Environment environment;
    //매개변수 채워서 회원이 등록되도록 처리(포스트맨으로 테스트)
    //Controller -> Service -> DAO -> Repository
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO requestDTO) {
        System.out.println(requestDTO+"===================================");
        userService.write(requestDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PostMapping("/login")
    public UserResponseDTO login(@RequestParam("username") String username,
                                 @RequestParam("password") String password, HttpServletResponse response) {
        UserResponseDTO responseDTO = userService.login(username, password);
        if(responseDTO != null) {//인증이 성공
            String mytoken = Jwts.builder()
                    //사용자정의클레이임
                    .setSubject(responseDTO.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() +
                            Long.parseLong(environment.getProperty("jwt.token-valid-in-millisecond"))))
                    .signWith(SignatureAlgorithm.HS512,environment.getProperty("jwt.secret"))
                    .compact();//위의 정보를 이용
            //response의 헤더에 셋팅
            response.setHeader("Authorization", mytoken);
            response.setHeader("userName", URLEncoder.encode(responseDTO.getUsername(), StandardCharsets.UTF_8));
        }
        return responseDTO;
    }
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            // JWT 토큰 파싱
            String username = Jwts.parser()
                    .setSigningKey(environment.getProperty("jwt.secret"))
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            // 사용자 정보를 반환
            return ResponseEntity.ok(Map.of("username", username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        }
    }

    @GetMapping("/mypage")
    public String mypage(){
        return "mypage";
    }

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "Rent deleted successfully! delete -------";
    }

    @GetMapping("/all")
    public List<User> findAll() {
        return userService.findAll();
    }
}
