
# Resumo dos termos na stack de Java

A seguir, está um resumo dos principais termos associados à stack de Java:

## Teorema CAP

O Teorema CAP é uma propriedade fundamental de sistemas distribuídos que afirma que é impossível para um sistema distribuído garantir simultaneamente as seguintes três propriedades: Consistência, Disponibilidade e Tolerância a Partição. Os sistemas distribuídos geralmente têm que fazer trade-offs entre essas propriedades.

## Sincronia de Dados

A sincronia de dados se refere ao processo de garantir que os dados sejam consistentes e atualizados em todos os pontos em que são armazenados ou acessados. Em sistemas distribuídos, a sincronia de dados pode ser um desafio, pois vários nós podem ter cópias dos mesmos dados e essas cópias podem se tornar inconsistentes.

## Dados Distribuídos

Dados distribuídos são dados que estão armazenados em vários locais e podem ser acessados por diferentes nós em um sistema distribuído. A distribuição de dados pode ajudar a aumentar a escalabilidade e a disponibilidade do sistema, mas também pode tornar mais difícil manter a consistência dos dados.

## Messages via Commands

As mensagens via comandos são uma abordagem para a comunicação entre componentes em sistemas distribuídos. Em vez de enviar diretamente dados ou chamadas de função, os componentes enviam mensagens que descrevem uma ação ou comando que deve ser executado por outro componente.

## Consistência Eventual

A consistência eventual é uma propriedade de sistemas distribuídos em que os dados se tornam consistentes em todos os nós após um período de tempo. Isso significa que, em alguns momentos, diferentes nós podem ter cópias diferentes dos mesmos dados, mas essas cópias acabarão convergindo para um estado consistente.

## UUIDs Universais

UUIDs universais são identificadores únicos e universais que são usados ​​para identificar recursos em sistemas distribuídos. Eles são usados ​​para evitar conflitos entre recursos com o mesmo nome ou identificador em diferentes nós.

## Publisher/Subscribe

Publisher/Subscriber é um padrão de comunicação em sistemas distribuídos em que os componentes podem publicar mensagens para um tópico e outros componentes podem se inscrever nesse tópico para receber as mensagens relevantes.

## Brokers

Brokers são componentes de software em sistemas de mensagens que são responsáveis ​​pela distribuição de mensagens entre os diferentes componentes. Eles geralmente implementam o padrão de publicação/inscrição.

## Exchanges

Exchanges são componentes em sistemas de mensagens que permitem aos componentes publicar e consumir mensagens de diferentes tópicos. Eles atuam como intermediários entre os produtores de mensagens e os consumidores.

## Error Queues

As filas de erros são filas de mensagens usadas para armazenar mensagens que não puderam ser entregues a seus destinatários. Isso pode ser útil em sistemas distribuídos, onde a entrega de mensagens pode falhar devido a falhas de rede ou outros problemas.

## Resiliência Síncrona

A resiliência síncrona se refere à capacidade de um sistema distribuído de lidar com falhas de componentes de forma síncrona. Isso significa que os componentes restantes do sistema devem ser capazes de continuar a operar sem problemas, mesmo quando alguns componentes falharem.

## Autenticação Distribuída

A autenticação distribuída é o processo de autenticar usuários em um sistema distribuído em que os usuários podem acessar diferentes recursos em diferentes nós do sistema. Isso pode ser alcançado por meio de uma autoridade central de autenticação ou por meio de sistemas descentralizados de autenticação e autorização. A autenticação distribuída é essencial para garantir que apenas usuários autorizados tenham acesso a recursos em um sistema distribuído.

Claro, aqui estão exemplos de código Java que ilustram cada tópico:

## Sincronia de Dados

```java
public class DataSynchronization {
    private Map<String, String> dataMap = new HashMap<>();

    public synchronized void setData(String key, String value) {
        dataMap.put(key, value);
    }

    public synchronized String getData(String key) {
        return dataMap.get(key);
    }
}

``` 

