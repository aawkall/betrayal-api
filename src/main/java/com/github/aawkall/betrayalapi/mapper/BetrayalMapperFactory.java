package com.github.aawkall.betrayalapi.mapper;

import javax.inject.Inject;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.github.aawkall.betrayalapi.entity.db.Betrayal;
import com.github.aawkall.betrayalapi.entity.db.Player;
import com.github.aawkall.betrayalapi.entity.response.BetrayalResponse;
import com.github.aawkall.betrayalapi.entity.response.PlayerResponse;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Component
public class BetrayalMapperFactory implements FactoryBean<MapperFactory> {

	@Inject
	BetrayalMapper betrayalMapper;

	@Inject
	PlayerMapper playerMapper;

	@Override
	public MapperFactory getObject() throws Exception {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.classMap(Betrayal.class, BetrayalResponse.class)
			.customize(betrayalMapper).register();
		mapperFactory.classMap(Player.class, PlayerResponse.class)
			.customize(playerMapper).register();
		return mapperFactory;
	}

	@Override
	public Class<?> getObjectType() {
		return MapperFactory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}