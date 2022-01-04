package com.example.redelcom_test.sqlite.model;

public class HistoricModel {
    private int id;
    private String text;
    private String result;
    private String checked;

    public HistoricModel() {}

    public HistoricModel(String text, String result, String checked){
        this.text = text;
        this.result = result;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}
