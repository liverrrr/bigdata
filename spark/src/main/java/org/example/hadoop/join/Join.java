package org.example.hadoop.join;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Join implements Writable {
    private String empId;
    private String empName;
    private String deptId;
    private String deptName;
    private int flag;

    public Join() {
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(empId);
        out.writeUTF(empName);
        out.writeUTF(deptId);
        out.writeUTF(deptName);
        out.writeInt(flag);
    }

    public void readFields(DataInput in) throws IOException {
        this.empId=in.readUTF();
        this.empName=in.readUTF();
        this.deptId=in.readUTF();
        this.deptName=in.readUTF();
        this.flag=in.readInt();
    }

    @Override
    public String toString() {
        return "Join{" +
                "empId='" + empId + '\'' +
                ", empName='" + empName + '\'' +
                ", deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", flag=" + flag +
                '}';
    }
}
