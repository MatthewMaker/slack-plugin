package jenkins.plugins.slakk;

import jenkins.plugins.slakk.StandardSlakkService;
import org.junit.Before;
import org.junit.Test;

public class StandardSlakkServiceTest {

    /**
     * Publish should generally not rethrow exceptions, or it will cause a build job to fail at end.
     */
    @Test
    public void publishWithBadHostShouldNotRethrowExceptions() {
        StandardSlakkService service = new StandardSlakkService("foo", "token", "#general");
        service.setHost("hostvaluethatwillcausepublishtofail");
        service.publish("message");
    }

    /**
     * Use a valid host, but an invalid team domain
     */
    @Test
    public void invalidTeamDomainShouldFail() {
        StandardSlakkService service = new StandardSlakkService("my", "token", "#general");
        service.publish("message");
    }

    /**
     * Use a valid team domain, but a bad token
     */
    @Test
    public void invalidTokenShouldFail() {
        StandardSlakkService service = new StandardSlakkService("tinyspeck", "token", "#general");
        service.publish("message");
    }
}
