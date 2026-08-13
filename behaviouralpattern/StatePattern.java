package behaviouralpattern;

class OrderContext{
    private OrderState orderState;

    public OrderContext() {
        this.orderState = new OrderPlacedState();
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void next() {
        orderState.next(this);
    }

    public void cancel() {
        orderState.cancel(this);
    }

    public void getOrderStateName() {
        System.out.println(orderState.getStateName());
    }
}

interface OrderState{
    void next(OrderContext orderContext);
    void cancel(OrderContext orderContext);
    String getStateName();
}

class OrderPlacedState implements OrderState{
    @Override
    public void next(OrderContext orderContext){
        orderContext.setOrderState(new PreparingState());
        System.out.println("Order is now being prepared.");
    }
    @Override
    public void cancel(OrderContext orderContext){
        orderContext.setOrderState(new CancelledState());
        System.out.println("Order has been cancelled.");
    }
    @Override
    public String getStateName() {
        return "ORDER_PLACED";
    }
}

class PreparingState implements OrderState{
    @Override
    public void next(OrderContext orderContext){
        orderContext.setOrderState(new OutForDeliveryState());
        System.out.println("Order is now out for delivery.");
    }
    @Override
    public void cancel(OrderContext orderContext){
        System.out.println("Order can not be cancelled.");
    }
    @Override
    public String getStateName() {
        return "ORDER_PREPARING";
    }
}
class OutForDeliveryState implements OrderState{
    @Override
    public void next(OrderContext orderContext){
        orderContext.setOrderState(new DeliveredState());
        System.out.println("Order has been delivered.");
    }
    @Override
    public void cancel(OrderContext orderContext){
        System.out.println("Order can not be cancelled.");
    }
    @Override
    public String getStateName() {
        return "ORDER_OUT_FOR_DELIVERY";
    }
}
class DeliveredState implements OrderState{
    @Override
    public void next(OrderContext orderContext){
        System.out.println("Order is already delivered.");
    }
    @Override
    public void cancel(OrderContext orderContext){
        System.out.println("Order can not be cancelled.");
    }
    @Override
    public String getStateName() {
        return "ORDER_DELIVERED";
    }
}
class CancelledState implements OrderState{
    @Override
    public void next(OrderContext orderContext){
        System.out.println("Cancelled order cannot move to next state.");
    }
    @Override
    public void cancel(OrderContext orderContext){
        System.out.println("Order is already cancelled.");
    }
    @Override
    public String getStateName() {
        return "ORDER_CANCELLED";
    }
}

public class StatePattern {
    public static void main(String[] args) {
        OrderContext order = new OrderContext();
        order.getOrderStateName();
        order.next();
        order.getOrderStateName();
        order.next();
        order.getOrderStateName();
        order.next();
        order.getOrderStateName();
        order.cancel();
        order.getOrderStateName();
    }
}
