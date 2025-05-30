package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Complaint implements DataSerializable {
    private String id;
    private String type;
    private String agency;

    public Complaint() {}

    public Complaint(String id, String type, String agency) {
        this.id = id;
        this.type = type;
        this.agency = agency;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAgency() {
        return agency;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(id);
        objectDataOutput.writeUTF(type);
        objectDataOutput.writeUTF(agency);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.id = objectDataInput.readUTF();
        this.type = objectDataInput.readUTF();
        this.agency = objectDataInput.readUTF();
    }
}
