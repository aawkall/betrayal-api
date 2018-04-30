package com.github.aawkall.betrayalapi.mapper;

import org.springframework.stereotype.Component;

import com.github.aawkall.betrayalapi.entity.db.Betrayal;
import com.github.aawkall.betrayalapi.entity.response.BetrayalResponse;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

@Component
public class BetrayalMapper extends CustomMapper<Betrayal, BetrayalResponse> {

	@Override
	public void mapAtoB(Betrayal betrayal, BetrayalResponse betrayalResponse, MappingContext context) {
		betrayalResponse.setBetrayalId(betrayal.getBetrayalId());
		betrayalResponse.setTimeCreated(betrayal.getTimeCreated());
		betrayalResponse.setHauntNumber(betrayal.getHauntNumber());
	}
}