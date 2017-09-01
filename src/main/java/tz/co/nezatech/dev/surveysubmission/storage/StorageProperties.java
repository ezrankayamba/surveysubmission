package tz.co.nezatech.dev.surveysubmission.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties("storage")
@PropertySource("classpath:config.properties")
public class StorageProperties {

	@Value("${survey.form.repos.upload.folder}")
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
