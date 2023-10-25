# Spring boot reactive web
 Demo project for Spring Reactive Web

### Tracing with Spring Reactive

[Issue](https://github.com/spring-projects/spring-boot/issues/33372)
[Solution](https://stackoverflow.com/questions/75221864/how-to-get-trace-id-in-spring-cloud-gateway-using-micrometer-brave)

### Reactive web client

1. [Library](https://betterprogramming.pub/feign-reactive-my-preferred-solution-for-rest-api-consumption-5d79a283b24f)
    1. [Spring boot 3 issue](https://github.com/PlaytikaOSS/feign-reactive/issues/534)
2. Http Exchange

### [Postgres reactive](https://hantsy.github.io/spring-reactive-sample/data/data-r2dbc.html)

### Downloading large file using WebClient

```java
//Write the file in parts and write each buffer to disk.
Flux<DataBuffer> dataBuffer = webClient
  .get()
  .uri("/largefiles/1")
  .retrieve()
  .bodyToFlux(DataBuffer.class);

DataBufferUtils.write(dataBuffer, destination,
    StandardOpenOption.CREATE)
    .share().block();
```

### Writing large to disk using Gateway filter

```java
final File file = new File(tmpDir + "/Del/" + uuidUtil.getId(GatewayUUIDType.FILE) + ".json");
file.getParentFile().mkdirs();  //Create directories if it does not exist.
final AsynchronousFileChannel channel = AsynchronousFileChannel.open(file.toPath(), CREATE_NEW, WRITE);
return DataBufferUtils.write(exchange.getRequest().getBody(), channel)
        .map(DataBufferUtils::release)
        .doOnComplete(() -> exchange.getAttributes().put(TEMP_FILENAME, file.getAbsolutePath()))
        .then(chain.filter(exchange));  //For blocking without using block()
```
