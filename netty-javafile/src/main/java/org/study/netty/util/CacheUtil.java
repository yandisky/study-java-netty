package org.study.netty.util;

import org.study.netty.domain.FileBurstInstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtil {
    public static Map<String, FileBurstInstruct> burstDataMap = new ConcurrentHashMap<>();
}