Este é um exemplo simples de sincronização de dados em Java usando a palavra-chave `synchronized` para garantir que apenas uma thread possa acessar o mapa de dados em um determinado momento.

## Messages via Commands


```java
public interface Command {
    void execute();
}

public class ConcreteCommand implements Command {
    private Receiver receiver;

    public ConcreteCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.doSomething();
    }
}

public class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}

public class Receiver {
    public void doSomething() {
        System.out.println("Doing something...");
    }
}

public class Main {
    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        Command command = new ConcreteCommand(receiver);
        Invoker invoker = new Invoker();

        invoker.setCommand(command);
        invoker.executeCommand();
    }
}
```

## Consistência Eventual

```java
public class EventuallyConsistentMap<K, V> {
    private Map<K, List<V>> map = new HashMap<>();

    public synchronized void put(K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(value);
    }

    public synchronized List<V> get(K key) {
        return map.getOrDefault(key, new ArrayList<>());
    }

    public synchronized void reconcile() {
        for (K key : map.keySet()) {
            List<V> values = map.get(key);
            if (values.size() > 1) {
                V mostRecentValue = values.get(0);
                for (int i = 1; i < values.size(); i++) {
                    if (values.get(i).compareTo(mostRecentValue) > 0) {
                        mostRecentValue = values.get(i);
                    }
                }
                map.put(key, Collections.singletonList(mostRecentValue));
            }
        }
    }
}

```

## Publisher/Subscribe


```java

public interface Publisher {
    void subscribe(Subscriber subscriber);
    void unsubscribe(Subscriber subscriber);
    void publish(Message message);
}

public class Message {
    private String content;

    public Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

public class ConcretePublisher implements Publisher {
    private List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void publish(Message message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.receive(message);
        }
    }
}

public class ConcreteSubscriber implements Subscriber {
    private String name;

    public ConcreteSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void receive(Message message) {
        System.out.println(name + " received message: " + message.getContent());
    }
}

public class Main {
    public static void main(String[] args) {
        Publisher publisher = new ConcretePublisher();
        Subscriber subscriber1 = new ConcreteSubscriber("Subscriber 1");
        Subscriber subscriber2 = new ConcreteSubscriber("Subscriber 2");

        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);

        publisher.publish(new Message("Hello world!"));

        publisher.unsubscribe(subscriber1);

        publisher.publish(new Message("Goodbye world!"));
    }
}

```

### Dados Distribuídos

Dados distribuídos referem-se a dados que estão armazenados em vários locais físicos diferentes. O exemplo a seguir demonstra como usar o Apache Cassandra para armazenar dados distribuídos:

```java
public class CassandraData {
    private Cluster cluster;
    private Session session;

    public CassandraData(String node) {
        cluster = Cluster.builder().addContactPoint(node).build();
        session = cluster.connect();

        session.execute("CREATE KEYSPACE IF NOT EXISTS example WITH replication "
                + "= {'class':'SimpleStrategy', 'replication_factor':3};");

        session.execute("CREATE TABLE IF NOT EXISTS example.data (key text PRIMARY KEY, value text);");
    }

    public void write(String key, String value) {
        session.execute("INSERT INTO example.data (key, value) VALUES ('" + key + "', '" + value + "');");
    }

    public String read(String key) {
        ResultSet result = session.execute("SELECT value FROM example.data WHERE key='" + key + "';");
        Row row = result.one();
        if (row != null) {
            return row.getString("value");
        } else {
            return null;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CassandraData data = new CassandraData("localhost");

        data.write("key", "value");

        String value = data.read("key");
        System.out.println(value); // imprime "value"
    }
}
```

### Brokers, Exchanges


```java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Publisher {
    private final static String QUEUE_NAME = "example_queue";
    private final static String EXCHANGE_NAME = "example_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String message = "Hello, world!";
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {
    private final static String QUEUE_NAME = "example_queue";
    private final static String EXCHANGE_NAME = "example_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}

```



