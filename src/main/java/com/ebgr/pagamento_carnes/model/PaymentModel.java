package com.ebgr.pagamento_carnes.model;

import com.ebgr.pagamento_carnes.controller.dto.PaymentDTO;
import com.ebgr.pagamento_carnes.controller.dto.PaymentMonthDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.repository.cdi.Eager;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@ToString
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

    private String txid;
    @Setter
    private String pixUrl;
    @Setter
    private LocalDateTime expiresAt;
    @Setter
    private LocalDateTime closedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    UserModel user;


    public PaymentModel(UserModel user, int paymentMonth, int paymentYear) {
        this.user = user;
        this.paymentYear = paymentYear;
        this.paymentMonth = paymentMonth;
        this.expiresAt = null;
        this.txid = generate_txid();
    }

    public int getPaymentMonth() { return paymentMonth; }


    public PaymentMonthDTO serialize() {
        PaymentMonthDTO dto = new PaymentMonthDTO(
                this.paymentMonth,
                this.paymentYear,
                this.closedAt,
                this.pixUrl
        );
        return dto;
    }



    private static Random random= new Random();
    private static String generate_txid() {
        return (LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0)) + "_" + random.nextInt(0, 2_147_483_647));
    }

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
