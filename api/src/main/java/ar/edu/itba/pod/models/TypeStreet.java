package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class TypeStreet implements DataSerializable {
    private String type;
    private String street;

    public TypeStreet() {}

    public TypeStreet(final Complaint complaint) {
        this.type = complaint.getType();
        this.street = complaint.getStreet();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TypeStreet that = (TypeStreet) o;
        return Objects.equals(type, that.type) && Objects.equals(street, that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, street);
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(type);
        objectDataOutput.writeUTF(street);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.type = objectDataInput.readUTF();
        this.street = objectDataInput.readUTF();
    }
}
