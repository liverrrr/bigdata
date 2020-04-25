package org.example.hadoop.top;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Order implements WritableComparable<Order> {

    private String user_id;
    private String name;
    private Integer price;

    public Order() {
    }

    public Order(String user_id, String name, Integer price) {
        this.user_id = user_id;
        this.name = name;
        this.price = price;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public int compareTo(Order o) {
//        int compare = this.user_id.compareTo(o.user_id);
//        if (compare==0){
//            return -this.price.compareTo(o.price);
//        }
//        return compare;
        return -this.price.compareTo(o.price);
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(user_id);
        out.writeUTF(name);
        out.writeInt(price);
    }

    public void readFields(DataInput in) throws IOException {
        this.user_id=in.readUTF();
        this.name=in.readUTF();
        this.price=in.readInt();
    }

    @Override
    public String toString() {
        return "Order{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
