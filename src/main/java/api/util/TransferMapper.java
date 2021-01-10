package api.util;

import api.db.model.Transfer;
import api.response.TransferDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Created by Mehmet Aktas on 2020-12-12
 * <p>
 * Utility class for transfer operations. Utilities method will be added here
 */

@Mapper(componentModel = "spring")
public interface TransferMapper {

	/**
	 * This method will convert db transfer object to transfer DTO
	 *
	 * @param transfer
	 * @return
	 */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public TransferDto convertToTransferDto(Transfer transfer);

}
