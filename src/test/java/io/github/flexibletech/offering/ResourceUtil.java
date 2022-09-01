package io.github.flexibletech.offering;

import com.google.common.io.Files;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResourceUtil {
    private ResourceUtil() {
    }

    public static String getString(String path) throws IOException {
        return Files.asCharSource(
                        ResourceUtils.getFile(path),
                        Charset.defaultCharset())
                .read();
    }

    public static byte[] getByteArray(String path) throws IOException {
        return getString(path).getBytes(StandardCharsets.UTF_8);
    }
}
