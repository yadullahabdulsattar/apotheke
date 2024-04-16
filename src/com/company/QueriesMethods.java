package com.company;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class QueriesMethods {
    static ResultSet connection (String query) {
        ResultSet resultSet = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/apotheke", "root", "password");
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    static PreparedStatement updateConnection (String query) {
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/apotheke", "root", "password");
            Statement statement = connection.createStatement();
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void testQueries () {
        clearScreen();
        try {
            //Test
            String query = "select * from medikament";
            ResultSet resultSet = connection(query);
            if(resultSet != null) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("name") + ' ' + resultSet.getString("lager_id"));
                }
            }
            System.out.println("------------------------------------");

            // Anfragen in Anfragen
            query = "select * from medikament where medikament_typ_id = (select id from medikament_typ where name = 'Tropfen');";
            resultSet = connection(query);
            int count = 0;
            while (resultSet.next()) {
                count++;
                System.out.println(count + " " + resultSet.getString("name"));
            }
            System.out.println("------------------------------------");

            // GROUP BY
            query = "select count(*) as total from medikament where medikament_typ_id = (select id from medikament_typ where id = 1) group by medikament_typ_id;";
            resultSet = connection(query);
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("total"));
            }
            System.out.println("------------------------------------");

            // INNER JOIN
            query = "select m.name as n, l.einheiten as e from medikament as m inner join lager as l on m.lager_id = l.id;";
            resultSet = connection(query);
            int count1 = 0;
            while (resultSet.next()) {
                count1++;
                System.out.println(count1 + " " + resultSet.getString("n") + " " + resultSet.getString("e"));
            }
            System.out.println("------------------------------------");

            // LEFT INNER JOIN
            query = "select m.name as n, h.name as e from medikament as m right join hersteller h on m.hersteller_id = h.id where h.id in (1,5);";
            resultSet = connection(query);
            int count2 = 0;
            while (resultSet.next()) {
                count2++;
                System.out.println(count2 + " " + resultSet.getString("n") + " " + resultSet.getString("e"));
            }
            System.out.println("------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void getInventory () {
        clearScreen();
        Scanner scan = new Scanner(System.in);
        int digit = -1;
        while (digit != 0) {
            System.out.println("Choose the digit from following menu:" +
                    "\n1- The list of all the medicines." +
                    "\n2- The list of all the companies." +
                    "\n3- The list of all medicine types." +
                    "\n4- The list of all the medicine with complete info." +
                    "\n0- Exit the system.\n");
            if (scan.hasNextInt()){
                digit = scan.nextInt();
            } else {
                System.out.println("Invalid input!");
            }

            switch (digit) {
                case 1:
                    listMedicines();
                    break;
                case 2:
                    listCompanies();
                    break;
                case 3:
                    listMedicineTypes();
                    break;
                case 4:
                    getMedicineInfo();
                    break;
                default:
                    System.out.println("You have exited the program!");
            }
        }
    }

    static void listMedicines () {
        clearScreen();
        Medikament medikament = new Medikament();
        try {
            String query = "select * from medikament";
            ResultSet resultSet = connection(query);
            int count = 0;
            if (resultSet != null) {
                while (resultSet.next()){
                    count++;
                    medikament.setName(resultSet.getString("name"));
                    System.out.println(count + "- Medikament Name: " + medikament.getName());
                }
            }
            System.out.println("------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void listCompanies () {
        clearScreen();
        Hersteller hersteller = new Hersteller();
        try {
            String query = "select * from hersteller";
            ResultSet resultSet = connection(query);
            int count = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    count ++;
                    hersteller.setName(resultSet.getString("name"));
                    hersteller.setPlz(resultSet.getInt("plz"));
                    hersteller.setOrt(resultSet.getString("ort"));
                    System.out.println(count + "- Name: " + hersteller.getName() + ", Adresse: " + hersteller.getPlz() + ", " + hersteller.getOrt());
                }
            }
            System.out.println("------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void listMedicineTypes () {
        clearScreen();
        Medikament_Typ medikament_typ = new Medikament_Typ();
        try {
            String query = "select * from medikament_typ";
            ResultSet resultSet = connection(query);
            int count = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    count ++;
                    medikament_typ.setName(resultSet.getString("name"));
                    System.out.println(count + "- Name: " + medikament_typ.getName());
                }
            }
            System.out.println("------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void getMedicineInfo () {
        clearScreen();
        Medikament medikament = new Medikament();
        Warenbestand warenbestand = new Warenbestand();
        try {
            String query = "select m.name as mn, h.name hn, l.id as li, l.einheiten as le, w.verkaufspreis as wv from medikament as m " +
                    "inner join hersteller as h on h.id = m.hersteller_id " +
                    "inner join lager as l on l.id = m.lager_id " +
                    "inner join warenbestand as w on w.medikament_id = m.id ";
            ResultSet resultSet = connection(query);
            int count = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    count++;
                    medikament.setName(resultSet.getString("mn"));
                    medikament.getHersteller().setName(resultSet.getString("hn"));
                    medikament.getLager().setId(resultSet.getInt("li"));
                    medikament.getLager().setEinheiten(resultSet.getInt("le"));
                    warenbestand.setVerkaufspreis(resultSet.getDouble("wv"));
                    System.out.println(count + "- Medikament: " + medikament.getName() + ", Hersteller: " +
                        medikament.getHersteller().getName() + ", Fach: " + medikament.getLager().getId() + ", Einheiten: " +
                        medikament.getLager().getEinheiten() + ", Verkaufspreis: € " + warenbestand.getVerkaufspreis());
                }
            }
            System.out.println("------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void updateInventory () {
        clearScreen();
        Scanner scan = new Scanner(System.in);
        int digit = -1;
        while (digit != 0) {
            System.out.println("Choose the digit from following menu:" +
                    "\n1- Update Company." +
                    "\n2- Update Medicine Type." +
                    "\n3- Update Medicine." +
                    "\n0- Exit the system.\n");
            if (scan.hasNextInt()){
                digit = scan.nextInt();
            } else {
                System.out.println("Invalid input!");
            }

            switch (digit) {
                case 1:
                    updateCompany();
                    break;
                case 2:
                    updateMedicineType();
                    break;
                case 3:
                    updateMedicine();
                    break;
                default:
                    System.out.println("You have exited the program!");
            }
        }
    }

    static void updateCompany () {
        clearScreen();
        listCompanies();
        Hersteller hersteller = new Hersteller();
        int digit = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the digit of the corresponding task: " +
                "\n1- Add company" +
                "\n2- Change Info of a company" +
                "\n3- Delete" +
                "\n0- Return");
        if (scan.hasNext()) {
            digit = scan.nextInt();
            if (digit == 1) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Please enter the name of the company: ");
                    if (scan.hasNext()) {
                        hersteller.setName(scan.next());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    scan = new Scanner(System.in);
                    System.out.println("Please enter the plz of the company: ");
                    if (scan.hasNextInt()) {
                            hersteller.setPlz(scan.nextInt());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    scan = new Scanner(System.in);
                    System.out.println("Please insert the ort of the company: ");
                    if (scan.hasNext()) {
                        hersteller.setOrt(scan.next());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    if (hersteller.getName() != null && hersteller.getPlz() > 0 && hersteller.getOrt() != null) {
                        query = "insert into hersteller (name, plz, ort) values (?, ?, ?)";
                        PreparedStatement preparedStatement = updateConnection(query);
                        preparedStatement.setString(1, hersteller.getName());
                        preparedStatement.setInt(2, hersteller.getPlz());
                        preparedStatement.setString(3, hersteller.getOrt());
                        preparedStatement.execute();
                        System.out.println(hersteller.getName() + " has been added!");
                    } else {
                        System.out.println("Invalid entry!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (digit == 2) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Which company info you want to change? ");
                    if (scan.hasNext()) {
                        hersteller.setName(scan.next());
                        query = "select * from hersteller where name like " + "\"%" + hersteller.getName() + "%\"" + ";";
                        ResultSet resultSet = connection(query);
                        while (resultSet.next()) {
                            hersteller.setId(resultSet.getInt("id"));
                            hersteller.setName(resultSet.getString("name"));
                            hersteller.setPlz(resultSet.getInt("plz"));
                            hersteller.setOrt(resultSet.getString("ort"));
                        }
                        scan = new Scanner(System.in);
                        System.out.println("Please enter the digit of the corresponding task: " +
                                "\n1- Change name" +
                                "\n2- Change plz" +
                                "\n3- Change ort" +
                                "\n0- Return");
                        if (scan.hasNext()){
                            digit = scan.nextInt();
                            if (digit == 1) {
                                scan = new Scanner(System.in);
                                System.out.println("Please enter the new name of the company: ");
                                if (scan.hasNext()) { //check for \n
                                    hersteller.setName(scan.next());
                                }
                            } else if (digit == 2) {
                                scan = new Scanner(System.in);
                                System.out.println("Please enter the new plz of the company: ");
                                if (scan.hasNextInt()) {
                                    hersteller.setPlz(scan.nextInt());
                                }
                            } else if (digit == 3) {
                                scan = new Scanner(System.in);
                                System.out.println("Please insert the new ort of the company: ");
                                if (scan.hasNext()) {
                                    hersteller.setOrt(scan.next());
                                }
                            } else {
                                System.out.println("Invalid entry!");
                            }
                        }
                        if (hersteller.getName() != null && hersteller.getPlz() > 0 && hersteller.getOrt() != null) {
                            query = "update hersteller set name = ?, plz = ?, ort = ? where id = ?;";
                            PreparedStatement preparedStatement = updateConnection(query);
                            preparedStatement.setString(1, hersteller.getName());
                            preparedStatement.setInt(2, hersteller.getPlz());
                            preparedStatement.setString(3, hersteller.getOrt());
                            preparedStatement.setInt(4, hersteller.getId());
                            preparedStatement.executeUpdate();
                            System.out.println(hersteller.getName() + " has been updated!");
                        } else {
                            System.out.println("Invalid entry!");
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (digit == 3) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Which company you want to delete? ");
                    if (scan.hasNext()) {
                        hersteller.setName(scan.next());
                        query = "select * from hersteller where name like " + "\"%" + hersteller.getName() + "%\"" + ";";
                        ResultSet resultSet = connection(query);
                        while (resultSet.next()) {
                            hersteller.setId(resultSet.getInt("id"));
                        }
                        if (hersteller.getId() > 0) {
                            query = "delete from hersteller where id = ?;";
                            PreparedStatement preparedStatement = updateConnection(query);
                            preparedStatement.setInt(1, hersteller.getId());
                            preparedStatement.execute();
                            System.out.println(hersteller.getName() + " has been deleted!");
                        } else {
                            System.out.println("Invalid company name!");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("This company cannot be deleted because some products are still available.");
                }
            } else {
                System.out.println("You have exited!");
            }
        }
    }
    static void updateMedicineType () {
        clearScreen();
        listMedicineTypes();
        Medikament_Typ medikament_typ = new Medikament_Typ();
        int digit = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the digit of the corresponding task: " +
                "\n1- Add medicine type" +
                "\n2- Change info of a medicine type" +
                "\n3- Delete" +
                "\n0- Return");
        if (scan.hasNext()) {
            digit = scan.nextInt();
            if (digit == 1) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Please enter the name of the medicine type: ");
                    if (scan.hasNext()) {
                        medikament_typ.setName(scan.next());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    if (medikament_typ.getName() != null) {
                        query = "insert into medikament_typ (name) values (?)";
                        PreparedStatement preparedStatement = updateConnection(query);
                        preparedStatement.setString(1, medikament_typ.getName());
                        preparedStatement.execute();
                        System.out.println(medikament_typ.getName() + " has been added!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (digit == 2) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Which medicine type info you want to change? ");
                    if (scan.hasNext()) {
                        medikament_typ.setName(scan.next());
                        query = "select * from medikament_typ where name like " + "\"%" + medikament_typ.getName() + "%\"" + ";";
                        ResultSet resultSet = connection(query);
                        while (resultSet.next()) {
                            medikament_typ.setId(resultSet.getInt("id"));
                            medikament_typ.setName(resultSet.getString("name"));
                        }
                        scan = new Scanner(System.in);
                        System.out.println("Please enter the new name of the medicine type: ");
                        if (scan.hasNext()) { //check for \n
                            medikament_typ.setName(scan.next());
                        }
                    }
                    query = "update medikament_typ set name = ? where id = ?;";
                    PreparedStatement preparedStatement = updateConnection(query);
                    preparedStatement.setString(1, medikament_typ.getName());
                    preparedStatement.setInt(2, medikament_typ.getId());
                    preparedStatement.executeUpdate();
                    System.out.println(medikament_typ.getName() + " has been updated!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (digit == 3) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Which medicine type you want to delete? ");
                    if (scan.hasNext()) {
                        medikament_typ.setName(scan.next());
                        query = "select * from medikament_typ where name like " + "\"%" + medikament_typ.getName() + "%\"" + ";";
                        ResultSet resultSet = connection(query);
                        while (resultSet.next()) {
                            medikament_typ.setId(resultSet.getInt("id"));
                        }
                        if (medikament_typ.getId() > 0) {
                            query = "delete from medikament_typ where id = ?;";
                            PreparedStatement preparedStatement = updateConnection(query);
                            preparedStatement.setInt(1, medikament_typ.getId());
                            preparedStatement.execute();
                            System.out.println(medikament_typ.getName() + " has been deleted!");
                        } else {
                            System.out.println("Invalid medicine type!");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("This medicine type cannot be deleted because some medicines still exist with this medicine type.");
                }
            } else {
                System.out.println("Invalid entry!");
            }
        }
    }
    static void updateMedicine () {
        clearScreen();
        getMedicineInfo();
        Medikament medikament = new Medikament();
        Warenbestand warenbestand = new Warenbestand();
        int digit = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the digit of the corresponding task: " +
                "\n1- Add Medicine" +
                "\n2- Change info of a medicine" +
                "\n3- Delete medicine" +
                "\n0- Return");
        if (scan.hasNext()) {
            digit = scan.nextInt();
            if (digit == 1) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Please enter the name of the medicine: ");
                    if (scan.hasNext()) {
                        medikament.setName(scan.next());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    scan = new Scanner(System.in);
                    System.out.println("Please enter the type of the medicine: ");
                    if (scan.hasNext()) {
                        medikament.getMedikament_typ().setName(scan.next());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    scan = new Scanner(System.in);
                    System.out.println("Please insert the name of the company: ");
                    if (scan.hasNext()) {
                        medikament.getHersteller().setName(scan.next());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    scan = new Scanner(System.in);
                    System.out.println("Please enter the units of the medicine: ");
                    if (scan.hasNextInt()) {
                        medikament.getLager().setEinheiten(scan.nextInt());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    scan = new Scanner(System.in);
                    System.out.println("Please insert the expiry of the medicine in form YYYY-MM-DD: ");
                    String date = scan.next();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    Date expiry = null;
                    try {
                        //Parsing the String
                        expiry = dateFormat.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    warenbestand.setAblauf_datum(expiry);
                    scan = new Scanner(System.in).useLocale(Locale.US);
                    System.out.println("Please insert the selling price of the medicine: ");
                    if (scan.hasNext()) {
                        warenbestand.setVerkaufspreis(scan.nextDouble());
                    } else {
                        System.out.println("Invalid entry!");
                    }
                    if (medikament.getName() != null && medikament.getMedikament_typ().getName() != null && medikament.getHersteller().getName() != null && warenbestand.getAblauf_datum() != null && warenbestand.getVerkaufspreis() > 0.0) {
                        boolean check = true;
                        ResultSet resultSet = null;
                        resultSet = connection("select * from medikament_typ where name like " + "\"%" + medikament.getMedikament_typ().getName() + "%\"" + ";");
                        if(!resultSet.next()) {
                            check = false;
                            System.out.println("Medicine type doesn't exist!");
                        } else {
                            medikament.getMedikament_typ().setId(resultSet.getInt("id"));
                        }
                        resultSet = connection("select * from hersteller where name like " + "\"%" + medikament.getHersteller().getName() + "%\"" + ";");
                        if(!resultSet.next()) {
                            check = false;
                            System.out.println("Company doesn't exist!");
                        } else {
                            medikament.getHersteller().setId(resultSet.getInt("id"));
                        }
                        resultSet = connection("select * from medikament where name like " + "\"%" + medikament.getName() + "%\"" + ";");
                        if(resultSet.next() && medikament.getName() == resultSet.getString("name")) {
                            check = false;
                            System.out.println("Medicine already exist!");
                        }
                        if(check && medikament.getLager().getEinheiten() > 0) {
                            query = "insert into lager (einheiten) values (?)";
                            PreparedStatement preparedStatement = null;
                            try {
                                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/apotheke", "root", "password");
                                Statement statement = connection.createStatement();

                                preparedStatement = connection.prepareStatement(query, statement.RETURN_GENERATED_KEYS);
                                preparedStatement.setInt(1, medikament.getLager().getEinheiten());
                                preparedStatement.execute();
                                ResultSet newId = preparedStatement.getGeneratedKeys();
                                if(newId.next()) {
                                    medikament.getLager().setId(newId.getInt(1));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            query = "insert into medikament (name, medikament_typ_id, hersteller_id, lager_id) values (?, ?, ?, ?)";
                            try {
                                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/apotheke", "root", "password");
                                Statement statement = connection.createStatement();
                                preparedStatement = connection.prepareStatement(query, statement.RETURN_GENERATED_KEYS);
                                preparedStatement.setString(1, medikament.getName());
                                preparedStatement.setInt(2, medikament.getMedikament_typ().getId());
                                preparedStatement.setInt(3, medikament.getHersteller().getId());
                                preparedStatement.setInt(4, medikament.getLager().getId());
                                preparedStatement.execute();
                                ResultSet newId = preparedStatement.getGeneratedKeys();
                                if (newId.next()) {
                                    medikament.setId(newId.getInt(1));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            query = "insert into warenbestand (medikament_id, ablauf_datum, verkaufspreis) values (?, ?, ?)";
                            preparedStatement = updateConnection(query);
                            preparedStatement.setInt(1, medikament.getId());
                            preparedStatement.setDate(2,new java.sql.Date(warenbestand.getAblauf_datum().getTime()));
                            preparedStatement.setDouble(3, warenbestand.getVerkaufspreis());
                            preparedStatement.execute();
                            System.out.println(medikament.getName() + " has been added!");
                        } else {
                            System.out.println("Unable to add medicine!");
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (digit == 2) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Which medicine info you want to change? ");
                    if (scan.hasNext()) {
                        medikament.setName(scan.next());
                        query = "select * from medikament where name like " + "\"%" + medikament.getName() + "%\"" + ";";
                        ResultSet resultSet = connection(query);
                        while (resultSet.next()) {
                            medikament.setId(resultSet.getInt("id"));
                            medikament.setName(resultSet.getString("name"));
                            medikament.getMedikament_typ().setId(resultSet.getInt("medikament_typ_id"));
                            medikament.getHersteller().setId(resultSet.getInt("hersteller_id"));
                            medikament.getLager().setId(resultSet.getInt("lager_id"));
                        }
                        query = "select * from warenbestand where medikament_id = " + medikament.getId() + ";";
                        resultSet = connection(query);
                        while (resultSet.next()){
                            warenbestand.getMedikament().setId(medikament.getId());
                            warenbestand.setAblauf_datum(resultSet.getDate("ablauf_datum"));
                            warenbestand.setVerkaufspreis(resultSet.getDouble("verkaufspreis"));
                        }
                        scan = new Scanner(System.in);
                        System.out.println("Please enter the digit of the corresponding task: " +
                                "\n1- Change medicine name" +
                                "\n2- Change medicine type" +
                                "\n3- Change medicine company" +
                                "\n4- Change the lager position" +
                                "\n5- Change the units available" +
                                "\n6- Change the medicine expiry date" +
                                "\n7- Change the selling price" +
                                "\n0- Return");
                        if (scan.hasNext()){
                            digit = scan.nextInt();
                            if (digit == 1) {
                                scan = new Scanner(System.in);
                                System.out.println("Please enter the new name of the medicine: ");
                                if (scan.hasNext()) { //check for \n
                                    medikament.setName(scan.next());
                                }
                            } else if (digit == 2) {
                                scan = new Scanner(System.in);
                                System.out.println("Please enter the new medicine type: ");
                                if (scan.hasNext()) {
                                    medikament.getMedikament_typ().setName(scan.next());
                                    query = "select * from medikament_typ where name like " + "\"%" + medikament.getMedikament_typ().getName() + "%\"" + ";";
                                    resultSet = connection(query);
                                    if (resultSet.next()){
                                        medikament.getMedikament_typ().setId(resultSet.getInt("id"));
                                    }
                                }
                            } else if (digit == 3) {
                                scan = new Scanner(System.in);
                                System.out.println("Please enter the new name of the company: ");
                                if (scan.hasNext()) {
                                    medikament.getHersteller().setName(scan.next());
                                    query = "select * from hersteller where name like " + "\"%" + medikament.getHersteller().getName() + "%\"" + ";";
                                    resultSet = connection(query);
                                    if (resultSet.next()){
                                        medikament.getHersteller().setId(resultSet.getInt("id"));
                                    }
                                }
                            } else if (digit == 4) {
                                scan = new Scanner(System.in);
                                System.out.println("Please enter the new position of the medicine: ");
                                if (scan.hasNextInt()) {
                                    medikament.getLager().setId(scan.nextInt());
                                }
                            }else if (digit == 5) {
                                scan = new Scanner(System.in);
                                System.out.println("Please enter the new value of units available: ");
                                if (scan.hasNextInt()) {
                                    medikament.getLager().setEinheiten(scan.nextInt());
                                }
                            } else if (digit == 6) {
                                scan = new Scanner(System.in);
                                System.out.println("Please enter the new expiry date: ");
                                if (scan.hasNext()) {
                                    String date = scan.next();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                    Date expiry = null;
                                    try {
                                        //Parsing the String
                                        expiry = dateFormat.parse(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    warenbestand.setAblauf_datum(expiry);
                                }
                            }else if (digit == 7) {
                                scan = new Scanner(System.in).useLocale(Locale.US);
                                System.out.println("Please enter the new selling price: ");
                                if (scan.hasNext()) {
                                    warenbestand.setVerkaufspreis(scan.nextDouble());
                                }
                            } else {
                                System.out.println("Invalid entry!");
                            }
                        }
                        if (digit > 0 && digit < 5) {
                            query = "update medikament set name = ?, medikament_typ_id = ?, hersteller_id = ?, lager_id = ? where id = ?;";
                            PreparedStatement preparedStatement = updateConnection(query);
                            preparedStatement.setString(1, medikament.getName());
                            preparedStatement.setInt(2, medikament.getMedikament_typ().getId());
                            preparedStatement.setInt(3, medikament.getHersteller().getId());
                            preparedStatement.setInt(4, medikament.getLager().getId());
                            preparedStatement.setInt(5, medikament.getId());
                            preparedStatement.executeUpdate();
                            System.out.println(medikament.getName() + " has been updated!");
                        }else if (digit == 5 && medikament.getLager().getEinheiten() > 0){
                            query = "update lager set einheiten = ? where id = ?;";
                            PreparedStatement preparedStatement = updateConnection(query);
                            preparedStatement.setInt(1, medikament.getLager().getEinheiten());
                            preparedStatement.setInt(2, medikament.getLager().getId());
                            preparedStatement.executeUpdate();
                            System.out.println(medikament.getName() + " has been updated!");
                        } else if ((digit == 6 || digit == 7) && warenbestand.getVerkaufspreis() > 0.00){
                            query = "update warenbestand set ablauf_datum = ?, verkaufspreis = ? where medikament_id = ?;";
                            PreparedStatement preparedStatement = updateConnection(query);
                            preparedStatement.setDate(1, new java.sql.Date(warenbestand.getAblauf_datum().getTime()));
                            preparedStatement.setDouble(2, warenbestand.getVerkaufspreis());
                            preparedStatement.setInt(3, medikament.getId());
                            preparedStatement.executeUpdate();
                            System.out.println(medikament.getName() + " has been updated!");
                        } else {
                            System.out.println("Couldn't update!");
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (digit == 3) {
                try {
                    String query = null;
                    scan = new Scanner(System.in);
                    System.out.println("Which medicine you want to delete? ");
                    if (scan.hasNext()) {
                        medikament.setName(scan.next());
                        query = "select * from medikament where name like " + "\"%" + medikament.getName() + "%\"" + ";";
                        ResultSet resultSet = connection(query);
                        while (resultSet.next()) {
                            medikament.setId(resultSet.getInt("id"));
                            medikament.getLager().setId(resultSet.getInt("lager_id"));
                        }
                        if (medikament.getId() > 0) {
                            query = "delete from warenbestand where medikament_id = ?;";
                            PreparedStatement preparedStatement = updateConnection(query);
                            preparedStatement.setInt(1, medikament.getId());
                            preparedStatement.execute();
                            query = "delete from medikament where id = ?;";
                            preparedStatement = updateConnection(query);
                            preparedStatement.setInt(1, medikament.getId());
                            preparedStatement.execute();
                            query = "delete from lager where id = ?;";
                            preparedStatement = updateConnection(query);
                            preparedStatement.setInt(1, medikament.getLager().getId());
                            preparedStatement.execute();
                            System.out.println(medikament.getName() + " has been deleted!");
                        } else {
                            System.out.println("Invalid medicine name!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Invalid entry!");
            }
        }
    }

    static void sale() {
        clearScreen();
        System.out.println("Welcome to the sales module!!!");
        double total = 0;
        boolean continueSale = false;
        do {
            Scanner scan = new Scanner(System.in);
            System.out.println("Do you wish to add (more) items to the total? (Y/N): ");
            if (scan.hasNext()) {
                String input=scan.next();
                if (input.equals("Y") || input.equals("y") ) {
                    continueSale = true;
                    total += subTotal();
                }else {
                    continueSale = false;
                }
            }
        } while (continueSale);
        DecimalFormat dec = new DecimalFormat("#0.00");
        System.out.println("Total: " + dec.format(total));
    }

    static double subTotal () {
        Medikament medikament = new Medikament();
        Warenbestand warenbestand = new Warenbestand();
        double subtotal = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please type in the name of the medicine: ");
        if (scan.hasNext()){
            medikament.setName(scan.next());
        } else {
            System.out.println("Invalid input!");
        }
        String query = "select m.name as mn, h.name hn, l.id as li, l.einheiten as le, w.verkaufspreis as wv from medikament as m " +
                "inner join hersteller as h on h.id = m.hersteller_id " +
                "inner join lager as l on l.id = m.lager_id " +
                "inner join warenbestand as w on w.medikament_id = m.id " +
                "where m.name like " + "\"%" + medikament.getName() + "%\"" + ";";
        try {
            ResultSet resultSet = connection(query);
            int count = 0;
            while (resultSet.next()) {
                medikament.setName(resultSet.getString("mn"));
                medikament.getHersteller().setName(resultSet.getString("hn"));
                medikament.getLager().setId(resultSet.getInt("li"));
                medikament.getLager().setEinheiten(resultSet.getInt("le"));
                warenbestand.setVerkaufspreis(resultSet.getDouble("wv"));
                count++;
                System.out.println(count + "- Medikament: " + medikament.getName() + ", Hersteller: " +
                        medikament.getHersteller().getName() + ", Fach: " + medikament.getLager().getId() + ", Einheiten: " +
                        medikament.getLager().getEinheiten() + ", Verkaufspreis: €" + warenbestand.getVerkaufspreis());
                medikament.getLager().setId(resultSet.getInt("li"));
                subtotal = resultSet.getDouble("wv");
                //Update einheiten
                int itemsSold = 0;
                scan = new Scanner(System.in);
                System.out.println("Please enter the units sold: ");
                if (scan.hasNext()){
                    itemsSold = scan.nextInt();
                } else {
                    System.out.println("Invalid input!");
                }
                subtotal *= itemsSold;
                medikament.getLager().setEinheiten(medikament.getLager().getEinheiten() - itemsSold);
                if (medikament.getLager().getEinheiten() > 0) {
                    query = "update lager set einheiten = ? where id = ?;";
                    PreparedStatement preparedStatement = updateConnection(query);
                    preparedStatement.setInt(1, medikament.getLager().getEinheiten());
                    preparedStatement.setInt(2, medikament.getLager().getId());
                    preparedStatement.executeUpdate();
                } else {
                    System.out.println("Medicine not in stock!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("------------------------------------");
        return subtotal;
    }
}
