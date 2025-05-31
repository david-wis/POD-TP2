package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class AgencyDate implements DataSerializable {
    private String agency;
    private int year;
    private int month;

    public AgencyDate() {}

    public AgencyDate(Complaint complaint, int month) {
        this.agency = complaint.getAgency();
        this.year = complaint.getYear();
        this.month = month;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AgencyDate that = (AgencyDate) o;
        return year == that.year && month == that.month && Objects.equals(agency, that.agency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, year, month);
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(agency);
        objectDataOutput.writeInt(year);
        objectDataOutput.writeInt(month);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.agency = objectDataInput.readUTF();
        this.year = objectDataInput.readInt();
        this.month = objectDataInput.readInt();
    }

    public String getAgency() {
        return agency;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}
