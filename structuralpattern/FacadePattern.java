/*The Facade Pattern is a structural design pattern that provides a simplified, 
unified interface to a complex subsystem or group of classes.
It acts as a single entry point for clients to interact with the system,
 hiding the underlying complexity and making the system easier to use. */
 
package structuralpattern;
// Service class responsible for handling payments
interface IPaymentService { void makePayment(String accountId, double amount); }
interface ISeatReservationService { void reserveSeat(String movieId, String seatNumber); }
interface INotificationService { void sendBookingConfirmation(String userEmail); }
interface ILoyaltyPointsService { void addPoints(String accountId, int points); }
interface ITicketService { void generateTicket(String movieId, String seatNumber); }

// Concrete Implementations
class PaymentServiceImpl implements IPaymentService {
    public void makePayment(String accountId, double amount) {
        System.out.println("Payment of ₹" + amount + " successful for account " + accountId);
    }
}

class SeatReservationServiceImpl implements ISeatReservationService {
    public void reserveSeat(String movieId, String seatNumber) {
        System.out.println("Seat " + seatNumber + " reserved for movie " + movieId);
    }
}

class NotificationServiceImpl implements INotificationService {
    public void sendBookingConfirmation(String userEmail) {
        System.out.println("Booking confirmation sent to " + userEmail);
    }
}

class LoyaltyPointsServiceImpl implements ILoyaltyPointsService {
    public void addPoints(String accountId, int points) {
        System.out.println(points + " loyalty points added to account " + accountId);
    }
}

class TicketServiceImpl implements ITicketService {
    public void generateTicket(String movieId, String seatNumber) {
        System.out.println("Ticket generated for movie " + movieId + ", Seat: " + seatNumber);
    }
}


class BookingRequest {
    private final String accountId;
    private final String movieId;
    private final String seatNumber;
    private final String userEmail;
    private final double amount;

    // Private constructor so it can only be made via the Builder
    private BookingRequest(Builder builder) {
        this.accountId = builder.accountId;
        this.movieId = builder.movieId;
        this.seatNumber = builder.seatNumber;
        this.userEmail = builder.userEmail;
        this.amount = builder.amount;
    }

    // Getters
    public String getAccountId() { return accountId; }
    public String getMovieId() { return movieId; }
    public String getSeatNumber() { return seatNumber; }
    public String getUserEmail() { return userEmail; }
    public double getAmount() { return amount; }

    // Nested Builder for arguments
    public static class Builder {
        private String accountId;
        private String movieId;
        private String seatNumber;
        private String userEmail;
        private double amount;

        public Builder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder setMovieId(String movieId) {
            this.movieId = movieId;
            return this;
        }

        public Builder setSeatNumber(String seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public Builder setUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public BookingRequest build() {
            // Quick validation logic can live here
            if (accountId == null || movieId == null || seatNumber == null) {
                throw new IllegalArgumentException("Account, Movie, and Seat selection are required.");
            }
            return new BookingRequest(this);
        }
    }
}

// ========== The MovieBookingFacade class  ==============

class MovieBookingFacade {
    private final IPaymentService paymentService;
    private final ISeatReservationService seatReservationService;
    private final INotificationService notificationService;
    private final ILoyaltyPointsService loyaltyPointsService;
    private final ITicketService ticketService;

    // Private constructor - only accessible via the Builder
    private MovieBookingFacade(Builder builder) {
        this.paymentService = builder.paymentService;
        this.seatReservationService = builder.seatReservationService;
        this.notificationService = builder.notificationService;
        this.loyaltyPointsService = builder.loyaltyPointsService;
        this.ticketService = builder.ticketService;
    }

    // Unified operational method
    public void bookMovieTicket(BookingRequest request) {
        paymentService.makePayment(request.getAccountId(), request.getAmount());
        seatReservationService.reserveSeat(request.getMovieId(), request.getSeatNumber());
        ticketService.generateTicket(request.getMovieId(), request.getSeatNumber());
        
        // Optional feature safety check
        if (loyaltyPointsService != null) {
            loyaltyPointsService.addPoints(request.getAccountId(), 50);
        }
        if (notificationService != null) {
            notificationService.sendBookingConfirmation(request.getUserEmail());
        }

        System.out.println("Movie ticket booking completed successfully!");
    }

    // The Builder Class
    public static class Builder {
        private IPaymentService paymentService;
        private ISeatReservationService seatReservationService;
        private INotificationService notificationService;
        private ILoyaltyPointsService loyaltyPointsService;
        private ITicketService ticketService;

        public Builder setPaymentService(IPaymentService paymentService) {
            this.paymentService = paymentService;
            return this;
        }

        public Builder setSeatReservationService(ISeatReservationService seatReservationService) {
            this.seatReservationService = seatReservationService;
            return this;
        }

        public Builder setNotificationService(INotificationService notificationService) {
            this.notificationService = notificationService;
            return this;
        }

        public Builder setLoyaltyPointsService(ILoyaltyPointsService loyaltyPointsService) {
            this.loyaltyPointsService = loyaltyPointsService;
            return this;
        }

        public Builder setTicketService(ITicketService ticketService) {
            this.ticketService = ticketService;
            return this;
        }

        public MovieBookingFacade build() {
            // Enforce mandatory systems check
            if (paymentService == null || seatReservationService == null || ticketService == null) {
                throw new IllegalStateException("Missing critical services required for booking.");
            }
            return new MovieBookingFacade(this);
        }
    }
}

public class FacadePattern {
    public static void main(String[] args) {
        // 1. Build the Facade instance using the Builder Pattern
        MovieBookingFacade movieBookingFacade = new MovieBookingFacade.Builder()
                .setPaymentService(new PaymentServiceImpl())
                .setSeatReservationService(new SeatReservationServiceImpl())
                .setTicketService(new TicketServiceImpl())
                .setLoyaltyPointsService(new LoyaltyPointsServiceImpl())
                .setNotificationService(new NotificationServiceImpl())
                .build();

        // 2. Argument Builder: Construct your runtime values clearly
        BookingRequest request = new BookingRequest.Builder()
                .setAccountId("user123")
                .setMovieId("movie456")
                .setSeatNumber("A10")
                .setUserEmail("user@example.com")
                .setAmount(500.0)
                .build();

        // 3. Execution
        movieBookingFacade.bookMovieTicket(request);
    }
}
