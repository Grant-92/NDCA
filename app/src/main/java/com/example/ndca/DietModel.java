package com.example.ndca;

class DietModel {

    private int image;
    private String header, descript;


    public DietModel(int image, String header, String descript) {
        this.image = image;
        this.header = header;
        this.descript = descript;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
