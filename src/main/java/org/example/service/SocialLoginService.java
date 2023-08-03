package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.model.repository.UserRepository;
import org.example.model.request.LoginRequest;
import org.example.model.request.RegisterRequest;
import org.example.model.request.SocialLoginRequest;
import org.example.model.response.LoginResponse;
import org.example.model.user.SocialLogin;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public LoginResponse googleLogin(SocialLoginRequest request){
        String GOOGLE_AUTH_URL = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
        Object object = getUserDetailsFromSocialApi(request, GOOGLE_AUTH_URL + request.getId_token());
        String stringObject = mapObjectToString(object);
        SocialLogin socialLogin = readValue(stringObject, SocialLogin.class);
        signUpUserIfExists(socialLogin);

        return authenticationService.login(new LoginRequest(socialLogin.getEmail(), socialLogin.getId()));
    }

    private Object getUserDetailsFromSocialApi(SocialLoginRequest socialLoginRequest, String url) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + socialLoginRequest.getId_token());
        HttpEntity<?> http = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, http, Object.class).getBody();
    }

    private <T> T readValue(String objectString, Class<T> className) throws RuntimeException {
        try {
            return objectMapper.readValue(objectString, className);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

        private String mapObjectToString(Object object) throws RuntimeException {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

    private void signUpUserIfExists(SocialLogin socialUser) throws RuntimeException {
        if (!socialUser.isVerified_email()) {
            socialUser.setVerified_email(socialUser.isEmail_verified());
        }
        if (!socialUser.isVerified_email()) {
            throw new RuntimeException("Your email address is not verified");
        }

        if (!userRepository.existsByEmail(socialUser.getEmail())) {
            if (socialUser.getId() == null) socialUser.setId(socialUser.getSub());
            RegisterRequest signupRequest = new RegisterRequest(socialUser.getEmail(), socialUser.getId());
            if (socialUser.getGiven_name() == null && socialUser.getFamily_name() == null
                    && socialUser.getName() != null) {
                String[] names = socialUser.getName().split(" ");
                signupRequest.setFirstName(names[0]);
                signupRequest.setLastName(names[1]);
            } else {
                signupRequest.setFirstName(socialUser.getGiven_name());
                signupRequest.setLastName(socialUser.getFamily_name());
            }
            authenticationService.signUpSocialUser(signupRequest);
        }
    }
    }
