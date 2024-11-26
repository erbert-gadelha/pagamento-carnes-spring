package com.ebgr.pagamento_carnes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_payment")
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="payment_month")
    private Integer paymentMonth;
    @Column(name="payment_year")
    private Integer paymentYear;

    @Setter
    private String pixUrl;
    @Setter
    private LocalDateTime expiresAt;
    @Setter
    private LocalDateTime closedAt;

    @ManyToOne//(fetch = FetchType.LAZY)
    UserModel user;


    public PaymentModel(UserModel user, int paymentMonth, int paymentYear/*, String pixUrl*/) {
        this.user = user;
        this.pixUrl = pixUrl;
        this.paymentYear = paymentYear;
        this.paymentMonth = paymentMonth;
        this.expiresAt = LocalDateTime.now().plusSeconds(3600);
    }

    public LocalDateTime getClosedAt() {return closedAt;}

    public int getPaymentMonth() { return paymentMonth; }

    /*@Override
    public String toString() {
        return String.format("""
                Payment: {
                    id: %s
                    user: {
                        id: %d,
                        name: %s,
                        password: %s
                    },
                    month: %d,
                    year: %d,
                    pixUrl: %s,
                    expiresAt: %s,
                    closedAt: %s
                }
                """, id, user.getId(), user.getName(), user.getPassword(), paymentMonth, paymentYear, pixUrl, expiresAt, closedAt);
    }*/

}
