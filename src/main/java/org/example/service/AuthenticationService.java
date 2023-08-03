package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.config.JwtService;
import org.example.model.repository.UserRepository;
import org.example.model.request.LoginRequest;
import org.example.model.request.RegisterRequest;
import org.example.model.response.LoginResponse;
import org.example.model.response.RegisterResponse;
import org.example.model.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ModelMapper mapper = new ModelMapper();
    public RegisterResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email_verified(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return RegisterResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .message("User successfully register")
                .build();
    }

    public LoginResponse login(LoginRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e){
            throw new BadCredentialsException(e.getLocalizedMessage());
        }
        User foundUser = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(foundUser);
        return LoginResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtService.generateRefreshToken(foundUser))
                .userId(foundUser.getId())
                .build();
    }

    public void signUpSocialUser(RegisterRequest registerRequest) {
        User user = mapper.map(registerRequest, User.class);
        user.setEmail(registerRequest.getEmail());
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setEmail_verified(true);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail != null) {
//            var user = userRepository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}
