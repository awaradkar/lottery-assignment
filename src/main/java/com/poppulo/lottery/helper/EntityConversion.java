package com.poppulo.lottery.helper;

import org.modelmapper.ModelMapper;

public class EntityConversion {

	@SuppressWarnings("unchecked")
	public static <T> T convertModel(T entityObj, T dtoObj, int i) {
		ConversionEnum conEnum = ConversionEnum.values()[i];
		ModelMapper modelMapper = new ModelMapper();
		switch (conEnum) {
		case ENTITYTODTO:
			dtoObj = (T) modelMapper.map(entityObj, dtoObj.getClass());
			return dtoObj;

		case DTOTOENTITY:
			entityObj = (T) modelMapper.map(dtoObj, entityObj.getClass());
			return entityObj;

		default:
			return null;
		}
	}

	public static enum ConversionEnum {
		ENTITYTODTO, DTOTOENTITY;
	}

}
