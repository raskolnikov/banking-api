package api.util;

import api.db.model.Account;
import api.response.AccountDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Created by Mehmet Aktas on 2020-12-12
 */

@Mapper(componentModel = "spring")
public interface AccountMapper {

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public AccountDto convertToAccountDto(Account account);
}
