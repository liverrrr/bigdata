package org.example.hadoop.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Access implements WritableComparable<Access> {
    private String phone;
    private long up;
    private long down;
    private long sum;

    public Access(String phone, long up, long down) {
        this.phone = phone;
        this.up = up;
        this.down = down;
        this.sum = this.up + this.down;
    }

    public int compareTo(Access o) {
        return this.sum > o.getSum() ? -1 : 1;
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(phone);
        out.writeLong(up);
        out.writeLong(down);
        out.writeLong(sum);
    }

    public void readFields(DataInput in) throws IOException {
        this.phone = in.readUTF();
        this.up = in.readLong();
        this.down = in.readLong();
        this.sum = in.readLong();
    }

    public Access() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getUp() {
        return up;
    }

    public void setUp(long up) {
        this.up = up;
    }

    public long getDown() {
        return down;
    }

    public void setDown(long down) {
        this.down = down;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Access{" +
                "phone='" + phone + '\'' +
                ", up=" + up +
                ", down=" + down +
                ", sum=" + sum +
                '}';
    }

}
