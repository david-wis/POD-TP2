package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class TypeCount implements DataSerializable, Comparable<TypeCount> {
    private String type;
    private long count;

    public TypeCount(){
    }

    public TypeCount(final String type, final long count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public long getCount() {
        return count;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setCount(final long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeCount typeCount = (TypeCount) o;
        return count == typeCount.count && type.equals(typeCount.type);
    }

    @Override
    public int compareTo(TypeCount o) {
        int countComparison = Long.compare(this.count, o.count);
        if( countComparison != 0) {
            return countComparison;
        }
        return o.type.compareTo(type);
    }


    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(type);
        objectDataOutput.writeLong(count);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        this.type = objectDataInput.readUTF();
        this.count = objectDataInput.readLong();
    }


}
