# go-rpc


## Go dependencies

protobuf-compiler

```bash
apt install -y protobuf-compiler
go install google.golang.org/protobuf/cmd/protoc-gen-go@v1.28
go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@v1.2
```

Go dependency:

```bash
go get google.golang.org/grpc
```

## Python Dependencies

```bash
python -m pip install grpcio-tools
pip install grpcio
```


## Generating code for Go


```bash
cd go-rpc
protoc --go_out=. --go_opt=paths=source_relative     --go-grpc_out=. --go-grpc_opt=paths=source_relative student/student.proto 
```

## Generating code for Python

```bash
cd go-rpc/python_client
python3 -m grpc_tools.protoc -I../student --python_out=. --pyi_out=. --grpc_python_out=. ../student/student.proto
```

## Notes on generated code


student.pb.go, which contains all the protocol buffer code to populate, serialize, and retrieve request and response message types.

student_grpc.pb.go, which contains the following:

+ An interface type (or stub) for clients to call with the methods defined in the Student service.

+ An interface type for servers to implement, also with the methods defined in the Student service.
