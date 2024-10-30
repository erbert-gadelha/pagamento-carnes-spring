package com.ebgr.pagamento_carnes.repository;

import com.ebgr.pagamento_carnes.model.Payment;
import com.ebgr.pagamento_carnes.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository  extends CrudRepository<Payment, Integer> {
    public Optional<Payment> findByUserAndPaymentMonthAndPaymentYear(User user, int payment_month, int payment_year);
    public List<Payment> findByUser(User user);

    public List<Payment> findAll();
}
