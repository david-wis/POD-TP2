package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class NeighborhoodQuadType implements DataSerializable {
    private String neighborhood;
    private int quadLat, quadLon;
    private String type;

    public NeighborhoodQuadType() {}

    public NeighborhoodQuadType(String neighborhood, int quadLat, int quadLon, String type) {
        this.neighborhood = neighborhood;
        this.quadLat = quadLat;
        this.quadLon = quadLon;
        this.type = type;
    }

    public String getNeighborhood() { return neighborhood; }
    public int getQuadLat() { return quadLat; }
    public int getQuadLon() { return quadLon; }
    public String getType() { return type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeighborhoodQuadType that = (NeighborhoodQuadType) o;
        return quadLat == that.quadLat &&
                quadLon == that.quadLon &&
                Objects.equals(neighborhood, that.neighborhood) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neighborhood, quadLat, quadLon, type);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(neighborhood);
        out.writeInt(quadLat);
        out.writeInt(quadLon);
        out.writeUTF(type);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        neighborhood = in.readUTF();
        quadLat = in.readInt();
        quadLon = in.readInt();
        type = in.readUTF();
    }
}
