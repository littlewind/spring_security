package com.littlewind.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name="shop_id")
    private Long id;
    
    private Long shop_id;

    private String name;
    
    @Column(name = "create_date", nullable = false)
    private Date createDate = new Date();
    
    
    private Long status = 1L;
    
    public Shop() {
		super();
	}
    
	public Shop(Long shop_id, String name) {
		super();
		this.shop_id = shop_id;
		this.name = name;
		this.createDate = new Date();
	}
	
	public Shop(Long shop_id, String name, Long status) {
		this(shop_id, name);
		if (status!=null) {
			this.status = status;
		} else {
			this.status = 1L;
		}

	}




	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
    
    
}
