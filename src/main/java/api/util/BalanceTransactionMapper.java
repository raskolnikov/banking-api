package api.util;

import api.db.model.BalanceTransaction;
import api.response.BalanceTransactionDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Created by Mehmet Aktas on 2020-12-12
 */


@Mapper(componentModel = "spring")
public interface BalanceTransactionMapper {


	/**
	 * This method will convert balanceTransaction to balanceTransactionDto
	 *
	 * @param balanceTransaction
	 * @return
	 */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public BalanceTransactionDto convertToBalanceTransactionDto(BalanceTransaction balanceTransaction);


}
