package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Date;

public class Complaint implements DataSerializable {
    private String id;
    private String neighborhood;
    private float longitude;
    private float latitude;
    private Date date;
    private String street;

    private String type;
    private String agency;

    public Complaint() {}

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

    public static class ComplaintBuilder {
        private final Complaint complaint;

        public Complaint build() {
            return this.complaint;
        }


        public ComplaintBuilder() {
            this.complaint = new Complaint();
        }

        public ComplaintBuilder setId(String id) {
            this.complaint.id = id;
            return this;
        }

        public ComplaintBuilder setType(String type) {
            this.complaint.type = type;
            return this;
        }

        public ComplaintBuilder setAgency(String agency) {
            this.complaint.agency = agency;
            return this;
        }

        public ComplaintBuilder setNeighborhood(String neighborhood) {
            this.complaint.neighborhood = neighborhood;
            return this;
        }

        public ComplaintBuilder setLongitude(float longitude) {
            this.complaint.longitude = longitude;
            return this;
        }

        public ComplaintBuilder setLatitude(float latitude) {
            this.complaint.latitude = latitude;
            return this;
        }

        public ComplaintBuilder setDate(Date date) {
            this.complaint.date = date;
            return this;
        }

        public ComplaintBuilder setStreet(String street) {
            this.complaint.street = street;
            return this;
        }
    }
}
