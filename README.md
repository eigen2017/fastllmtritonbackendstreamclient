this is the client to this backend:
https://github.com/eigen2017/fastllmtritonbackendstream/tree/main

java files in the inference package are auto generated grpc file:
1. get proto files from https://github.com/triton-inference-server/common/tree/main/protobuf
2. get protoc-22.3-linux-x86_64.zip and protoc-gen-grpc-java-1.57.2-linux-x86_64
3. mv protoc-gen-grpc-java to proto/tool/bin, that is folder of protoc
4. if you want change the package of proto gen java files, you need to add to proto file: add option java_package = "com.you.want";
5. protoc -I=. --java_out=. grpc_service.proto
6. protoc -I=. --java_out=. model_config.proto
7. protoc --plugin=/models/glm/proto/tool/bin/protoc-gen-grpc-java --grpc-java_out=. --proto_path=. grpc_service.proto


