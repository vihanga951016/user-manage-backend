package com.manage.userbackend.services;

import com.manage.userbackend.beans.RoleBean;
import com.manage.userbackend.beans.UserAuthenticationBean;
import com.manage.userbackend.beans.UserBean;
import com.manage.userbackend.beans.requests.auth.UserLoginRequestBean;
import com.manage.userbackend.exceptions.UserManageException;
import com.manage.userbackend.http.HttpResponse;
import com.manage.userbackend.repositories.RoleRepository;
import com.manage.userbackend.repositories.UserAuthenticationRepository;
import com.manage.userbackend.repositories.UserRepository;
import com.manage.userbackend.security.AuthService;
import com.manage.userbackend.security.HashUtils;
import com.manage.userbackend.security.JwtTypes;
import com.manage.userbackend.security.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final RoleRepository roleRepository;
    private final AuthService authService;

    private static Logger LOGGER = LogManager.getLogger(AuthServices.class);

    public ResponseEntity register(UserBean userBean, HttpServletRequest request){
        try {
            jwtUserDetailsService.authenticate(request, JwtTypes.USER_MANAGE_APP);

            UserBean findAdmin = userRepository.findAnyAdmin();

            if(findAdmin != null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HttpResponse<>()
                        .responseFail("Supper Admin Already Exist"));
            }

            RoleBean roleBean = roleRepository.getSuperAdmin();

            UserBean user = UserBean.builder()
                    .fullName(userBean.getFullName())
                    .email(userBean.getEmail())
                    .deactivated(false)
                    .password(userBean.getPassword())
                    .role(roleBean)
                    .build();

            return ResponseEntity.ok().body(new HttpResponse<>()
                    .responseOk(userRepository.save(user)));

        } catch (UserManageException ex) {
            return ResponseEntity.internalServerError().body(new HttpResponse<>()
                    .responseFail(ex.getMessage()));
        }
    }

    public ResponseEntity userLogin(HttpServletRequest request,
                                    UserLoginRequestBean requestBean) {
        try {

            jwtUserDetailsService.authenticate(request, JwtTypes.USER_MANAGE_APP);

            UserBean user = userRepository.findByEmail(requestBean.getUsername());

            if(user == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HttpResponse<>()
                        .responseFail("Incorrect Email"));
            }

            if(user.isDeactivated()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HttpResponse<>()
                        .responseFail("Account Deactivated"));
            }

            if(HashUtils.checkEncrypted(requestBean.getPassword(), user.getPassword())){
                LOGGER.info("Password hash matched | " + user.getId() + " | user |"
                        + user.getId() +" | email |" + user.getEmail());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HttpResponse<>()
                        .responseFail("Incorrect Password"));
            }

            user.setToken(authService.createUserLoginToken(user, requestBean.getPassword()));

            List<Integer> getAllPreviousLoginIds =
                    userAuthenticationRepository.getAllLoggedInEntityIds(requestBean.getUsername());

            if(getAllPreviousLoginIds.size() > 0) {
                userAuthenticationRepository
                        .updateAllLoggedInEntities(getAllPreviousLoginIds, new Date());
            }

            UserAuthenticationBean authenticationBean = UserAuthenticationBean
                    .builder()
                    .username(user.getEmail())
                    .loginTime(new Date())
                    .logoutTime(null)
                    .build();

            userAuthenticationRepository.save(authenticationBean);

            return ResponseEntity.ok().body(new HttpResponse<>().responseOk(user));

        } catch (UserManageException ex) {
            return ResponseEntity.internalServerError().body(new HttpResponse<>()
                    .responseFail(ex.getMessage()));
        }
    }

    public ResponseEntity userLogout(HttpServletRequest request) {
        try {
            Claims claims = jwtUserDetailsService.authenticate(request, JwtTypes.USER,
                    JwtTypes.SUPER_ADMIN, JwtTypes.ADMIN);

            Integer userId = claims.get("uid", Integer.class);

            UserBean userBean = userRepository.getById(userId);

            if(userBean == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HttpResponse<>()
                        .responseFail("User Not Found"));
            }

            Integer loginEntityId = userAuthenticationRepository
                    .getLoggedInEntityId(userBean.getEmail());

            userAuthenticationRepository.updateLoggedInEntity(loginEntityId, new Date());

            return ResponseEntity.ok().body(new HttpResponse<>().responseOk(true));

        } catch (UserManageException ex) {
            return ResponseEntity.internalServerError().body(new HttpResponse<>()
                    .responseFail(ex.getMessage()));
        }
    }
}
