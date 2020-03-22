package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.gateway.api.security.annotation.PermitAll;
import net.jaggerwang.scip.common.entity.UserEntity;
import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MutationAuthLoginDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    @PermitAll
    public Object get(DataFetchingEnvironment env) {
        var userInput = objectMapper.convertValue(env.getArgument("user"), UserDto.class);
        String username = null;
        if (userInput.getUsername() != null)  {
            username = userInput.getUsername();
        } else if (userInput.getMobile() != null) {
            username = userInput.getMobile();
        } else if (userInput.getEmail() != null) {
            username = userInput.getEmail();
        }
        if (StringUtils.isEmpty(username)) {
            throw new UsecaseException("用户名、手机或邮箱不能都为空");
        }
        var password = userInput.getPassword();
        if (StringUtils.isEmpty(password)) {
            throw new UsecaseException("密码不能为空");
        }

        return monoWithContext(loginUser(username, password)
                .flatMap(loggedUser -> userAsyncService.info(loggedUser.getId())), env);
    }
}
