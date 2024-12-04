package com.ebgr.pagamento_carnes.repository;

import com.ebgr.pagamento_carnes.model.PaymentModel;
import com.ebgr.pagamento_carnes.model.UserModel;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository  extends CrudRepository<PaymentModel, Integer> {
    public Optional<PaymentModel> findByUserAndPaymentMonthAndPaymentYear(UserModel user, int payment_month, int payment_year);
    public List<PaymentModel> findByUser(UserModel user);

    //List<PaymentModel> findAllByUserAndExpiresAtAfter(UserModel user, LocalDateTime expiresAt);
    List<PaymentModel> findAllByUserAndExpiresAtAfterOrExpiresAtIsNull(UserModel user, LocalDateTime expiresAt);
    List<PaymentModel> findAllByExpiresAtBefore(LocalDateTime expiresAt);

    public List<PaymentModel> findAll();
    //public void delete(List<PaymentModel> paymentModels);
}
