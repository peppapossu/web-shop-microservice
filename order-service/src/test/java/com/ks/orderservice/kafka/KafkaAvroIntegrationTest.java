package com.ks.orderservice.kafka;

import com.ks.avro.order.Item;
import com.ks.avro.order.OrderCreatedEvent;
import com.ks.common.proto.Money;
import com.ks.common.proto.ProductResponse;
import com.ks.orderservice.mapper.AvroMapper;
import com.ks.orderservice.repository.AppUserRepository;
import com.ks.orderservice.repository.CustomerRepository;
import com.ks.orderservice.repository.RefreshTokenRepository;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(MockDataSourceConfig.class)
class KafkaAvroIntegrationTest {

    @Autowired
    AvroMapper avroMapper;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private AppUserRepository appUserRepository;
    @MockBean
    private CustomerRepository customerRepository;

    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private Consumer<String, OrderCreatedEvent> consumer;

    static Network network = Network.newNetwork();

    // Kafka контейнер
    static KafkaContainer kafka =
            new KafkaContainer(
                    DockerImageName.parse("confluentinc/cp-kafka:7.6.0")
                            .asCompatibleSubstituteFor("apache/kafka")
            )
                    .withNetwork(network)
                    .withNetworkAliases("kafka");

    // Schema Registry контейнер
    static GenericContainer<?> schemaRegistry;

    @BeforeAll
    static void startContainers() {
        kafka.start();

        schemaRegistry = new GenericContainer<>("confluentinc/cp-schema-registry:7.6.0")
                .withNetwork(network)
                .withNetworkAliases("schema-registry")
                .withExposedPorts(8081)
                .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
                .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
                .withEnv(
                        "SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
                        "PLAINTEXT://kafka:9092"
                )
                .waitingFor(Wait.forHttp("/subjects").forStatusCode(200));

        schemaRegistry.start();

        String srUrl = "http://" + schemaRegistry.getHost() + ":" + schemaRegistry.getMappedPort(8081);

        System.setProperty("schema.registry.url", srUrl);

        System.out.println("🔥 FORCED schema.registry.url = " + srUrl);
    }


    @BeforeEach
    void setupKafka() {
        // Producer
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put("schema.registry.url", "http://localhost:" + schemaRegistry.getMappedPort(8081));
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));

        // Consumer
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group" + UUID.randomUUID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        consumerProps.put("schema.registry.url", "http://localhost:" + schemaRegistry.getMappedPort(8081));
        consumerProps.put("specific.avro.reader", true);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new DefaultKafkaConsumerFactory<String, OrderCreatedEvent>(consumerProps).createConsumer();
        consumer.subscribe(List.of("order-created"));
    }


    // Подставляем динамически свойства для Spring Boot
    @DynamicPropertySource
    static void dynamicProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.properties.schema.registry.url",
                () -> "http://localhost:" + schemaRegistry.getMappedPort(8081));
//                () -> "http://" + schemaRegistry.getHost() + ":" + schemaRegistry.getFirstMappedPort());
        registry.add("spring.kafka.consumer.properties.specific.avro.reader", () -> true);
    }


    @Test
    void testProducerConsumer() {
        // Создаём Avro сообщение
        List<Item> items = new ArrayList<>();

        items.add(avroMapper.toItemAvro(ProductResponse.newBuilder()
                .setPrice(Money.newBuilder()
                        .setAmount(100)
                        .build())
                .setProductId(1)
                .setSale(0)
                .setQuantity(1)
                .setTotalPrice(Money.newBuilder()
                        .setAmount(110)
                        .build())

                .build()));


        OrderCreatedEvent event = OrderCreatedEvent.newBuilder()
                .setCustomerId(1)
                .setOrderId(2)
                .setItems(items)
                .build();

        System.out.println(event.toString());


        assertTrue(kafka.isRunning());
        assertTrue(schemaRegistry.isRunning());

        AdminClient admin = AdminClient.create(Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers()
        ));
        NewTopic topic = new NewTopic("order-created", 1, (short) 1);
        admin.createTopics(List.of(topic));
        admin.close();


        // Отправляем через producer
        kafkaTemplate.send("order-created", event);
        kafkaTemplate.flush();

        consumer.subscribe(List.of("order-created"));

        // Получаем сообщение
        ConsumerRecord<String, OrderCreatedEvent> record =
                KafkaTestUtils.getSingleRecord(consumer, "order-created");

        // Проверяем содержимое
        assertEquals(2L, record.value().getOrderId());
    }

}

