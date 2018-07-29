package com.fbi.fbdata.product;

import com.fbi.fbdata.FBData;
import com.fbi.fbo.product.Product;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;



@Entity(name = "ProductConversion")
@Table(name = "bi_upctouom", uniqueConstraints = { @UniqueConstraint(columnNames = { "productId", "uomFromId" }) }, indexes = { @Index(columnList = "productId", name = "product_id"), @Index(columnList = "uomFromId", name = "uom_id") })
@SequenceGenerator(name = "product_conversion_sequence", sequenceName = "GENPRODUCTCONVERSIONID")
public class ProductConversionFPO implements FBData<ProductConversionFPO> {

    public static final int COL_LENGTH_NAME = 255;
    public static final int COL_LENGTH_CODE = 255;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "product_conversion_sequence")
    @Column(nullable = false)
    private int id;

    @Column(nullable = false, name = "productId")
    @NotNull
    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    private ProductFpo getProduct() {
        return product;
    }

    private void setProduct(ProductFpo product) {
        this.product = product;
    }

    public String getUpcCode() {
        return upcCode;
    }

    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }

    public int getUomFromId() {
        return uomFromId;
    }

    public void setUomFromId(int uomFromId) {
        this.uomFromId = uomFromId;
    }

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProductFpo.class, optional = false)
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    private ProductFpo product;


    @Type(type = "com.fbi.fbdata.system.FBStringHibernateType")
    @Column(length = 20, nullable = false)
    @Length(max = 20)
    private String upcCode;

    @Column(name = "uomFromId", nullable = false)
    @NotNull
    private int uomFromId;


    public ProductConversionFPO(){
        this.id = -1;
        this.setDefaultValues();
    }

    private void setDefaultValues() {
        this.upcCode = "";
    }


    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int i) {
        this.id = i;
    }

    @Override
    public int compareTo(ProductConversionFPO productConversionFPO) {
        return 0;
    }
}
