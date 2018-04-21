[![Build Status](https://travis-ci.org/ehartung/sea-proxy.svg?branch=master)](https://travis-ci.org/ehartung/sea-proxy?branch=master)
[![Coverage Status](https://codecov.io/github/ehartung/sea-proxy/coverage.svg?branch=master)](https://codecov.io/github/ehartung/sea-proxy?branch=master)

# Sea Proxy

Sea Proxy is an HTTP reverse proxy which terminates OAuth2 and SSL. It can be used to secure any kind of resource that is communicating via HTTP and does not support OAuth2 natively.

## Features
- HTTP reverse proxy
- OAuth2 termination for resources
- SSL termination
- Configurable routing
- OAuth2 scopes per route

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

### Deploy to ECS wit Cloudcrane

1. Create ECS cluster (see https://github.com/ehartung/cloudcrane)
2. Deploy service into ECS cluster:

        $ cloudcrane service --application=sea-proxy --version=1 deploy
