package configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.thoughtworks.xstream.XStream;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.monitoring.MessageMonitor;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class AxonConfig {

    @Value("${spring.data.mongodb.host:127.0.0.1}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database:user}")
    private String mongoDatabase;

    @Bean
    public MongoClient mongo() {
        ServerAddress serverAddress = new ServerAddress(mongoHost, mongoPort);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(serverAddress)))
                .applyToSocketSettings(builder ->
                        builder.connectTimeout(10, TimeUnit.SECONDS))
                .build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate axonMongoTemplate(MongoClient mongoClient) {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongoClient, mongoDatabase)
                .build();
    }

    @Bean
    @Primary
    public Serializer serializer() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{
                "com.springbank.**",
                "org.axonframework.**"
        });
        return XStreamSerializer.builder()
                .xStream(xStream)
                .build();
    }

    @Bean
    public TokenStore tokenStore(MongoTemplate axonMongoTemplate, Serializer serializer) {
        return MongoTokenStore.builder()
                .mongoTemplate(axonMongoTemplate)
                .serializer(serializer)
                .build();
    }

    @Bean
    public EventStorageEngine storageEngine(MongoTemplate axonMongoTemplate, Serializer serializer) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(axonMongoTemplate)
                .snapshotSerializer(serializer)       // important for snapshots!
                .eventSerializer(serializer)          // important for events!
                .build();
    }

    @Bean
    public EmbeddedEventStore eventStore(
            EventStorageEngine storageEngine,
            org.axonframework.config.Configuration configuration) {

        MessageMonitor<? super EventMessage<?>> messageMonitor =
                configuration.messageMonitor(EmbeddedEventStore.class, "eventStore");

        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(messageMonitor)
                .build();
    }
}
