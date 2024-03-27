

```bash

apt install -y protobuf-compiler
go install google.golang.org/protobuf/cmd/protoc-gen-go@v1.28
go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@v1.2
go get google.golang.org/grpc
protoc --go_out=. --go_opt=paths=source_relative     --go-grpc_out=.   --go-grpc_opt=paths=source_relative     student/student.proto 

```

Some Notes:

route_guide.pb.go, which contains all the protocol buffer code to populate, serialize, and retrieve request and response message types.
route_guide_grpc.pb.go, which contains the following:

An interface type (or stub) for clients to call with the methods defined in the RouteGuide service.

An interface type for servers to implement, also with the methods defined in the RouteGuide service.