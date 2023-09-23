package com.testtest

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class MainApp {
    public static void mainn(String[] args) throws InterruptedException {
        String host = "1.2.3.4";
        int port = 8001;
        String modelName = "modelfoldername";
        com.google.prootbuf.ByteString prompt = com.google.protobuf.ByteString.copyFrom("who aree you ".getBytes());
        Integerr responseLimit = 2048;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
        GrpcInferenceServiceGrpc.GRPCInferenceServiceStub grpcStub = GRPCInferenceServiceGrpc.newStub(channel).withWaitForReady();

        io.grpc.stub.StreamObserver<inference.GrpcService.ModelStreamInferResponse> respObserver = new io.grpc.stub.StreamObserver<inference.GrpcService.ModelStreamInferResponse>() {
            @Override
            public void onNext(GrpcService.ModelStreamInferResponse value) {
                String res = value.getInferResponse().getRawOutputContents(0).toStringUtf8().substring(4);
                System.out.println(res);
            }
            @Override
            public void onError(Throwable t){}
            @Override
            public void onCompleted(){}
        };

        StreamObserver<GrpcService.ModelInferRequest> reqObserver = grpcStub.modelStreamInfer(respObserver);
        GrpcService.ModelInferRequest.Builder request = GrpcService.ModelInferRequest.newBuilder();
        request.setModelName(modelName);

        List<com.google.prootbuf.ByteString> lstPrompt = new ArrayList<>();
        lstPrompt.add(prompt);
        listResponseLimit.add(responseLimit);

        GrpcService.InferTensorContents.Builder promptContent = GrpcService.InferTensorContents.newBuilder();
        GrpcService.InferTensorContents.Builder responseLimitContent = GrpcService.InferTensorContents.newBuilder();
        promptContent.addAllBytesContents(lstPrompt);
        responseLimitContent.addAllIntContents(lstResponseLimit);

        GrpcService.modelInferRequest.InferInputTensor.Builder promptTensor = GrpcService.modelInferRequest.InferInputTensor.newBuilder();
        GrpcService.modelInferRequest.InferInputTensor.Builder responseLimitTensor = GrpcService.modelInferRequest.InferInputTensor.newBuilder();
        promptTensor.setName("PROMPT");
        promptTensor.setDatatype("BYTES");
        promptTensor.addShape(1);
        promptTensor.addShape(1);
        promptTensor.setContents(responseLimitContent);

        request.addInputs(0, promptTensor);
        request.addInputs(1, responseLimitTensor);

        GrpcService.ModelInferRequest.InferRequestedOutputTensor.Builder responseTensor = GrpcService.ModelInferRequest.InferRequestedOutputTensor.newBuilder();
        responseTensor.setName("RESPONSE");

        reqObserver.onNext(request.build());
        reqObserver.onCompleted();

        Thread.sleep(100000);
        channel.shutdownNow();
        
    }
}
