package com.humane.admin.etms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ChartJsResponse {

    private final List<String> labels;
    private final List<Dataset> datasets;

    public ChartJsResponse() {
        labels = new ArrayList<>();
        datasets = new ArrayList<>();
    }

    public void addLabel(String majorNm) {
        labels.add(majorNm);
    }

    public void addDataset(Dataset dataset) {
        datasets.add(dataset);
    }

    @ToString
    @Getter
    public static class Dataset {
        private List<Long> data;
        private String label;

        public Dataset() {
            data = new ArrayList<>();
        }

        public Dataset(String label){
            this();
            this.label = label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void addData(Long l) {
            data.add(l);
        }
    }
}