### Error Queues
Nesse exemplo, o Consumer está vinculado à fila "example_queue" e recebe mensagens dessa fila. Se ocorrer um erro ao processar a mensagem, o Consumer rejeita a mensagem e a envia para uma fila de erro chamada "example_error_queue". Essa fila pode ser monitorada e processada posteriormente para lidar com as mensagens que não puderam ser processadas inicialmente.

```java
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private final static String QUEUE_NAME = "example_queue";
    private final static String ERROR_QUEUE_NAME = "example_error_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(ERROR_QUEUE_NAME, true, false, false, null);
        channel.basicQos(1);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                int result = 1 / 0; // Throws an exception to simulate an error
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.out.println(" [x] Error: " + e.getMessage());
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                channel.basicPublish("", ERROR_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, delivery.getBody());
            }
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
    }
}
```

**Resiliência Síncrona:**

```java
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public class Service {
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void doSomething() {
        // Do something that may fail
    }
}
```

Nesse exemplo, o método "doSomething" é anotado com a anotação "@Retryable" do Spring, o que significa que o método será retried automaticamente em caso de falha. O parâmetro "maxAttempts" define o número máximo de tentativas, enquanto o parâmetro "backoff" define a estratégia de backoff (neste caso, uma espera de 1 segundo entre as tentativas).

**Autenticação Distribuída:**

```java
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

public class Service {
    private OAuth2RestTemplate restTemplate;

    public Service(OAuth2RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void doSomething() {
        String result = restTemplate.getForObject("https://api.example.com/some-resource", String.class);
        // Process the result
    }
}
```

Nesse exemplo, o método "doSomething" usa um OAuth2RestTemplate do Spring para fazer uma solicitação HTTP a uma API protegida. O OAuth2RestTemplate é configurado com as credenciais de autenticação e é usado para fazer a solicitação. Dessa forma, a autenticação é gerenciada de forma
<hr>

30.  Spring Boot: Spring Boot é um framework para criação de aplicações Java de forma rápida e fácil, utilizando o ecossistema do Spring. O objetivo do Spring Boot é facilitar a configuração e o desenvolvimento de aplicações, fornecendo uma maneira simples de iniciar um projeto Spring com um mínimo de configuração.
    
31.  Spring Data JPA: Spring Data JPA é um subprojeto do Spring que fornece uma camada de acesso a dados com suporte a JPA (Java Persistence API). Ele permite que os desenvolvedores criem aplicativos com menos código, com recursos como consultas dinâmicas, suporte a transações, paginação e ordenação.
    
32.  Spring Web: O Spring Web é um subprojeto do Spring Framework que fornece suporte para a criação de aplicativos web. Ele inclui recursos para processamento de solicitações HTTP, mapeamento de URLs, gerenciamento de sessões e muito mais.
    
33.  Spring Validation: O Spring Validation é uma biblioteca que fornece recursos para validação de dados em aplicativos Spring. Ele inclui um conjunto de anotações e classes de utilitários para validação de dados de entrada.
    
34.  Spring HATEOAS: O Spring HATEOAS é um subprojeto do Spring que fornece suporte para implementação do padrão HATEOAS (Hypermedia as the Engine of Application State). Ele permite que os desenvolvedores criem aplicativos RESTful que fornecem links para os recursos relacionados.
    
35.  Spring Security: O Spring Security é um subprojeto do Spring que fornece recursos para autenticação e autorização em aplicativos Spring. Ele inclui recursos como gerenciamento de usuários, controle de acesso baseado em regras, criptografia de senhas e muito mais.
    
36.  Spring AMQP: Spring AMQP é um subprojeto do Spring que fornece suporte para a implementação de aplicativos baseados em AMQP (Advanced Message Queuing Protocol). Ele inclui recursos para enviar e receber mensagens, gerenciamento de filas e muito mais.
    
