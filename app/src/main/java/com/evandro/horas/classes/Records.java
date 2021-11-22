package com.evandro.horas.classes;

import java.util.List;

public class Records {
    List<Register> records;

    public List<Register> getRecords() {
        return records;
    }

    public void setRecords(Register register) {
        this.records.add(register);
    }

    @Override
    public String toString() {

        String all = "";

        for(Register r : this.records) {
            all = all + r.getDate() + ", ";
        }

        return all;
    }
}
