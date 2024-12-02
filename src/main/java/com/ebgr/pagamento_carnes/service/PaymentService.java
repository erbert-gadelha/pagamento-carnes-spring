package com.ebgr.pagamento_carnes.service;


import com.ebgr.pagamento_carnes.controller.dto.PaymentDTO;
import com.ebgr.pagamento_carnes.controller.dto.PaymentMonthDTO;
import com.ebgr.pagamento_carnes.controller.dto.PaymentsSummary;
import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.ebgr.pagamento_carnes.model.PaymentModel;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import com.ebgr.pagamento_carnes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    UserService userService;
    @Autowired
    EfiHelper efiHelper;
    private final String [] months = {
            "janeiro", "fevereiro", "março", "abril", "maio", "junho",
            "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};

    public List<PaymentMonthDTO> getUserPayments(String login) {
        UserModel userModel = userService.findUserOrThrow(login);
        List<PaymentModel> paymentModels = paymentRepository.findByUser(userModel);

        return paymentModels.stream().map(PaymentModel::serialize).toList();
    }

    public PaymentDTO createOrGetPayment(String login, int month, int year) {

        UserModel userModel = userService.findUserOrThrow(login);
        final PaymentModel paymentModel_0 = paymentRepository.findByUserAndPaymentMonthAndPaymentYear(
                userModel,
                month,
                year
        ).orElse(null);


        if(paymentModel_0 != null)
            if( paymentModel_0.getPixUrl() != null && paymentModel_0.getExpiresAt().isAfter(LocalDateTime.now()))
                return new PaymentDTO(
                    paymentModel_0.getPixUrl(),
                    paymentModel_0.getExpiresAt()
                );
            else
                paymentRepository.delete(paymentModel_0);




        final PaymentModel paymentModel_1 = new PaymentModel(userModel, month, year);
        CobrancaImediata.Response cobrancaImediata =  efiHelper.criarCobrancaImediata(new DTO_efi.Devedor("70292933479", "Erbert"), 2, 3600);
        GerarQRCode.Response qrCode = efiHelper.criarQrCode(cobrancaImediata);
        paymentModel_1.setPixUrl(qrCode.linkVisualizacao());
        paymentModel_1.setExpiresAt(LocalDateTime.now().plusSeconds(3600));
        paymentRepository.save(paymentModel_1);

        return new PaymentDTO(
                paymentModel_1.getPixUrl(),
                paymentModel_1.getExpiresAt()
        );
    }


/*
    public Map<String, Object>[] createPaymentTable(UserModel user) {
        List<HashMap<String, Object>> modelList = new ArrayList<>(12);
        //System.out.println(months.length);
        for (String month : months) {
            //System.out.println(month);
            modelList.add(new HashMap<>() {{
                put("name", month);
            }});
        }

        Map<String, Object>[] modelArray = modelList.toArray(new HashMap[0]);
        List<PaymentModel> paymentModelList = paymentRepository.findByUser(user);

        int lastMonth = -1;
        boolean someExpired = false;
        for(PaymentModel paymentModel : paymentModelList) {
            lastMonth = paymentModel.getPaymentMonth();
            if(paymentModel.getClosedAt() != null) {
                modelArray[lastMonth].put("completeAt", paymentModel.getClosedAt());
                continue;
            }

            String pixUrl = paymentModel.getPixUrl();
            LocalDateTime expiresAt = paymentModel.getExpiresAt();

            // NOT EXPIRED
            if (pixUrl != null && expiresAt != null && expiresAt.isAfter(LocalDateTime.now())) {
                modelArray[lastMonth].put("url", paymentModel.getPixUrl());
                continue;
            } else {
                System.out.println(paymentModel);
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
            //expiredMonth = lastMonth;
            //lastMonth--;
        }


        if(!someExpired) {
            lastMonth++;
            if(lastMonth < modelArray.length)
                modelArray[lastMonth].put("clickable", "api/create-payment/"+lastMonth+"/2024");
        }



        System.out.println(Arrays.toString(modelArray));

        return modelArray;
    }


    public PaymentsSummary userPaymentsSummary(UserModel user) {
        List<PaymentModel> paymentModelList = paymentRepository.findByUser(user);
        PaymentsSummary.Info[] info = new PaymentsSummary.Info[12];
        for(int i = 0; i < 12; i++)
            info[i] = new PaymentsSummary.Info(i, months[i], null, null);




        int openPayments = 12;
        for(PaymentModel paymentModel : paymentModelList) {
            int lastMonth = paymentModel.getPaymentMonth();

            // IF ALREADY PAID
            if(paymentModel.getClosedAt() != null) {
                info[lastMonth] = new PaymentsSummary.Info(lastMonth, months[lastMonth], paymentModel.getClosedAt().toString(), null);
                openPayments --;
                continue;
            }

            String pixUrl = paymentModel.getPixUrl();
            LocalDateTime expiresAt = paymentModel.getExpiresAt();

            // IF NOT EXPIRED
            if (pixUrl != null && expiresAt != null && expiresAt.isAfter(LocalDateTime.now()))
                info[lastMonth] = new PaymentsSummary.Info(lastMonth, months[lastMonth], paymentModel.getClosedAt().toString(), paymentModel.getPixUrl());

        }

        return new PaymentsSummary(info, openPayments, 12-openPayments);
    }
*/

}
