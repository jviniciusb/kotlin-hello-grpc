# kotlin-hello-grpc

A very simple sample gRPC server application written using Klotin.

## TLS

Command to create certificate files and test the application using TLS:
```shell
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes -subj '/CN=localhost'
```