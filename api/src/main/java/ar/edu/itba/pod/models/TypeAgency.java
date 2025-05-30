package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class TypeAgency implements DataSerializable {
    private String type;
    private String agency;

    public TypeAgency(final Complaint complaint) {
        this.type = complaint.getType();
        this.agency = complaint.getAgency();
    }

    public TypeAgency(String type, String agency) {
        this.type = type;
        this.agency = agency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TypeAgency that = (TypeAgency) o;
        return Objects.equals(type, that.type) && Objects.equals(agency, that.agency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, agency);
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(type);
        objectDataOutput.writeUTF(agency);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.type = objectDataInput.readUTF();
        this.agency = objectDataInput.readUTF();
    }
}
