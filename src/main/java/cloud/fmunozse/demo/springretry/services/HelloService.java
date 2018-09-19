package cloud.fmunozse.demo.springretry.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    Logger logger = LoggerFactory.getLogger(HelloService.class);


    class BoomException extends RuntimeException {
        public BoomException(String message) {
            super(message);
        }
    }


    @CircuitBreaker(maxAttempts = 2, include = BoomException.class)
    public int callRetriableExternalSystem(String uuid ) {

        //prepare the retry template to try do the 3 times
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate.execute(context -> {
            int result = doCallSystem(uuid);
            return result;
        });
    }


    public int doCallSystem (String uuid ) {
        boolean isOk = isOK(.9);
        logger.info("[{},{}] callRetriableExternalSystem ... ", uuid, (isOk ? "OK" : "KO") );

        if (!isOk) {
            logger.info("[{},{}]    waiting .... ", uuid, (isOk ? "OK" : "KO") );
            sleep(1000l);
            throw new BoomException("Error happened");
        }

        logger.info("[{},{}] returned ok .... ", uuid, (isOk ? "OK" : "KO") );
        return 1;
    }

    @Recover
    private int fallbackForCall(String uuid) {
        logger.error("[{}] Fallback for call invoked ", uuid );
        return 0;
    }

    private boolean isOK(double percentWhenIsOk) {
        double random = Math.random();
        return random > percentWhenIsOk ;
    }

    private void sleep (long millis)  {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
