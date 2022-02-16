package net.primegames.components.store.payment;

import java.util.HashMap;
import java.util.Map;

public class PaymentStore {

    private final Map<String, Payment> payments = new HashMap<>();

    public Payment getPayment(String transectionId) {
        return payments.get(transectionId);
    }

    public Map<String, Payment> getPayments() {
        return payments;
    }

    public void addPayment(Payment payment) {
        if (payment.isClaimed()) {
            throw new IllegalArgumentException("Not allowed to register already claimed purchase");
        }
        payments.put(payment.getTransaction_id(), payment);
    }

    public void removePayment(String transectionId) {
        payments.remove(transectionId);
    }


    public void removePayment(Payment payment){
        payments.remove(payment.getTransaction_id());
    }

    //get payments for xuid
    public Map<String, Payment> getPayments(String xuid) {
        Map<String, Payment> payments = new HashMap<>();
        for (Payment payment : this.payments.values()) {
            if (payment.getXuid().equals(xuid)) {
                payments.put(payment.getTransaction_id(), payment);
            }
        }
        return payments;
    }


}
