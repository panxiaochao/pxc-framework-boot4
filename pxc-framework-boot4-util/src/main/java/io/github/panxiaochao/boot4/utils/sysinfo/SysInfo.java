package io.github.panxiaochao.boot4.utils.sysinfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * SysInfo Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Setter
@Getter
@ToString
public class SysInfo {

    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    private String computerIp;

    /**
     * DNS
     */
    private String dns;

    /**
     * IPV4网关
     */
    private String ipv4Gateway;

    /**
     * IPV6网关
     */
    private String ipv6Gateway;

    /**
     * 项目路径
     */
    private String userDir;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

}
