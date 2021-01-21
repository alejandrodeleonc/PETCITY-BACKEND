package Encapsulaciones;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="INFORMACION_PAYPAL")
public class PaypalInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    String paypal_id;
    String payer_id;
    String status;
    String payerName;
    String descripcion;
    String moneda;
    float value;
    float tax;
    Date created;
    String payee;
    String merchant_id;

    public PaypalInfo() {
    }

    public PaypalInfo(String paypal_id, String payer_id, String status, String payerName, String descripcion, String moneda, float value, float tax, Date created, String payee, String merchant_id) {
        this.paypal_id = paypal_id;
        this.payer_id = payer_id;
        this.status = status;
        this.payerName = payerName;
        this.descripcion = descripcion;
        this.moneda = moneda;
        this.value = value;
        this.tax = tax;
        this.created = created;
        this.payee = payee;
        this.merchant_id = merchant_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaypal_id() {
        return paypal_id;
    }

    public void setPaypal_id(String paypal_id) {
        this.paypal_id = paypal_id;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }
}
