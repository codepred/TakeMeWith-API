package codepred.configuration;


import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MethodLoggerAspect {

    private final Logger logger = LoggerFactory.getLogger(MethodLoggerAspect.class);

    private final EntityManagerFactory entityManagerFactory;

    @Value("${rest.methods.logger.enabled:true}")
    private boolean restMethodsLoggerEnabled;

    @Value("${service.methods.logger.enabled:true}")
    private boolean serviceMethodsLoggerEnabled;

    @Value("${repository.methods.logger.enabled:true}")
    private boolean repositoryMethodsLoggerEnabled;
    @Autowired
    public MethodLoggerAspect(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    @Around("@within(restController)")
    public Object logRestControllerMethod(ProceedingJoinPoint joinPoint, RestController restController) throws Throwable {
        return loggerServiceFormatter(joinPoint, restMethodsLoggerEnabled);
    }

    @Around("@within(service)")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint, Service service) throws Throwable {
        return loggerRestControllerFormatter(joinPoint, serviceMethodsLoggerEnabled);
    }

    @Around("@within(repository)")
    public Object logRepositoryMethod(ProceedingJoinPoint joinPoint, Repository repository) throws Throwable {
        return loggerRepositoryMethod(joinPoint, repositoryMethodsLoggerEnabled);
    }

    private Object loggerRestControllerFormatter(ProceedingJoinPoint joinPoint, boolean enabled) throws Throwable {
        if (enabled) {
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();

            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            logger.info("{} ==> {}() - start: request = {}", joinPoint.getTarget().getClass().getSimpleName(), methodName, args);
            logger.info("{} ==> {}() - end: response = {} Execution Time: {}",
                        getColorizedClass(joinPoint), methodName, Objects.toString(result, "null"), getColoredTime(executionTime));

            return result;
        } else {
            return joinPoint.proceed();
        }
    }
    private Object loggerServiceFormatter(ProceedingJoinPoint joinPoint, boolean enabled) throws Throwable {
        if (enabled) {
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();

            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            logger.info("Service ==> {}() - start: request = {}", methodName, args);

            long queryCount = getHibernateQueryCount();

            logger.info("Service ==> Hibernate Query Count: {}", queryCount);
            logger.info("Service ==> {}() - end: response = {} Execution Time: {}",
                        methodName, Objects.toString(result, "null"), getColoredTime(executionTime));

            return result;
        } else {
            return joinPoint.proceed();
        }
    }

    private long getHibernateQueryCount() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        return statistics.getQueryExecutionCount();
    }

    private Object loggerRepositoryMethod(ProceedingJoinPoint joinPoint, boolean enabled) throws Throwable {
        if (enabled) {
            String methodName = joinPoint.getSignature().getName();

            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            logger.info("{} ==> {}() - Execution Time: \u001B[31m{} ms\u001B[0m",
                        getColorizedClass(joinPoint), methodName, getColoredTime(executionTime));

            return result;
        } else {
            return joinPoint.proceed();
        }
    }


    private String getColorizedClass(JoinPoint joinPoint) {
        return "\u001B[34m" + joinPoint.getTarget().getClass().getSimpleName() + "\u001B[0m";
    }

    private String getColoredTime(long executionTime) {
        return "\u001B[31m" + executionTime + " ms\u001B[0m";
    }
}
