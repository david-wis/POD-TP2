package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

public class Complaint implements DataSerializable {
    private String id;
    private String neighborhood;
    private float longitude;
    private float latitude;
    private YearMonth date;
    private String street;
    private boolean open;

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

    public String getStreet() {
        return street;
    }

    public YearMonth getDate() {
        return date;
    }

    public int getMonth() {
        return date.getMonthValue();
    }

    public int getYear() {
        return date.getYear();
    }

    public boolean isOpen() {
        return open;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }


    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(id);
        objectDataOutput.writeUTF(type);
        objectDataOutput.writeUTF(agency);
        objectDataOutput.writeUTF(neighborhood);
        objectDataOutput.writeFloat(longitude);
        objectDataOutput.writeFloat(latitude);
        objectDataOutput.writeObject(date);
        objectDataOutput.writeUTF(street);
        objectDataOutput.writeBoolean(open);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.id = objectDataInput.readUTF();
        this.type = objectDataInput.readUTF();
        this.agency = objectDataInput.readUTF();
        this.neighborhood = objectDataInput.readUTF();
        this.longitude = objectDataInput.readFloat();
        this.latitude = objectDataInput.readFloat();
        this.date = objectDataInput.readObject();
        this.street = objectDataInput.readUTF();
        this.open = objectDataInput.readBoolean();
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

        public ComplaintBuilder setDate(YearMonth date) {
            this.complaint.date = date;
            return this;
        }

        public ComplaintBuilder setStreet(String street) {
            this.complaint.street = street;
            return this;
        }

        public ComplaintBuilder setOpen(boolean open) {
            this.complaint.open = open;
            return this;
        }
    }
}
