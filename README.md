# Sea Proxy

## Deployment
### Build the project

        $ mvn3 clean package
        $ docker build -t <tag> .

### Check that Docker image works

        $ docker run -p 9000:9000 -p 7979:7979 -it <tag>

### Deploy to Docker registry and on AWS

        $ pierone login
        $ docker push <tag>
        $ mai login
        $ senza create senza.yaml blue ApplicationId=sea-proxy DockerImage=<tag> ImageVersion=1.0.x MintBucket=<mint-bucket> ScalyrAccountKey=<scalyr-account-key>

