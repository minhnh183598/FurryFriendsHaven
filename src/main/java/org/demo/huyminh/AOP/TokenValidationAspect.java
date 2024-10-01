package org.demo.huyminh.AOP;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.InvalidateRepository;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Minh
 * Date: 9/27/2024
 * Time: 10:58 PM
 */

@Slf4j
@Aspect
@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TokenValidationAspect {

    final InvalidateRepository invalidateRepository;

    @Before("execution(* org.demo.huyminh.Controller..*(..))")
    public void validateToken() throws ParseException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return;
        }

        token = token.substring(7);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            if (invalidateRepository.existsById(jti)) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }
}
