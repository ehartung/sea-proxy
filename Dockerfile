FROM registry.opensource.zalan.do/stups/openjdk:1.8.0-131-4

COPY target/sea-proxy-0.0.1-SNAPSHOT.jar      /data/sea-proxy-0.0.1-SNAPSHOT.jar

WORKDIR /data

EXPOSE 7979 9000

CMD java -Dnetworkaddress.cache.ttl=60 $(java-dynamic-memory-opts) -jar sea-proxy-0.0.1-SNAPSHOT.jar
