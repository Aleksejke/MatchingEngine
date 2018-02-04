package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class MatchingEngine {

    //reading of divided line
    public static Orders reading(String[] array) {
        Orders order = new Orders();
        for (int i = 0; i < array.length; i++) {
            if (i == 0)
                order.setName(array[i]);
            if (i == 1)
                order.setOperation(array[i]);
            if (i == 2)
                order.setStock(array[i]);
            if (i == 3)
                order.setQuantity(Integer.parseInt(array[i]));
            if (i == 4)
                order.setPrice(new BigDecimal(array[i]));
        }
        return order;
    }

    //adding Trade to ArrayList from seller side
    public static ArrayList<String> adding_tradeA(ArrayList<String> trade, Orders order, ArrayList<Orders> side, int n) {
        ArrayList<String> deal = trade;
        deal.add(order.getName() + " -> " +
                side.get(n).getName() + " " +
                order.getStock() + " " +
                side.get(n).getQuantity() + "@" +
                side.get(n).getPrice());
        return deal;
    }

    //adding Trade to ArrayList from buyer side
    public static ArrayList<String> adding_tradeB(ArrayList<String> trade, Orders order, ArrayList<Orders> side, int n) {
        ArrayList<String> deal = trade;
        deal.add(side.get(n).getName() + " -> " +
                order.getName() + " " +
                order.getStock() + " " +
                side.get(n).getQuantity() + "@" +
                side.get(n).getPrice());
        return deal;
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Orders> sellers = new ArrayList<>();      //Sellers in the Order Book
        ArrayList<Orders> buyers = new ArrayList<>();       //Buyers in the Order Book
        ArrayList<String> output = new ArrayList<String>(); //for output into file
        Orders order = new Orders();

        BufferedReader reader = new BufferedReader(new FileReader("Orders.txt"));
        String line;
        String[] array;

        if (reader.ready()) {

            while ((line = reader.readLine()) != null) {
                array = line.split("[ @]");

                if (array.length != 5) {
                    System.out.println("Incorrect number of parameters!");
                    break;
                } else {

                    order = reading(array);
                    if (order.getOperation().equals("SELL")) {
                        sellers.add(order);

                        if (!buyers.isEmpty()) {
                            for (int i = 0; i < buyers.size(); i++) {
                                //order = last seller
                                if (order.getStock().equals(buyers.get(i).getStock()) && (order.getQuantity() != 0)) {
                                    int which_bigger = order.getPrice().compareTo(buyers.get(i).getPrice());

                                    //if our seller has smaller/equal price than buyer -> continue checking
                                    if ((which_bigger == 0) || (which_bigger == -1)) {

                                        if (order.getQuantity() > buyers.get(i).getQuantity()) {
                                            output = adding_tradeA(output, order, buyers, i);
                                            int quantity_difference = order.getQuantity() -
                                                    buyers.get(i).getQuantity();
                                            order.setQuantity(quantity_difference);
                                            sellers.get(sellers.size() - 1).setQuantity(quantity_difference);
                                            buyers.remove(i);
                                            i--;    //because we remove one element from ArrayList
                                        } else

                                        if (order.getQuantity() == buyers.get(i).getQuantity()) {
                                            output = adding_tradeA(output, order, buyers, i);
                                            sellers.remove(sellers.size() - 1);
                                            buyers.remove(i);
                                            break;
                                        } else

                                        if (order.getQuantity() < buyers.get(i).getQuantity()) {
                                            output.add(order.getName() + " -> " +
                                                    buyers.get(i).getName() + " " +
                                                    order.getStock() + " " +
                                                    order.getQuantity() + "@" +
                                                    buyers.get(i).getPrice());

                                            int quantity_difference = buyers.get(i).getQuantity() -
                                                    order.getQuantity();
                                            buyers.get(i).setQuantity(quantity_difference);
                                            sellers.remove(sellers.size() - 1);
                                            break;
                                        }

                                    } else break;
                                }
                            }
                        }
                        //sorting of sellers by price (ascending order)
                        Collections.sort(sellers);

                    } else {
                        buyers.add(order);

                        if (!sellers.isEmpty()) {
                            for (int i = 0; i < sellers.size(); i++) {
                                //order = last buyer
                                if (order.getStock().equals(sellers.get(i).getStock()) && (order.getQuantity() != 0)) {
                                    int which_bigger = order.getPrice().compareTo(sellers.get(i).getPrice());

                                    //if our buyer has bigger/equal price than seller -> continue checking
                                    if ((which_bigger == 1) || (which_bigger == 0)) {

                                        if (order.getQuantity() > sellers.get(i).getQuantity()) {
                                            output = adding_tradeB(output, order, sellers, i);
                                            int quantity_difference = order.getQuantity() -
                                                    sellers.get(i).getQuantity();
                                            order.setQuantity(quantity_difference);
                                            buyers.get(buyers.size() - 1).setQuantity(quantity_difference);
                                            sellers.remove(i);
                                            i--;    //because we remove one element from ArrayList
                                        } else

                                        if (order.getQuantity() == sellers.get(i).getQuantity()) {
                                            output = adding_tradeB(output, order, sellers, i);
                                            buyers.remove(buyers.size() - 1);
                                            sellers.remove(i);
                                            break;
                                        } else

                                        if (order.getQuantity() < sellers.get(i).getQuantity()) {
                                            output.add(sellers.get(i).getName() + " -> " +
                                                    order.getName() + " " +
                                                    order.getStock() + " " +
                                                    order.getQuantity() + "@" +
                                                    sellers.get(i).getPrice());

                                            int quantity_difference = sellers.get(i).getQuantity() -
                                                    order.getQuantity();
                                            sellers.get(i).setQuantity(quantity_difference);
                                            buyers.remove(buyers.size() - 1);
                                            break;
                                        }

                                    } else break;
                                }
                            }
                        }
                        //sorting of buyers by price (descending order)
                        Collections.sort(buyers);
                    }

                    order = new Orders();
                }
            }

            reader.close();


            PrintWriter writer = new PrintWriter("Trades.txt");
            for (String out: output)
                writer.println(out);
            writer.close();

        } else System.out.println("Error! File is empty!");
    }
}