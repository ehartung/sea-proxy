FROM registry.opensource.zalan.do/stups/openjdk:8u66-b17-1-12

COPY target/sea-proxy-0.0.1-SNAPSHOT.jar      /data/sea-proxy-0.0.1-SNAPSHOT.jar

WORKDIR /data

EXPOSE 7979 9000

CMD java -Dnetworkaddress.cache.ttl=60 $(java-dynamic-memory-opts) -jar sea-proxy-0.0.1-SNAPSHOT.jar
