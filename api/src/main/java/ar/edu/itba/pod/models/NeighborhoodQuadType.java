package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.util.Objects;

public class NeighborhoodQuadType extends NeighborhoodQuad {
    private String type;

    public NeighborhoodQuadType() {
        super();
    }

    public NeighborhoodQuadType(String neighborhood, int quadLat, int quadLon, String type) {
        super(neighborhood, quadLat, quadLon);
        this.type = type;
    }

    public String getType() { return type; }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        super.writeData(out);
        out.writeUTF(type);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        super.readData(in);
        type = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        NeighborhoodQuadType that = (NeighborhoodQuadType) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
