package dk.kb.ccs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The constants for the Cumulus Crowd Service application.
 * Automatically extracted values for the setup.
 */
@Component
public class CcsConstants {

    /** The name of the application, automatically extracted from setup.*/
    @Value("${application.name}")
    protected String applicationName;

    /** The build version of the application, automatically extracted.*/
    @Value("${build.version}")
    protected String buildVersion;

    /**
     * @return The automatically extracted name of the application.
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @return The build version of the application.
     */
    public String getBuildVersion() {
        if(buildVersion == null) {
            return CcsConstants.class.getPackage().getImplementationVersion();
        }
        return buildVersion;
    }
}
