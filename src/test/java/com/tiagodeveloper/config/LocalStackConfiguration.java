package com.tiagodeveloper.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
@TestConfiguration
@TestPropertySource(properties = {
        "amazon.aws.accesskey=test1",
        "amazon.aws.secretkey=test231"
})
public class LocalStackConfiguration {
	
	private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres")
	        .withDatabaseName("postgres")
	        .withUsername("postgres")
	        .withPassword("postgres")
	        .withInitScript("init_script.sql");
	
	private static final LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
                    .withServices(LocalStackContainer.Service.DYNAMODB)
                    .withNetworkAliases("localstack")
                    .withNetwork(Network.builder().createNetworkCmdModifier(cmd -> cmd.withName("test-net")).build());

    static {
    	postgresqlContainer.start();
    	localStack.start();
    	System.setProperty("amazon.dynamodb.endpoint", localStack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString());
    }
    
    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, 
				"spring.datasource.url="+ postgresqlContainer.getJdbcUrl(),
				"spring.datasource.username="+ postgresqlContainer.getUsername(),
				"spring.datasource.password="+ postgresqlContainer.getPassword()
//				,"spring.main.web-application-type=none"
			);
			
		}
    	
    }
}
