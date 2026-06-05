package io.github.panxiaochao.boot4.email.config;

import cn.hutool.extra.mail.MailAccount;
import io.github.panxiaochao.boot4.email.config.properties.EmailProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * Email 自动配置类
 * </p>
 *
 * @author Lypxc
 * @since 2026-06-05
 */
@AutoConfiguration
@EnableConfigurationProperties(EmailProperties.class)
@ConditionalOnProperty(name = "spring.pxc-framework-boot4.email.enabled", havingValue = "true")
public class EmailAutoConfiguration {

    /**
     * 创建邮件账户配置。
     * @param emailProperties 邮件配置属性
     * @return 邮件账户
     */
    @Bean
    public MailAccount mailAccount(EmailProperties emailProperties) {
        return emailProperties.toMailAccount();
    }

}