37.  Spring Cloud Gateway: O Spring Cloud Gateway é um subprojeto do Spring Cloud que fornece um gateway de API que pode ser usado para rotear solicitações para vários serviços. Ele inclui recursos como roteamento baseado em regras, balanceamento de carga, segurança e muito mais.
    
38.  Spring Cloud Netflix Eureka: O Spring Cloud Netflix Eureka é um subprojeto do Spring Cloud que fornece um serviço de registro e descoberta de serviços. Ele permite que os aplicativos sejam implantados dinamicamente em um ambiente de nuvem distribuído.
    
39.  Spring Cloud Resilience4j: O Spring Cloud Resilience4j é um subprojeto do Spring Cloud que fornece recursos para lidar com falhas em aplicativos distribuídos. Ele inclui recursos como tolerância a falhas, limitação de taxa, circuit breaker e muito mais.
    
40.  Spring Actuator: O Spring Actuator é um subprojeto do Spring que fornece recursos para monitoramento e gerenciamento de aplicativos Spring. Ele inclui recursos como verificação de integridade, exposição de métricas, gerenciamento de beans e muito mais.
-   Spring Cloud Gateway: é uma biblioteca do Spring que fornece uma solução de gateway para aplicativos baseados em nuvem. Ele oferece recursos como roteamento, filtragem e monitoramento de tráfego de rede.
    
-   Spring Cloud Netflix Eureka: é um servidor de registro de serviços para a infraestrutura em nuvem. Ele ajuda na identificação e localização de serviços distribuídos para que possam se comunicar uns com os outros.
    
-   Spring Cloud Resilience4j: é uma biblioteca para implementação de tolerância a falhas e resiliência em aplicativos baseados em nuvem. Ele fornece recursos como limitação de taxa, bulkhead e circuit breaker.
    
-   Spring Actuator: é uma extensão do Spring Boot que adiciona recursos para monitoramento, gerenciamento e diagnóstico de aplicativos em produção.
    
-   Spring Cloud Config: é uma biblioteca que fornece recursos para a configuração externa de aplicativos baseados em nuvem. Ele ajuda a gerenciar e distribuir as configurações de aplicativos em vários ambientes.
    
-   Spring Utils: é um conjunto de classes utilitárias do Spring que ajudam no desenvolvimento de aplicativos. Ele inclui classes para manipulação de coleções, data e hora, IO, entre outros.
    
-   Serializações: é um recurso do Spring que permite a conversão de objetos Java em formatos de dados como JSON, XML e outros.
    
-   Filters em API REST: é uma funcionalidade do Spring que permite a aplicação de filtros em requisições HTTP recebidas por um aplicativo RESTful.
    
-   Customizações de Validações: é uma funcionalidade do Spring que permite a personalização das validações de campos de formulários em aplicativos web.
    
-   APIs: é uma funcionalidade do Spring que permite a criação de interfaces de programação de aplicativos (APIs) em aplicativos baseados em nuvem.
    
-   Spring Framework: é um framework do Spring que fornece recursos para o desenvolvimento de aplicativos Java.
    
-   Core Container: é uma parte do Spring Framework que fornece recursos para a injeção de dependência e inversão de controle.
    
-   Inversão de Controle: é um princípio de programação em que o controle da criação e gestão de objetos é transferido para um contêiner.
    
-   Injeção de Dependência: é um padrão de projeto de software em que as dependências são injetadas em uma classe em vez de serem criadas por ela.
    
-   Beans: são objetos gerenciados pelo contêiner do Spring que são criados e gerenciados por ele.
    
-   Métodos Produtores: são métodos que produzem beans gerenciados pelo contêiner do Spring.
    
-   Estereótipos do Spring: são anotações que ajudam a definir o papel de uma classe em um aplicativo Spring.
    
-   Spring Logging: é um recurso do Spring que permite a criação de logs de aplicativos. Ele usa o framework de log logback como padrão.
