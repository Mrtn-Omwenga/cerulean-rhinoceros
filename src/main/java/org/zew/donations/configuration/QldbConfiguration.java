package org.zew.donations.configuration;

import com.amazon.ion.IonSystem;
import com.amazon.ion.system.IonSystemBuilder;
import com.fasterxml.jackson.dataformat.ion.IonObjectMapper;
import com.fasterxml.jackson.dataformat.ion.ionvalue.IonValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.qldbsession.QldbSessionClient;
import software.amazon.qldb.QldbDriver;

import static java.util.Objects.requireNonNull;

@Configuration
public class QldbConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public IonObjectMapper ionObjectMapper() {
        return new IonValueMapper(ionSystem());
    }

    @Bean
    public QldbDriver qldbDriver() {
        return QldbDriver
                .builder()
                .ledger(requireNonNull(env.getProperty("org.zew.qldb.ledger", String.class)))
                .sessionClientBuilder(
                        QldbSessionClient
                                .builder()
                                .credentialsProvider(ProfileCredentialsProvider.create("zew"))
                                .region(Region.EU_CENTRAL_1))
                .build();
    }

    @Bean
    public IonSystem ionSystem() {
        return IonSystemBuilder.standard().build();
    }

}
