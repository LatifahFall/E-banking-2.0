package org.latifah.employeedashboardback.aspect;

import org.latifah.employeedashboardback.service.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditService auditService;

    public AuditAspect() {
        this.auditService = auditService;
    }

//    @AfterReturning(pointcut = "execution(* org.latifah.employeedashboardback.service..*.*(..)) && !within(org.latifah.employeedashboardback.service.AuditService)", returning = "result")
//    public void logSuccess(JoinPoint joinPoint, Object result) {
//        String method = joinPoint.getSignature().getName();
//        String type = inferEntityType(joinPoint);
//        String id = extractEntityId(joinPoint, result);
//
//        Map<String, Object> details = new HashMap<>();
//        details.put("method", method);
//        auditService.logAction(method, type, id, details, true);
//    }

    @AfterReturning(
            pointcut = "execution(* org.latifah.employeedashboardback.service..*.*(..)) && !within(org.latifah.employeedashboardback.service.AuditService)",
            returning = "result")
    public void logSuccess(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().getName();
        String type = inferEntityType(joinPoint);
        String id = extractEntityId(joinPoint, result);

        Map<String, Object> details = new HashMap<>();
        details.put("method", method);

        // Ajout d’arguments utiles si disponibles
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            details.put("entityId", args[0]);
            if (args.length > 1 && args[1] instanceof Double) details.put("amount", args[1]);
            if (args.length > 2 && args[2] instanceof String) details.put("description", args[2]);
        }

        details.put("result", result != null ? result.toString() : "null");

        auditService.logAction(method, type, id, details, true);
    }


    @AfterThrowing(
            pointcut = "execution(* org.latifah.employeedashboardback.service..*.*(..)) && !within(org.latifah.employeedashboardback.service.AuditService)",
            throwing = "ex")
    public void logFailure(JoinPoint joinPoint, Exception ex) {
        String method = joinPoint.getSignature().getName();
        String type = inferEntityType(joinPoint);
        String id = extractEntityId(joinPoint, null);

        Map<String, Object> details = new HashMap<>();
        details.put("method", method);
        details.put("error", ex.getMessage());

        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            details.put("entityId", args[0]);
            if (args.length > 1 && args[1] instanceof Double) details.put("amount", args[1]);
            if (args.length > 2 && args[2] instanceof String) details.put("description", args[2]);
        }

        auditService.logAction(method, type, id, details, false);
    }


//    private String inferEntityType(JoinPoint jp) {
//        return jp.getTarget().getClass().getSimpleName().replace("Service", "").toUpperCase();
//    }

    private String inferEntityType(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // Cas particulier pour BankAccount
        if (className.toLowerCase().contains("bankaccount")) {
            return "BANK_ACCOUNT";
        }

        // Nettoyage du nom de classe générique
        return className
                .replace("ServiceImpl", "")
                .replace("Service", "")
                .toUpperCase();
    }


    private String extractEntityId(JoinPoint jp, Object result) {
        Object[] args = jp.getArgs();
        return args.length > 0 ? String.valueOf(args[0]) : "N/A";
    }
}
