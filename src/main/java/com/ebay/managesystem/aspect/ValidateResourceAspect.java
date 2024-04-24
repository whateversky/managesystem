package com.ebay.managesystem.aspect;

import com.ebay.managesystem.annotation.ValidateResource;
import com.ebay.managesystem.exception.ServiceException;
import com.ebay.managesystem.support.UserInfoContext;
import com.ebay.managesystem.exception.ResourceAccessException;
import com.ebay.managesystem.repository.UserInfoRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Aspect
@Component
public class ValidateResourceAspect {
    @Resource
    private UserInfoRepository userInfoRepository;

    @Pointcut("@annotation(com.ebay.managesystem.annotation.ValidateResource)")
    public void pointcut() {

    }


    @Before(value = "pointcut() && @annotation(validateResource)")
    public void before(JoinPoint joinPoint,
                       ValidateResource validateResource) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        int index = -1;
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                if (Objects.equals(parameterName, validateResource.resourceKey())) {
                    index = i;
                    break;
                }
            }
        }
        if (index >= 0) {
            if (!userInfoRepository.isUserIdDataAccessible(UserInfoContext.getUserId(), joinPoint.getArgs()[index].toString())) {
                throw new ResourceAccessException("resource cannot be accessed");
            }
        } else {
            throw new ServiceException("resource key config error");
        }

    }

}
