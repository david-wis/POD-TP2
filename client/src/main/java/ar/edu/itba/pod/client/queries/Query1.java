package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.client.ClientUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Query1 {
    // TODO: Remove throws
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ClientUtils.run("TotalComplaintsByTypeAgency");
    }
}
