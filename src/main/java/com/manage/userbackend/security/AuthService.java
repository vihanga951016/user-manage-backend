package com.manage.userbackend.security;

import com.manage.userbackend.beans.UserBean;
import com.manage.userbackend.beans.UserPermissionBean;
import com.manage.userbackend.beans.requests.auth.AuthRequestBean;
import com.manage.userbackend.repositories.UserPermissionRepository;
import com.manage.userbackend.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@SuppressWarnings("Duplicates")
public class AuthService {
    private static Logger LOGGER = LogManager.getLogger(AuthService.class);

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;

    public String createUserLoginToken(UserBean userBean, String password){
        Map<String, Object> claims = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        long expireTime = calendar.getTimeInMillis();
        LOGGER.info("User login expires on >>> " + calendar.getTime().toString());

        String userType = null;

        LOGGER.info(userBean.getRole().getRole() + " is trying to login");

        if (userBean.getRole().getRole().equals(JwtTypes.SUPER_ADMIN.name())){
            userType = JwtTypes.SUPER_ADMIN.name();
        } else if(userBean.getRole().getRole().equals(JwtTypes.USER.name())) {
            userType = JwtTypes.USER.name();
        } else {
            LOGGER.error("User type is not defined");
        }

        claims.put("user", userBean.getId());
        claims.put("uid", userBean.getId());
        claims.put("permission", userBean.getRole().getRole());

        return jwtTokenUtil.generateTokenWithExp
                (new User(userType, password, new ArrayList<>()), expireTime, claims);
    }

    public AuthRequestBean userAuthorities(Claims claims, Integer accessId) {

        Integer claimedUserId = claims.get("uid", Integer.class);

        UserBean userBean = userRepository.getById(claimedUserId);

        String userType = claims.getSubject();

        if(userType.equals(JwtTypes.SUPER_ADMIN.name())) {
            return new AuthRequestBean("allowed",
                    false, 5);
        } else {
            UserPermissionBean permission = userPermissionRepository
                    .getUserPermissionsByRoleAndPermission(userBean.getRole().getId(), accessId);

            if(permission == null) {

                LOGGER.error("error");

                return new AuthRequestBean("Not Allowed",
                        true, null);
            }

            return new AuthRequestBean("Allowed",
                    false, accessId);
        }
    }
}
