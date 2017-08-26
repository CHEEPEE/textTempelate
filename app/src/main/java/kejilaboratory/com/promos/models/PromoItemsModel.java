package kejilaboratory.com.promos.models;

import java.lang.ref.SoftReference;

/**
 * Created by Keji's Lab on 10/06/2017.
 */

public class PromoItemsModel {
    private String promoCode;
    private String promoDes;
    private String promoNumber;
    private String promoId;

    public String getPromoCode(){
        return promoCode;
    }
    public void setPromoCode(String promo_code){
        promoCode = promo_code;
    }
    public String getPromoDes(){
        return promoDes;
    }
    public void setPromoDes(String promo_des){
        promoDes = promo_des;
    }
    public String getPromoNumber(){
        return promoNumber;
    }
    public void setPromoNumber(String promo_number){
        promoNumber = promo_number;
    }
    public String getPromoId(){
        return promoId;
    }
    public void setPromoId(String promo_id){
        promoId=promo_id;
    }
}

