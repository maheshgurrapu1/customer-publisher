package com.prokarma.customer.publisher.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;

@Component
@Aspect
public class LoggingAspect {

  Logger log = LogManager.getLogger(LoggingAspect.class);

  @Pointcut("within(@org.springframework.stereotype.Controller *)")
  public void controller() {}

  @Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
  private void requestMappingAnnotations() {}

  @Pointcut("execution(* *.*(..))")
  protected void allMethod() {}

  @Autowired
  private Gson gson;

  @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) "
      + "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
      + "|| @annotation(org.springframework.web.bind.annotation.PostMapping)"
      + "|| @annotation(org.springframework.web.bind.annotation.PathVariable)"
      + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
      + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
  public void mappingAnnotations() {}

  @Before("controller() && allMethod() && args(customer,..)")
  public void logBefore(JoinPoint joinPoint, Object customer) {
    log.info("Request : {}", gson.toJson(customer));
  }

  @AfterReturning(pointcut = "(controller() || requestMappingAnnotations()) && allMethod()",
      returning = "result")
  public void logAfter(JoinPoint joinPoint, Object result) {
    log.info("Returning : {}", result);
  }


}
