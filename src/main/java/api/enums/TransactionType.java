package api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

public enum TransactionType {

	TRANSFER, WITHDRAW, DEPOSIT, PAYMENT;

	@JsonCreator
	public static TransactionType create(String name) {
		name = name.toUpperCase();
		return valueOf(name);
	}

	@JsonValue
	@Override
	public String toString() {
		return super.toString();
	}

}
