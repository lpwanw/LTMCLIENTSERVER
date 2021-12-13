package com.csm.model;

import com.csm.systeminfo.FileStores;

public class DiskModel {
    private String name;
    private Double used;
    private Double available;
    private Double total;
    private String percent;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getUsed() {
        return used;
    }

    public void setUsed(Double used) {
        this.used = used;
    }

    public Double getAvailable() {
        return available;
    }

    public void setAvailable(Double available) {
        this.available = available;
    }

    public String getPercent() {
        return String.format("%.2f",(this.used*100)/(this.total));
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
