package com.ebgr.pagamento_carnes.service;


import com.ebgr.pagamento_carnes.controller.dto.PaymentDTO;
import com.ebgr.pagamento_carnes.controller.dto.PaymentMonthDTO;
import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.ebgr.pagamento_carnes.model.PaymentModel;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
        List<PaymentModel> paymentModels = paymentRepository.findAllByUserAndExpiresAtAfterOrExpiresAtIsNull(userModel, LocalDateTime.now());

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
        CobrancaImediata.Response cobrancaImediata = efiHelper.criarCobrancaImediata(new DTO_efi.Devedor("70292933479", "Erbert"), 2, paymentModel_1.getTxid());
        GerarQRCode.Response qrCode = efiHelper.criarQrCode(cobrancaImediata);

        final String criacao = cobrancaImediata.calendario().criacao();
        final int expiracao = cobrancaImediata.calendario().expiracao();
        paymentModel_1.setExpiresAt(parseLocalDateTime(criacao).plusSeconds(expiracao));
        paymentModel_1.setPixUrl(qrCode.linkVisualizacao());

        paymentRepository.save(paymentModel_1);

        return new PaymentDTO(
                paymentModel_1.getPixUrl(),
                paymentModel_1.getExpiresAt()
        );
    }

    private static LocalDateTime parseLocalDateTime (String isoDate) {
        return LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME)
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
    }


}
