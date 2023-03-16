
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

1.  Event Driven - é um modelo de programação onde a comunicação entre componentes é baseada em eventos, onde um componente dispara um evento que é recebido e processado por outro componente.
2.  Event Notification Pattern - é um padrão de design onde um objeto registra-se para receber notificações quando um evento ocorre.
3.  Event Carried State Transfer Pattern - é um padrão de design onde o estado do objeto é incluído no evento, permitindo que outros componentes recuperem o estado sem acessar diretamente o objeto.
4.  API Gateway Pattern - é um padrão de design onde um único ponto de entrada é usado para acessar várias APIs. O gateway pode executar tarefas como autenticação, autorização, balanceamento de carga e caching.
5.  API Composition Pattern - é um padrão de design onde várias APIs são combinadas para criar uma API unificada.
6.  SAGA Pattern - é um padrão de design para controlar transações distribuídas em sistemas distribuídos.
7.  Cross-Cutting - é um termo usado para descrever funcionalidades que são comuns a várias partes de um sistema, como autenticação e autorização.
8.  Service Registry Discovery - é um padrão de design onde um registro central de serviços é mantido e consultado para descobrir serviços disponíveis em um sistema distribuído.
9.  Global Configuration - é um padrão de design onde as configurações do sistema são armazenadas em um local central e compartilhadas por todos os componentes do sistema.
10.  Circuit Breaker - é um padrão de design para lidar com falhas em sistemas distribuídos, onde um componente verifica se um serviço está disponível e, se não estiver, o circuit breaker interrompe temporariamente o acesso ao serviço.
11.  Retry - é um padrão de design onde uma operação falha, mas é tentada novamente em um período de tempo determinado.
12.  Log Aggregation - é um padrão de design onde logs de vários componentes de um sistema são coletados e agregados em um local central.
13.  Controle de Métricas - é um padrão de design onde as métricas de desempenho são coletadas e monitoradas para detectar possíveis problemas no sistema.
14.  Padrão JWT Autenticação - é um padrão de design de autenticação baseado em tokens JWT (JSON Web Tokens).
15.  Modelos Autorização - são modelos de autorização usados para controlar o acesso a recursos em sistemas distribuídos.
16.  Database per Service - é um padrão de design onde cada serviço possui seu próprio banco de dados.
17.  Shared Database - é um padrão de design onde vários serviços compartilham o mesmo banco de dados.
18.  API RESTful - é um estilo arquitetural para sistemas distribuídos que utiliza o protocolo HTTP e a abordagem CRUD para expor recursos.
19.  Maturidade de Richardson - é um modelo que descreve níveis de maturidade para implementação de APIs RESTful.
20.  Padrões de Datas - são padrões de design para trabalhar com datas em sistemas distribuídos.
21.  ISO 8601-UTC - é um padrão de formato de data e hora.
22.  Comunicação por Coreografia - é um padrão de design onde os componentes de um sistema colaboram para executar uma tarefa, sem a necessidade de um coordenador central.
23.  Padrão JWT Autenticação: O padrão JWT (JSON Web Token) é uma forma segura de transmitir informações entre duas partes. É utilizado principalmente para autenticação e autorização. O JWT é um token autocontido que contém informações sobre o usuário, como nome e permissões. O token é assinado digitalmente e é enviado ao servidor em cada solicitação. O servidor pode então verificar a assinatura para garantir que o token é válido e que as informações do usuário são confiáveis.
24.  Modelos Autorização: O modelo de autorização é uma técnica utilizada para controlar o acesso a recursos em um sistema. Existem vários modelos de autorização, como o modelo baseado em funções, o modelo baseado em políticas e o modelo baseado em atributos. Cada um desses modelos oferece diferentes níveis de granularidade para o controle de acesso.
25.  Broker Pattern: O padrão Broker é um padrão arquitetural utilizado para implementar sistemas distribuídos. O padrão utiliza um intermediário (broker) para controlar e coordenar a comunicação entre os diferentes componentes do sistema. O broker pode ser responsável por várias tarefas, como roteamento de mensagens, transformação de dados e gerenciamento de filas.
    
26.  Mediator Pattern: O padrão Mediator é um padrão comportamental que permite que os objetos se comuniquem entre si sem se acoplarem diretamente. Nesse padrão, um objeto atua como intermediário entre outros objetos, facilitando a comunicação entre eles. O padrão é útil para sistemas complexos, onde muitos objetos precisam se comunicar entre si.
    
27.  Access Token Pattern: O padrão Access Token é uma técnica utilizada para controlar o acesso a recursos em um sistema distribuído. Quando um usuário faz login em um sistema, ele recebe um access token que é usado para autenticar solicitações posteriores. O token pode conter informações sobre as permissões do usuário e pode ser verificado em cada solicitação para garantir que o usuário tenha acesso aos recursos solicitados.

