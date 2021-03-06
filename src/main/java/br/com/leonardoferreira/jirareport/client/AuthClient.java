package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.form.LoginForm;
import br.com.leonardoferreira.jirareport.domain.vo.CurrentUser;
import br.com.leonardoferreira.jirareport.domain.vo.SessionInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Created by lferreira on 3/26/18
 */
@FeignClient(name = "auth-client", url = "${jira.url}")
public interface AuthClient {

    @GetMapping("/rest/auth/1/session")
    SessionInfo login(@RequestBody LoginForm loginForm);

    @GetMapping("/rest/api/2/myself")
    CurrentUser findCurrentUser(@RequestHeader("cookie") String token);

}
