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

//@ToString
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
        //this.txid = generate_txid();
    }


    public PaymentMonthDTO serialize() {
        return new PaymentMonthDTO(
                this.paymentMonth,
                this.paymentYear,
                this.expiresAt,
                this.closedAt,
                this.pixUrl
        );
    }



    /*private static Random random= new Random();
    private static String generate_txid() {
        long randomNumber = (LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0))*1000 + random.nextInt(0, 1_024));
        return String.format("%032d", randomNumber);
    }*/


    @Override
    public String toString() {
        return String.format("PaymentModel[id=%d, user=UserModel[...], paymentMonth=%d, paymentYear=%d, txid=%s, pixUrl=%s, expiresAt=%s, closedAt=%s]", id, paymentMonth, paymentYear, txid, pixUrl, expiresAt, closedAt);
    }

}
