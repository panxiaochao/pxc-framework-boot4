package io.github.panxiaochao.boot4.utils.sysinfo;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * ServerInfo Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Getter
@Setter
public class ServerInfo {

    /**
     * CPU 相关信息
     */
    private Cpu cpu = new Cpu();

    /**
     * 內存 相关信息
     */
    private Mem mem = new Mem();

    /**
     * JVM 相关信息
     */
    private Jvm jvm = new Jvm();

    /**
     * 服务器 相关信息
     */
    private SysInfo sys = new SysInfo();

    /**
     * 磁盘存储 相关信息
     */
    private DiskInfo diskInfo = new DiskInfo();

    /**
     * 磁盘文件 相关信息
     */
    private List<DiskInfo> diskInfos = new LinkedList<>();

}
