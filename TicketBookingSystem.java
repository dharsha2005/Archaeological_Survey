import java.awt.*;
import java.awt.event.*;
import java.util.*;

class MonumentNode {
    String name;
    double indianChildPrice, indianAdultPrice, indianSeniorPrice;
    double foreignerChildPrice, foreignerAdultPrice, foreignerSeniorPrice;
    String openingTime, closingTime;
    String[] openDays;
    int availableTickets;
    MonumentNode next;

    public MonumentNode(String name, double indianChildPrice, double indianAdultPrice, double indianSeniorPrice,
                        double foreignerChildPrice, double foreignerAdultPrice, double foreignerSeniorPrice,
                        String openingTime, String closingTime, String[] openDays, int availableTickets) {
        this.name = name;
        this.indianChildPrice = indianChildPrice;
        this.indianAdultPrice = indianAdultPrice;
        this.indianSeniorPrice = indianSeniorPrice;
        this.foreignerChildPrice = foreignerChildPrice;
        this.foreignerAdultPrice = foreignerAdultPrice;
        this.foreignerSeniorPrice = foreignerSeniorPrice;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.openDays = openDays;
        this.availableTickets = availableTickets;
        this.next = null;
    }

    public String displayInfo() {
        return name + " | Prices (Indian): Child: ₹" + indianChildPrice + ", Adult: ₹" + indianAdultPrice +
                ", Senior: ₹" + indianSeniorPrice +
                "\nPrices (Foreigner): Child: ₹" + foreignerChildPrice + ", Adult: ₹" + foreignerAdultPrice +
                ", Senior: ₹" + foreignerSeniorPrice +
                "\nTimings: " + openingTime + " to " + closingTime +
                "\nOpen Days: " + String.join(", ", openDays) +
                "\nAvailable Tickets: " + availableTickets;
    }
}

class CityNode {
    String cityName;
    MonumentNode monumentsHead;
    CityNode next;

    public CityNode(String cityName) {
        this.cityName = cityName;
        this.monumentsHead = null;
        this.next = null;
    }

    public void addMonument(String name, double indianChildPrice, double indianAdultPrice, double indianSeniorPrice,
                            double foreignerChildPrice, double foreignerAdultPrice, double foreignerSeniorPrice,
                            String openingTime, String closingTime, String[] openDays, int availableTickets) {
        MonumentNode newMonument = new MonumentNode(name, indianChildPrice, indianAdultPrice, indianSeniorPrice,
                foreignerChildPrice, foreignerAdultPrice, foreignerSeniorPrice,
                openingTime, closingTime, openDays, availableTickets);
        if (monumentsHead == null) {
            monumentsHead = newMonument;
        } else {
            MonumentNode temp = monumentsHead;
            while (temp.next != null) temp = temp.next;
            temp.next = newMonument;
        }
    }

    public String displayMonuments() {
        if (monumentsHead == null) return "No monuments in " + cityName;
        StringBuilder sb = new StringBuilder();
        MonumentNode temp = monumentsHead;
        while (temp != null) {
            sb.append(temp.displayInfo()).append("\n\n");
            temp = temp.next;
        }
        return sb.toString();
    }

    public String[] getMonumentNames() {
        ArrayList<String> monumentNames = new ArrayList<>();
        MonumentNode temp = monumentsHead;
        while (temp != null) {
            monumentNames.add(temp.name);
            temp = temp.next;
        }
        return monumentNames.toArray(new String[0]);
    }
}

class TicketBookingSystem extends Frame implements ActionListener {
    private CityNode head;
    private Stack<String> bookingHistory;
    private Queue<String> waitingList;
    private TextArea displayArea;
    private TextField ticketNumberField;
    private Choice cityChoice, monumentChoice, ageGroupChoice, nationalityChoice;

