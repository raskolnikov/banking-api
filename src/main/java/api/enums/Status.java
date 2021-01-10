package api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

public enum Status {

	ACTIVE, INACTIVE, DELETED;

	@JsonCreator
	public static Status create(String name) {
		name = name.toUpperCase();
		return valueOf(name);
	}

	@JsonValue
	@Override
	public String toString() {
		return super.toString();
	}

}
