package cn.jiiiiiin.security.core.properties.refresh;

import cn.jiiiiiin.security.core.properties.BrowserProperties;
import cn.jiiiiiin.security.core.properties.SecurityProperties;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author jiiiiiin
 */
@Component
@Slf4j
@AllArgsConstructor
public class SecurityPropertiesRefreshConfig {

    private final SecurityProperties securityProperties;
    private final RefreshScope refreshScope;

    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        boolean keysChanged = false;
        for (String changedKey : changeEvent.changedKeys()) {
            if (changedKey.startsWith("jiiiiiin.security")) {
                keysChanged = true;
                break;
            }
        }
        if (!keysChanged) {
            return;
        }
        log.debug("监听到SecurityPropertiesRefreshConfig更新通知 before refresh {}", securityProperties.toString());
        refreshScope.refresh("securityProperties");
        log.debug("监听到SecurityPropertiesRefreshConfig更新通知 after refresh {}", securityProperties.toString());
    }
}