    public TicketBookingSystem() {
        head = null;
        bookingHistory = new Stack<>();
        waitingList = new LinkedList<>();
        initializeSystem();

        setLayout(new BorderLayout());
        setTitle("Monument Ticket Booking System");
        setSize(800, 600);

        Panel header = new Panel();
        header.setBackground(Color.BLUE);
        Label title = new Label("Monument Ticket Booking System", Label.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(title);
        add(header, BorderLayout.NORTH);

        Panel centerPanel = new Panel(new GridLayout(5, 2, 10, 10));
        centerPanel.setBackground(Color.LIGHT_GRAY);

        cityChoice = new Choice();
        cityChoice.add("Select City");
        String[] cities = {"Agra", "Chennai", "Delhi", "Mumbai", "Bangalore", "Kolkata", "Jaipur", "Hyderabad", "Goa", "Amritsar"};
        for (String city : cities) cityChoice.add(city);
        cityChoice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateMonumentChoices();
            }
        });

        centerPanel.add(new Label("City:"));
        centerPanel.add(cityChoice);

        centerPanel.add(new Label("Monument Name:"));
        monumentChoice = new Choice();
        centerPanel.add(monumentChoice);

        centerPanel.add(new Label("Number of Tickets:"));
        ticketNumberField = new TextField(5);
        centerPanel.add(ticketNumberField);

        centerPanel.add(new Label("Age Group (child/adult/senior):"));
        ageGroupChoice = new Choice();
        ageGroupChoice.add("child");
        ageGroupChoice.add("adult");
        ageGroupChoice.add("senior");
        centerPanel.add(ageGroupChoice);

        centerPanel.add(new Label("Nationality:"));
        nationalityChoice = new Choice();
        nationalityChoice.add("Indian");
        nationalityChoice.add("Foreigner");
        centerPanel.add(nationalityChoice);

        add(centerPanel, BorderLayout.CENTER);

        Panel bottomPanel = new Panel(new FlowLayout());

        Button displayMonumentsBtn = new Button("Display Monuments");
        displayMonumentsBtn.addActionListener(this);
        bottomPanel.add(displayMonumentsBtn);

        Button bookTicketBtn = new Button("Book Ticket");
        bookTicketBtn.addActionListener(this);
        bottomPanel.add(bookTicketBtn);

        Button showHistoryBtn = new Button("Show Booking History");
        showHistoryBtn.addActionListener(this);
        bottomPanel.add(showHistoryBtn);

        Button exitBtn = new Button("Exit");
        exitBtn.addActionListener(this);
        bottomPanel.add(exitBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        displayArea = new TextArea(20, 40);
        add(displayArea, BorderLayout.EAST);

        setVisible(true);
    }

    private void updateMonumentChoices() {
        String selectedCity = cityChoice.getSelectedItem();
        monumentChoice.removeAll();
        if (!selectedCity.equals("Select City")) {
            CityNode city = head;
            while (city != null) {
                if (city.cityName.equals(selectedCity)) {
                    String[] monumentNames = city.getMonumentNames();
                    for (String name : monumentNames) {
                        monumentChoice.add(name);
                    }
                    break;
                }
                city = city.next;
            }
        }
    }

    private void initializeSystem() {
        addCity("Agra");
        addMonumentToCity("Agra", "Taj Mahal", 50, 250, 200, 500, 1000, 800,
                "6:00 AM", "6:00 PM", new String[]{"Monday", "Wednesday", "Friday"}, 100);

        addCity("Chennai");
        addMonumentToCity("Chennai", "Marina Beach", 0, 0, 0, 0, 0, 0,
                "24 hours", "24 hours", new String[]{"All Days"}, 300);

        addCity("Delhi");
        addMonumentToCity("Delhi", "Red Fort", 30, 100, 50, 500, 800, 600,
                "9:00 AM", "5:30 PM", new String[]{"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}, 200);
        addMonumentToCity("Delhi", "India Gate", 0, 0, 0, 0, 0, 0,
                "24 hours", "24 hours", new String[]{"All Days"}, 500);

        addCity("Mumbai");
        addMonumentToCity("Mumbai", "Gateway of India", 20, 75, 40, 400, 700, 500,
                "9:00 AM", "6:00 PM", new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}, 150);
        addMonumentToCity("Mumbai", "Marine Drive", 0, 0, 0, 0, 0, 0,
                "24 hours", "24 hours", new String[]{"All Days"}, 400);

        addCity("Bangalore");
        addMonumentToCity("Bangalore", "Lalbagh Botanical Garden", 10, 30, 20, 100, 150, 120,
                "6:00 AM", "7:00 PM", new String[]{"All Days"}, 250);

        addCity("Kolkata");
        addMonumentToCity("Kolkata", "Victoria Memorial", 20, 50, 30, 500, 750, 600,
                "10:00 AM", "5:00 PM", new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}, 180);

        addCity("Jaipur");
        addMonumentToCity("Jaipur", "Hawa Mahal", 30, 100, 50, 500, 800, 600,
                "9:00 AM", "5:00 PM", new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}, 220);

        addCity("Hyderabad");
        addMonumentToCity("Hyderabad", "Charminar", 25, 70, 40, 300, 500, 400,
                "9:30 AM", "5:30 PM", new String[]{"All Days"}, 200);

        addCity("Goa");
        addMonumentToCity("Goa", "Basilica of Bom Jesus", 15, 50, 30, 200, 350, 300,
                "9:00 AM", "6:00 PM", new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}, 100);

        addCity("Amritsar");
        addMonumentToCity("Amritsar", "Golden Temple", 0, 0, 0, 0, 0, 0,
                "24 hours", "24 hours", new String[]{"All Days"}, 500);
    }

    private void addCity(String cityName) {
        CityNode newCity = new CityNode(cityName);
        if (head == null) {
            head = newCity;
        } else {
            CityNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newCity;
        }
    }

    private void addMonumentToCity(String cityName, String monumentName, double indianChildPrice, double indianAdultPrice,
                                   double indianSeniorPrice, double foreignerChildPrice, double foreignerAdultPrice,
                                   double foreignerSeniorPrice, String openingTime, String closingTime,
                                   String[] openDays, int availableTickets) {
        CityNode temp = head;
        while (temp != null) {
            if (temp.cityName.equals(cityName)) {
                temp.addMonument(monumentName, indianChildPrice, indianAdultPrice, indianSeniorPrice,
                        foreignerChildPrice, foreignerAdultPrice, foreignerSeniorPrice,
                        openingTime, closingTime, openDays, availableTickets);
                break;
            }
            temp = temp.next;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("Display Monuments")) {
            String cityName = cityChoice.getSelectedItem();
            CityNode city = head;
            while (city != null) {
                if (city.cityName.equals(cityName)) {
                    displayArea.setText(city.displayMonuments());
                    break;
                }
                city = city.next;
            }
        } else if (action.equals("Book Ticket")) {
            String cityName = cityChoice.getSelectedItem();
            String monumentName = monumentChoice.getSelectedItem();
            String ageGroup = ageGroupChoice.getSelectedItem();
            String nationality = nationalityChoice.getSelectedItem();
            String ticketCountStr = ticketNumberField.getText();

            if (cityName.equals("Select City") || monumentName == null || ticketCountStr.isEmpty()) {
                displayArea.setText("Please fill all fields before booking.");
                return;
            }

            int ticketCount;
            try {
                ticketCount = Integer.parseInt(ticketCountStr);
            } catch (NumberFormatException ex) {
                displayArea.setText("Please enter a valid number of tickets.");
                return;
            }

            // Find the selected city and monument
            CityNode city = head;
            MonumentNode selectedMonument = null;
            while (city != null) {
                if (city.cityName.equals(cityName)) {
                    MonumentNode temp = city.monumentsHead;
                    while (temp != null) {
                        if (temp.name.equals(monumentName)) {
                            selectedMonument = temp;
                            break;
                        }
                        temp = temp.next;
                    }
                    break;
                }
                city = city.next;
            }

            if (selectedMonument == null) {
                displayArea.setText("Monument not found.");
                return;
            }

            // Calculate ticket price based on age group and nationality
            double pricePerTicket = 0;
            if (nationality.equals("Indian")) {
                if (ageGroup.equals("child")) {
                    pricePerTicket = selectedMonument.indianChildPrice;
                } else if (ageGroup.equals("adult")) {
                    pricePerTicket = selectedMonument.indianAdultPrice;
                } else if (ageGroup.equals("senior")) {
                    pricePerTicket = selectedMonument.indianSeniorPrice;
                }
            } else if (nationality.equals("Foreigner")) {
                if (ageGroup.equals("child")) {
                    pricePerTicket = selectedMonument.foreignerChildPrice;
                } else if (ageGroup.equals("adult")) {
                    pricePerTicket = selectedMonument.foreignerAdultPrice;
                } else if (ageGroup.equals("senior")) {
                    pricePerTicket = selectedMonument.foreignerSeniorPrice;
                }
            }

            double totalAmount = pricePerTicket * ticketCount;

            // Check ticket availability
            if (selectedMonument.availableTickets >= ticketCount) {
                selectedMonument.availableTickets -= ticketCount;
                bookingHistory.push("Booked " + ticketCount + " tickets for " + monumentName + " in " + cityName +
                                    " for " + ageGroup + " (" + nationality + ") - Total: ₹" + totalAmount);
                displayArea.setText("Booking successful!\nTotal amount: ₹" + totalAmount +
                                    "\nAvailable tickets left: " + selectedMonument.availableTickets);
            } else {
                waitingList.add("Waiting list for " + ticketCount + " tickets to " + monumentName + " in " + cityName +
                                " for " + ageGroup + " (" + nationality + ")");
                displayArea.setText("Not enough tickets available. You have been added to the waiting list.");
            }
        } else if (action.equals("Show Booking History")) {
            displayArea.setText("Booking History:\n" + String.join("\n", bookingHistory) +
                                "\n\nWaiting List:\n" + String.join("\n", waitingList));
        } else if (action.equals("Exit")) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new TicketBookingSystem();
    }
}