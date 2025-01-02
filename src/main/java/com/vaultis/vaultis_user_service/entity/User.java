package com.vaultis.vaultis_user_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

	@Id @GeneratedValue
	private Long id;
	
	private String email;
	
	private String publicKey;
	
	public User(String email) {
		this.email = email;
	}
	
	public User updatePublicKey(String publicKey) {
		this.publicKey = publicKey;
		return this;
	}
	
}
