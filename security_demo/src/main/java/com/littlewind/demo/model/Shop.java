package com.littlewind.demo.model;

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
    
    public Shop() {
		super();
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

}
