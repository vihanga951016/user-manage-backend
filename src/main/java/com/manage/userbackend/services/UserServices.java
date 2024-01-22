package com.manage.userbackend.services;

import com.manage.userbackend.beans.UserBean;
import com.manage.userbackend.beans.requests.auth.AuthRequestBean;
import com.manage.userbackend.exceptions.UserManageException;
import com.manage.userbackend.http.HttpResponse;
import com.manage.userbackend.repositories.UserRepository;
import com.manage.userbackend.security.AuthService;
import com.manage.userbackend.security.HashUtils;
import com.manage.userbackend.security.JwtTypes;
import com.manage.userbackend.security.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("Duplicates")
public class UserServices {

    private final UserRepository userRepository;

    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthService authService;

    public ResponseEntity getAllUsers(HttpServletRequest request) {
        try {
            Claims claims = jwtUserDetailsService.authenticate(request, JwtTypes.SUPER_ADMIN,
                    JwtTypes.ADMIN, JwtTypes.USER);

            AuthRequestBean authRequest = authService.userAuthorities(claims, 4);

            if(authRequest.isHasError()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("You don't have permissions for this."));
            }

            List<UserBean> usersList;

            if(claims.getSubject().equals(JwtTypes.SUPER_ADMIN.name())) {
                usersList = userRepository.getAllUsersForSuperAdmin();
            } else if (claims.getSubject().equals(JwtTypes.ADMIN.name())) {
                usersList = userRepository.getAllUsersForAdmin();
            } else {
                usersList = userRepository.getAllUsersForUser();
            }

            return ResponseEntity.ok().body(new HttpResponse<>().responseOk(usersList));

        } catch (UserManageException ex) {
            return ResponseEntity.internalServerError().body(new HttpResponse<>()
                    .responseFail(ex.getMessage()));
        }
    }

    public ResponseEntity addUser(UserBean userBean, HttpServletRequest request) {
        try {
            Claims claims = jwtUserDetailsService.authenticate(request, JwtTypes.SUPER_ADMIN,
                    JwtTypes.ADMIN, JwtTypes.USER);

            AuthRequestBean authRequest = authService.userAuthorities(claims, 1);

            if(authRequest.isHasError()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("You don't have permissions for this."));
            }

            userBean.setPassword(HashUtils.hash(userBean.getPassword()));

            return ResponseEntity.ok()
                    .body(new HttpResponse<>()
                            .responseOk(userRepository.save(userBean)));

        } catch (UserManageException ex) {
            return ResponseEntity.internalServerError().body(new HttpResponse<>()
                    .responseFail(ex.getMessage()));
        }
    }

    public ResponseEntity updateUser(UserBean userBean, HttpServletRequest request) {
        try {
            Claims claims = jwtUserDetailsService.authenticate(request, JwtTypes.SUPER_ADMIN,
                    JwtTypes.ADMIN, JwtTypes.USER);

            AuthRequestBean authRequest = authService.userAuthorities(claims, 2);

            if(authRequest.isHasError()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("You don't have permissions for this."));
            }

            if(userBean.getId() == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("Invalid user"));
            }

            UserBean user = userRepository.getById(userBean.getId());

            if(user == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("User not found"));
            }

            return ResponseEntity.ok()
                    .body(new HttpResponse<>()
                            .responseOk(userRepository.save(userBean)));

        } catch (UserManageException ex) {
            return ResponseEntity.internalServerError().body(new HttpResponse<>()
                    .responseFail(ex.getMessage()));
        }
    }

    public ResponseEntity userAvailability(Integer uid, HttpServletRequest request) {
        try {
            Claims claims = jwtUserDetailsService.authenticate(request, JwtTypes.SUPER_ADMIN,
                    JwtTypes.ADMIN, JwtTypes.USER);

            AuthRequestBean authRequest = authService.userAuthorities(claims, 2);

            if(authRequest.isHasError()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("You don't have permissions for this."));
            }

            UserBean user = userRepository.getById(uid);

            if(user == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("User not found"));
            }

            userRepository.userActivation(uid, !user.isDeactivated());

            return ResponseEntity.ok()
                    .body(new HttpResponse<>()
                            .responseOk(!user.isDeactivated()));

        } catch (UserManageException ex) {
            return ResponseEntity.internalServerError().body(new HttpResponse<>()
                    .responseFail(ex.getMessage()));
        }
    }

    public ResponseEntity deleteUser(Integer uid, HttpServletRequest request) {
        try {
            Claims claims = jwtUserDetailsService.authenticate(request, JwtTypes.SUPER_ADMIN,
                    JwtTypes.ADMIN, JwtTypes.USER);

            AuthRequestBean authRequest = authService.userAuthorities(claims, 3);

            if(authRequest.isHasError()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("You don't have permissions for this."));
            }

            UserBean user = userRepository.getById(uid);

            if(user == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                        .responseFail("User not found"));
            }

            userRepository.deleteById(uid);

            return ResponseEntity.ok()
                    .body(new HttpResponse<>()
                            .responseOk("User deleted"));

        } catch (UserManageException ex) {
            return ResponseEntity.internalServerError().body(new HttpResponse<>()
                    .responseFail(ex.getMessage()));
        }
    }
}
