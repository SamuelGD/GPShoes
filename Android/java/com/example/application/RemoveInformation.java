package com.example.application;

import java.util.ArrayList;


public class RemoveInformation {
    private static final String partie1 = "\\";
    private static final String partie2 = "/";
    private static final String partie3 = "u003c";
    private static final String partie4 = "u003e";
    private static final String partie5 = "b";

    private static final String comparateur1 = partie1 + partie3 + partie5 + partie1 + partie4;
    private static final String comparateur2 = partie1 + partie3 + partie2 + partie5 + partie1 + partie4;

    public static ArrayList<String> getinfo(ArrayList<String> data) {
        for (int i = 0; i < data.size(); i++) {
            data.set(i, data.get(i).replace("\u003cb\u003e", ""));
            data.set(i, data.get(i).replace("\u003c/b\u003e", ""));
            data.set(i, data.get(i).replaceAll("\u003cdiv style=\"font-size:0.9em\"\u003e", " "));
            data.set(i, data.get(i).replace("\u003c/div\u003e", ""));
        }
        return data;
    }
}