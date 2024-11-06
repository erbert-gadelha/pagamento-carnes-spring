package com.ebgr.pagamento_carnes.service;

import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.ebgr.pagamento_carnes.model.Payment;
import com.ebgr.pagamento_carnes.model.User;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    EfiHelper efiHelper;

    public Payment createOrGetPayment(User user, int month, int year) {
        Payment payment = paymentRepository.findByUserAndPaymentMonthAndPaymentYear(user, year, month).orElse(null);
        if(payment == null)
            payment = new Payment(user, month, year);
        if(payment.getPixUrl() == null || payment.getExpiresAt().isAfter(LocalDateTime.now())) {
            CobrancaImediata.Response cobrancaImediata =  efiHelper.criarCobrancaImediata(new DTO_efi.Devedor("70292933479", "Erbert"), 2, 3600);
            GerarQRCode.Response qrCode = efiHelper.criarQrCode(cobrancaImediata);
            payment.setPixUrl(qrCode.linkVisualizacao());
            //payment.setPixUrl(cobrancaImediata.pixCopiaECola());
            payment.setExpiresAt(LocalDateTime.now().plusSeconds(5));
        }

        paymentRepository.save(payment);
        return payment;
    }

    private final String [] months = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    public Map<String, Object>[] createPaymentTable(User user) {
        List<HashMap<String, Object>> modelList = new ArrayList<>(12);
        //System.out.println(months.length);
        for (String month : months) {
            //System.out.println(month);
            modelList.add(new HashMap<>() {{
                put("name", month);
            }});
        }

        Map<String, Object>[] modelArray = modelList.toArray(new HashMap[0]);
        List<Payment> paymentList = paymentRepository.findByUser(user);

        int lastMonth = -1;
        boolean someExpired = false;
        for(Payment payment : paymentList) {
            lastMonth = payment.getPaymentMonth();
            if(payment.getClosedAt() != null) {
                modelArray[lastMonth].put("completeAt", payment.getClosedAt());
                continue;
            }

            String pixUrl = payment.getPixUrl();
            LocalDateTime expiresAt = payment.getExpiresAt();

            // NOT EXPIRED
            if (pixUrl != null && expiresAt != null && expiresAt.isAfter(LocalDateTime.now())) {
                modelArray[lastMonth].put("url", payment.getPixUrl());
                continue;
            } else {
                System.out.println(payment);
            }

            // EXPIRED
            if(!someExpired) {
                modelArray[lastMonth].put("clickable", "api/create-payment/" + lastMonth + "/2024");
                someExpired = true;
                System.err.println("expired: " + modelArray[lastMonth].get("name"));
            }


            //paymentRepository.delete(payment);
            //modelArray[lastMonth].put("clickable", "api/create-payment/"+lastMonth+"/2024");
            //someExpired = true;
            /*expiredMonth = lastMonth;
            lastMonth--;*/
        }


        if(!someExpired) {
            lastMonth++;
            if(lastMonth < modelArray.length)
                modelArray[lastMonth].put("clickable", "api/create-payment/"+lastMonth+"/2024");
        }



        System.out.println(Arrays.toString(modelArray));

        return modelArray;
    }
}
