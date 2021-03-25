package com.example.reddit.service;

import com.example.reddit.dto.AuthenticationResponse;
import com.example.reddit.dto.LoginRequest;
import com.example.reddit.dto.RegisterRequest;
import com.example.reddit.exceptions.SpringRedditException;
import com.example.reddit.model.NotificationEmail;
import com.example.reddit.model.User;
import com.example.reddit.model.VerificationToken;
import com.example.reddit.repository.UserRepository;
import com.example.reddit.repository.VerificationTokenRepository;
import com.example.reddit.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


/*
* we use dto here, and the reason is to
*"be a coarse-grained data container which efficiently transports data between processes"
* dto only fulfills the fields that are needed to be transferred from the frontend UI
* and service would receive those fields, and store them to the persistence layer by calling repository
* */


@Service
@AllArgsConstructor //provides constructor based di 

public class AuthService {

    //two types of DI, see
    // https://stackoverflow.com/questions/63259116/what-is-the-difference-between-using-autowired-annotation-and-private-final
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String verificationToken = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + verificationToken));
    }



    public String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token){
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringRedditException("Username not found")));

    }

    public void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("Invalid Token"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authenticate =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
