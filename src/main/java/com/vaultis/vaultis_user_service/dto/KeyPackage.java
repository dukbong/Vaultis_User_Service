package com.vaultis.vaultis_user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyPackage {

	private String publicKey;
	private String privateKey;
	
}
