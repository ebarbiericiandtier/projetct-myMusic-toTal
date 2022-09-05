package com.ciandt.summit.bootcamp2022.service.impl;

import com.ciandt.summit.bootcamp2022.service.TokenProviderService;
import org.assertj.core.api.AbstractAssert;

public class TokenProviderServiceAssert extends AbstractAssert<TokenProviderServiceAssert,TokenProviderService> {

    public TokenProviderServiceAssert(TokenProviderServiceImpl tokenProviderService){
        super(tokenProviderService,TokenProviderServiceAssert.class);
    }

    public static TokenProviderServiceAssert assertThat(TokenProviderService actual){
        return new TokenProviderServiceAssert((TokenProviderServiceImpl) actual);
    }

    TokenProviderServiceAssert returnsAsString(String username){
        isNotNull();
        if(actual.getToken(username)==null) {
            failWithMessage(
                    "Esperavamos retornar uma string"
            );
        }
        return this;
    }



}
