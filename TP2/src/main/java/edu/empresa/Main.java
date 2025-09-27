package edu.empresa;


import edu.empresa.utils.DataDelete;
import edu.empresa.utils.DataLoader;

public class Main {
    public static void main(String[] args) {

        new DataDelete().deleteAll();
        System.out.println("------------------------------------");

        new DataLoader().loadData();
        System.out.println("------------------------------------");
    }
}