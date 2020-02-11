package com.example.database_webserver_hanghoa;

public class HangHoa {
    private int idSP;
    private String tenSP;
    private int giaSP;
    private String anhSP;
    private String motaSP;

    public HangHoa(int idSP, String tenSP, int giaSP, String anhSP, String motaSP) {
        this.idSP = idSP;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.anhSP = anhSP;
        this.motaSP = motaSP;
    }

    public HangHoa(String tenSP, int giaSP, String anhSP, String motaSP) {
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.anhSP = anhSP;
        this.motaSP = motaSP;
    }

    public int getIdSP() {
        return idSP;
    }

    public void setIdSP(int idSP) {
        this.idSP = idSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(int giaSP) {
        this.giaSP = giaSP;
    }

    public String getAnhSP() {
        return anhSP;
    }

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
    }

    public String getMotaSP() {
        return motaSP;
    }

    public void setMotaSP(String motaSP) {
        this.motaSP = motaSP;
    }

    @Override
    public String toString() {
        return "HangHoa{" +
                "idSP=" + idSP +
                ", tenSP='" + tenSP + '\'' +
                ", giaSP=" + giaSP +
                ", anhSP='" + anhSP + '\'' +
                ", motaSP='" + motaSP + '\'' +
                '}';
    }
}
