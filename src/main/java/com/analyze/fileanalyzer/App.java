package com.analyze.fileanalyzer;

import org.apache.tika.Tika;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Main mainWindow = new Main();
        mainWindow.createMainWindow();
    }}
