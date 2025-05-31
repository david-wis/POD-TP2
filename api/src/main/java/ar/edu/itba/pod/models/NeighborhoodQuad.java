package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class NeighborhoodQuad implements DataSerializable {
    private String neighborhood;
    private int quadLat, quadLon;
//    private static final long serialVersionUID = 6529685098267757690L;

    public NeighborhoodQuad() {}

    public NeighborhoodQuad(String neighborhood, int quadLat, int quadLon) {
        this.neighborhood = neighborhood;
        this.quadLat = quadLat;
        this.quadLon = quadLon;
    }

    public String getNeighborhood() { return neighborhood; }
    public int getQuadLat() { return quadLat; }
    public int getQuadLon() { return quadLon; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeighborhoodQuad that = (NeighborhoodQuad) o;
        return quadLat == that.quadLat && quadLon == that.quadLon &&
                Objects.equals(neighborhood, that.neighborhood);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neighborhood, quadLat, quadLon);
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(neighborhood);
        objectDataOutput.writeInt(quadLat);
        objectDataOutput.writeInt(quadLon);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.neighborhood = objectDataInput.readUTF();
        this.quadLat = objectDataInput.readInt();
        this.quadLon = objectDataInput.readInt();
    }
}