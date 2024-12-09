package com.ebgr.pagamento_carnes.service;


import com.ebgr.pagamento_carnes.controller.dto.PaymentMonthDTO;
import com.ebgr.pagamento_carnes.controller.dto.WebhookDTO;
import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.ebgr.pagamento_carnes.model.PaymentModel;
import com.ebgr.pagamento_carnes.model.UserModel;
import com.ebgr.pagamento_carnes.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${efi.pixValue}")
    private double pixValue;


    public List<PaymentMonthDTO> getUserPayments(String login) {
        UserModel userModel = userService.findUserOrThrow(login);
        List<PaymentModel> paymentModels = paymentRepository.findAllByUserAndExpiresAtAfterOrExpiresAtIsNull(userModel, LocalDateTime.now());

        return paymentModels.stream().map(PaymentModel::serialize).toList();
    }

    private PaymentModel getValidInstance(UserModel userModel, int month, int year) {
        final PaymentModel paymentModel = paymentRepository.findByUserAndPaymentMonthAndPaymentYear(
                userModel, month, year).orElse(null);
        if(paymentModel != null) {
            if (paymentModel.getPixUrl() != null && paymentModel.getExpiresAt().isAfter(LocalDateTime.now()))
                return paymentModel;
            else
                paymentRepository.delete(paymentModel);
        }
        return null;
    }

    public PaymentMonthDTO createOrGetPayment(String login, int month, int year) {
        UserModel userModel = userService.findUserOrThrow(login);

        PaymentModel previousInstance = getValidInstance( userModel, month, year );
        if(previousInstance != null)
            return previousInstance.serialize();


        final PaymentModel paymentModel = new PaymentModel(userModel, month, year);
        final CobrancaImediata.Response cobrancaImediata = efiHelper.criarCobrancaImediata(new DTO_efi.Devedor("70292933479", "Erbert"));
        final GerarQRCode.Response qrCode = efiHelper.criarQrCode(cobrancaImediata);
        //efiHelper.criarWebhook(cobrancaImediata);
        new Thread(()-> efiHelper.criarWebhook(cobrancaImediata)).start();

        final String criacao = cobrancaImediata.calendario().criacao();
        final int expiracao = cobrancaImediata.calendario().expiracao();
        paymentModel.setExpiresAt(parseLocalDateTime(criacao).plusSeconds(expiracao));
        paymentModel.setPixUrl(qrCode.linkVisualizacao());
        paymentModel.setTxid(cobrancaImediata.txid());


        paymentRepository.save(paymentModel);
        return paymentModel.serialize();
    }

    public boolean confirmPayment(WebhookDTO webhookDTO) {
        PaymentModel paymentModel = paymentRepository.findByTxid(webhookDTO.pix()[0].txid()).orElse(null);
        if (paymentModel == null)
            return false;

        paymentModel.setClosedAt(webhookDTO.pix()[0].horario().toLocalDateTime());
        paymentRepository.save(paymentModel);
        return true;
    }

    private static LocalDateTime parseLocalDateTime (String isoDate) {
        return LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME)
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
    }




    public void verifyPayment(PaymentModel paymentModel) {
        System.err.println("verifyPayment: " + efiHelper.verificarCobranca(paymentModel.getTxid()));
        //System.err.println("deveria estar verificando");
    }

}
