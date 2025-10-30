package com.minvoice.demo.application.services.interfaces;


public interface IInfoInvoiceService {
    double getTotalBilled();
    double getTotalPaid();
    double getPaymentDue();
    int getInvoiceCount();
}
